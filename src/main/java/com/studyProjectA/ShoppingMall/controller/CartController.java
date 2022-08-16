package com.studyProjectA.ShoppingMall.controller;

import com.studyProjectA.ShoppingMall.auth.PrincipalDetails;
import com.studyProjectA.ShoppingMall.dto.CartDto;
import com.studyProjectA.ShoppingMall.dto.CartItemDto;
import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.CartItem;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.response.Response;
import com.studyProjectA.ShoppingMall.service.CartService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class CartController {

    private final CartService cartService;

    @ApiOperation(value = "카트 조회하기", notes = "카트의 모든 상품을 불러옵니다. ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/items")
    public Response showMyCart(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        return Response.success(cartService.showMyCartItems(user));
    }


}
