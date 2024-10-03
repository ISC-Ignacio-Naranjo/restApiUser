package com.restapiuser.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {

    @Schema(example = "Ignacio Naranjo")
    private String name;
    @Schema(example = "joseignacio.naranjoguerra@gmail.com")
    private String email;
    @Schema(example = "password123")
    private String password;
    @Schema(example = "[\n" +
            "        {\n" +
            "            \"number\": \"1234567\",\n" +
            "            \"citycode\": \"1\",\n" +
            "            \"contrycode\": \"57\"\n" +
            "        }\n" +
            "    ]")
    private List<PhoneRequest> phones;
}
