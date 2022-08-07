package com.studyProjectA.ShoppingMall.controller;

import com.studyProjectA.ShoppingMall.dto.RegisterDto;
import com.studyProjectA.ShoppingMall.service.UserService;
import com.studyProjectA.ShoppingMall.response.Response;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "전체 회원 보기 ", notes = "전체 회원을 조회한다. ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response findAll() {
        return Response.success(userService.findAll());
    }

    @ApiOperation(value = "유저 찾기", notes = "개별 유저 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    public Response findUser(@PathVariable("id") Integer id) {
        return Response.success(userService.findUser(id));
    }

    @ApiOperation(value = "회원가입", notes = "회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/auth")
    public Response register(@RequestBody RegisterDto registerDto) {
        return Response.success(userService.register(registerDto));
    }
}
