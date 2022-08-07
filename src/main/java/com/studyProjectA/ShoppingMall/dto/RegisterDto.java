package com.studyProjectA.ShoppingMall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotNull(message = "username을 입력해주세요.")
    private String username;

    @NotNull(message = "password를 입력해주세요.")
    private String password;

    @NotNull(message = "email을 입력해주세요.")
    private String email;

    @NotNull(message = "address를 입력해주세요.")
    private String address;
}
