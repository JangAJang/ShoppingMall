package com.studyProjectA.ShoppingMall.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String address;

    @Column(nullable = true)
    private String provider;

    @Column(nullable = true)
    private String providerId;

    public List<String> getRoleList(){
        if(this.role.length() >0 ){
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

    @Builder
    public User(String username, String password, String email, String role, String provider, String providerId, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.address = address;
    }

}