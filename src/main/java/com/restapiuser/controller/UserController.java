package com.restapiuser.controller;


import com.restapiuser.entities.User;
import com.restapiuser.model.LoginRequest;
import com.restapiuser.model.UserMapper;
import com.restapiuser.model.UserResponse;
import com.restapiuser.model.UserRequest;
import com.restapiuser.service.UserService;
import com.restapiuser.util.UserRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UserMapper userMapper;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody UserRequest signupRequest) {
        try {
            return userService.signup(signupRequest);
        } catch (UserRegistrationException e) {
            return ResponseEntity.badRequest().body(new UserResponse(null, null, null,null, null, e.getMessage(), false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody(required = true)LoginRequest loginRequest){
        return userService.login(loginRequest);

    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> userDTOs = users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
}
