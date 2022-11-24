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
    public User setUser(RegisterDto registerDto){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.username = registerDto.getUsername();
        this.password = bCryptPasswordEncoder.encode(registerDto.getPassword());
        this.email = registerDto.getEmail();
        this.role = "ROLE_USER";
        this.address = registerDto.getAddress();
        return this;
    }

    public boolean isRightPassword(String encryptedPassword){
        return this.password.equals(encryptedPassword);
    }
}