package com.studyProjectA.ShoppingMall.dto;

import com.studyProjectA.ShoppingMall.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    private int id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String roll;

    @NotNull
    private String address;

    private String provider;

    private String providerId;

    public static UserDto toDto(User user){
        return new UserDto(
                user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), user.getAddress(), user.getProvider(), user.getProviderId()
        );
    }
}
