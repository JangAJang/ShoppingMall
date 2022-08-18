package com.studyProjectA.ShoppingMall.controller;

import com.studyProjectA.ShoppingMall.auth.PrincipalDetails;
import com.studyProjectA.ShoppingMall.dto.RegisterDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFound;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import com.studyProjectA.ShoppingMall.service.UserService;
import com.studyProjectA.ShoppingMall.response.Response;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @ApiOperation(value = "전체 회원 보기 ", notes = "전체 회원을 조회한다. ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Response findAll() {
        return Response.success(userService.findAll());
    }

    @ApiOperation(value = "마이페이지" ,notes = "마이 페이지를 조회합니다. ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{username}")
    public Response myPage(@PathVariable("username")String username){
        if(getUser().getUsername().equals(username)){
            System.out.println("로그인 확인 완료");
            System.out.println(getUser());
            return Response.success(UserDto.toDto(getUser()));
        }
        else{
            return Response.failure(404, "사용자가 일치하지 않습니다. ");
        }
    }

    @ApiOperation(value = "회원가입", notes = "회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/join")
    public Response register(@RequestBody RegisterDto registerDto) {
        return Response.success(userService.register(registerDto));
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

    @ApiOperation(value = "등록 상품 목록", notes = "사용자가 등록한 상품을 표시합니다. ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/my-products")
    public Response showMyProducts(){
        return Response.success();
    }

    User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFound::new);
        return loginUser;
    }


}
