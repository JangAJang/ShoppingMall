package com.studyProjectA.ShoppingMall.entity;

import com.studyProjectA.ShoppingMall.dto.LoginRequestDto;
import com.studyProjectA.ShoppingMall.dto.RegisterDto;
import com.studyProjectA.ShoppingMall.excpetion.EmailAlreadyExistsException;
import com.studyProjectA.ShoppingMall.excpetion.PasswordNotEqualException;
import com.studyProjectA.ShoppingMall.excpetion.UsernameAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "CART_ID")
    private Cart cart;

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
    public User makeNewUser(RegisterDto registerDto){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .username(registerDto.getUsername())
                .password(bCryptPasswordEncoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .role("ROLE_USER")
                .address(registerDto.getAddress())
                .build();
    }

    public boolean isRightPassword(String encryptedPassword){
        return this.password.equals(encryptedPassword);
    }
}