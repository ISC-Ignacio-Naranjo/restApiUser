package com.restapiuser.service.impl;


import com.restapiuser.components.UserParser;
import com.restapiuser.model.UserRequest;
import com.restapiuser.util.UserRegistrationException;
import com.restapiuser.entities.User;
import com.restapiuser.repository.UserRepository;
import com.restapiuser.security.CustomerDetailsService;
import com.restapiuser.security.jwt.JwtUtil;
import com.restapiuser.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
    private UserParser userParser;

    public User singUp(UserRequest userRequest) {
        log.info("singUp method {}", userRequest);
        validateUserRequest(userRequest);
        User newUser = userParser.createUserFromRequest(userRequest);
        return userRepository.save(newUser);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info(" Login Method");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (customerDetailsService.getUserDetail().isActive()){
                return new ResponseEntity<String>(
                        "{\"token\":\"" +
                                jwtUtil.generateToken(customerDetailsService.getUserDetail().getEmail(),
                                        customerDetailsService.getUserDetail().getName()) + "\"}",
                        HttpStatus.OK);

            }else {
                return new ResponseEntity<String>("{\"Message\":\""+"Wait for admin approve "+""+"\"}", HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            log.error("{}", e);
        }
        return new ResponseEntity<String>("{\"Message\":\""+"Wrong Credentials"+""+"\"}", HttpStatus.BAD_REQUEST);

    }

    @Override
    public List<User> findUsers() {
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

    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        //user.SetP(requestMap.get("numeroDeContacto"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setActive(false);
        return  user;

    }
}
