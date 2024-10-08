package com.restapiuser.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;


    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;
        private String name;
        private String email;
        private String password;

        @OneToMany(cascade = CascadeType.ALL)
        @JoinColumn(name = "user_id")
        private List<Phone> phones;

        private Date created;
        private Date modified;
        private Date lastLogin;
        private String token;
        private boolean isActive;

    }
