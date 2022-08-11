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

    @NotNull(message = "아이디를 입력해주세요")
    private String username;

    @NotNull(message = "비밀번호를 입력해주세요")
    private String password;

    @NotNull(message = "이메일을 입력해주세요")
    private String email;

    @NotNull
    private String roll;

    @NotNull(message = "주소를 입력해주세요")
    private String address;

    private String provider;

    private String providerId;

    public static UserDto toDto(User user){
        return new UserDto(
                user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), user.getAddress(), user.getProvider(), user.getProviderId()
        );
    }
}
