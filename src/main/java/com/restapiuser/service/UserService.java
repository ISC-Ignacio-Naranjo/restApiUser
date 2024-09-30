package com.restapiuser.service;
import com.restapiuser.entities.User;
import com.restapiuser.model.UserRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    User singUp(UserRequest userRequest);

    ResponseEntity<String> login(Map<String, String> requestMap);

    List<User> findUsers();
}
