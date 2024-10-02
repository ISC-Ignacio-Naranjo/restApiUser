package com.restapiuser.service;
import com.restapiuser.entities.User;
import com.restapiuser.model.LoginRequest;
import com.restapiuser.model.UserRequest;
import com.restapiuser.model.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<UserResponse> signup(UserRequest userRequest);
    ResponseEntity<String> login(LoginRequest loginRequest);

    List<User> getAllUsers();
}
