package com.studyProjectA.ShoppingMall.controller;
import com.studyProjectA.ShoppingMall.dto.LoginRequestDto;
import com.studyProjectA.ShoppingMall.dto.RegisterDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import com.studyProjectA.ShoppingMall.service.CartService;
import com.studyProjectA.ShoppingMall.service.ProductService;
import com.studyProjectA.ShoppingMall.service.UserService;
import com.studyProjectA.ShoppingMall.response.Response;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.studyProjectA.ShoppingMall.response.Response.success;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @ApiOperation(value = "마이페이지" ,notes = "마이 페이지를 조회합니다. ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/myPage")
    public Response myPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        return Response.success(UserDto.toDto(user));
    }

    @ApiOperation(value = "회원가입", notes = "회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/join")
    public Response register(@RequestBody RegisterDto registerDto) {
        return Response.success(userService.register(registerDto));
    }

    @ApiOperation(value = "로그인")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping("/login")
    public Response login(@RequestBody LoginRequestDto loginRequestDto){
        return Response.success(userService.login(loginRequestDto));
    }

    @ApiOperation(value = "관리자 페이지", notes = "관리자페이지 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/admin")
    public Response admin(){
        return Response.success();
    }

    @ApiOperation(value = "매니저 페이지", notes = "매니저페이지 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/manager")
    public Response manager(){
        return Response.success();
    }
}
