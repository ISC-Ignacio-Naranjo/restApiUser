package com.restapiuser.util;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
public class UsersUtils {

    public static ResponseEntity<String> getResponseEntity(String message, HttpStatus httpStatus){
        return new ResponseEntity<String>("MESSAGE : " + message, httpStatus);
    }
}
