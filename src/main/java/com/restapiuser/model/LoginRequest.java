package com.restapiuser.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(example = "joseignacio.naranjoguerra@gmail.com")
    private String email;
    @Schema(example = "password123")
    private String password;

}

