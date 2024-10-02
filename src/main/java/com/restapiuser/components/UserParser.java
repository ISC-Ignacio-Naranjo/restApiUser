package com.restapiuser.components;

import com.restapiuser.entities.Phone;
import com.restapiuser.entities.User;
import com.restapiuser.model.PhoneRequest;
import com.restapiuser.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserParser {

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public User createUserFromRequest(UserRequest userRequest) {
        User newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setName(userRequest.getName());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        List<Phone> phones = userRequest.getPhones().stream()
                .map(this::createPhoneFromRequest)
                .collect(Collectors.toList());
        newUser.setPhones(phones);

        Date currentDate = new Date();
        newUser.setCreated(currentDate);
        newUser.setModified(currentDate);
        newUser.setLastLogin(currentDate);

        String accessToken = jwtTokenGenerator.generateAccessToken(newUser);
        newUser.setToken(accessToken);
        newUser.setActive(true);

        return newUser;
    }
    private Phone createPhoneFromRequest(PhoneRequest phoneRequest) {
        Phone phone = new Phone();
        phone.setNumber(phoneRequest.getNumber());
        phone.setCityCode(phoneRequest.getCityCode() != null ? phoneRequest.getCityCode() : "");
        phone.setCountryCode(phoneRequest.getContryCode() != null ? phoneRequest.getContryCode() : "");
        return phone;
    }
}

