package com.restapiuser.controller;


import com.restapiuser.constants.UsersConstants;
import com.restapiuser.entities.User;
import com.restapiuser.model.UserRequest;
import com.restapiuser.service.UserService;
import com.restapiuser.util.ErrorResponse;
import com.restapiuser.util.UserRegistrationException;
import com.restapiuser.util.UsersUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserRequest userRequest){
        try {
            User savedUser = userService.singUp(userRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (UserRegistrationException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap){
        try{
            return userService.login(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return UsersUtils.getResponseEntity(UsersConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findUsers();
    }
}
