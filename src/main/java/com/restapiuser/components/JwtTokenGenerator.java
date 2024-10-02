package com.restapiuser.components;


import com.restapiuser.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.Date;

@Component
public class JwtTokenGenerator {


    @Value("${app.jwtSecret}")
    private String secretKey;

    @Value("${app.expirationTimeInMs}")
    private long expirationTimeInMs;

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTimeInMs);

        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }
}
