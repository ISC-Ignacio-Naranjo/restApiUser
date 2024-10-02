package com.restapiuser.service.impl;


import com.restapiuser.components.UserParser;
import com.restapiuser.model.LoginRequest;
import com.restapiuser.model.UserMapper;
import com.restapiuser.model.UserRequest;
import com.restapiuser.model.UserResponse;
import com.restapiuser.util.UserRegistrationException;
import com.restapiuser.entities.User;
import com.restapiuser.repository.UserRepository;
import com.restapiuser.security.CustomerDetailsService;
import com.restapiuser.security.jwt.JwtUtil;
import com.restapiuser.service.UserService;
import com.restapiuser.util.UsersUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.restapiuser.constants.UsersConstants.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserMapper userMapper;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserParser userParser;

    public ResponseEntity<UserResponse> signup(UserRequest userRequest) {
        log.info("singUp method {}", userRequest);
        validateUserRequest(userRequest);
        User newUser = userParser.createUserFromRequest(userRequest);
        User savedUser = userRepository.save(newUser);
        UserResponse userResponse =  userMapper.toDTO(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @Override
    public ResponseEntity<String> login(LoginRequest loginRequest) {
        log.info(" Login Method");
        try {
         return   authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        }catch (Exception e){
            log.error("{}", e);
        }
        return  UsersUtils.getResponseEntity(WRONG_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void validateUserRequest(UserRequest userRequest) {
        if (!isValidEmail(userRequest.getEmail())) {
            throw new UserRegistrationException(EMAIL_FORMAT_INVALID);
        }
        if (!isValidPassword(userRequest.getPassword())) {
            throw new UserRegistrationException(PASS_FORMAT_INVALID);
        }
        User existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser != null) {
            throw new UserRegistrationException(REGISTERED_MAIL);
        }
    }

    private boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }

    private boolean isValidPassword(String password) {
        Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);
        Matcher passwordMatcher = passwordPattern.matcher(password);
        return passwordMatcher.matches();
    }

    public ResponseEntity<String> authenticate(String email, String password) {

        // 1. find user by id in DB if exit
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // 2. Validate passwords
            if (passwordEncoder.matches(password, user.getPassword())) {
                log.info("passwordEncoder {}", passwordEncoder.matches(password, user.getPassword()));

                // 3.Generate and return token
                User userFromCustomerServ = customerDetailsService.getUserDetail(email);

                return new ResponseEntity<String>(
                        "{\"token\":\"" +
                                jwtUtil.generateToken(userFromCustomerServ.getEmail(),
                                        userFromCustomerServ.getName()) + "\"}",HttpStatus.OK);
            }
            return  UsersUtils.getResponseEntity(WRONG_PASSWORD, HttpStatus.UNAUTHORIZED);

        }
     return  UsersUtils.getResponseEntity(WRONG_CREDENTIALS, HttpStatus.UNAUTHORIZED);

    }

}
