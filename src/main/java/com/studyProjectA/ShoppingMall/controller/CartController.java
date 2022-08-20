package com.studyProjectA.ShoppingMall.controller;

import com.studyProjectA.ShoppingMall.auth.PrincipalDetails;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.repository.ProductRepository;
import com.studyProjectA.ShoppingMall.response.Response;
import com.studyProjectA.ShoppingMall.service.CartService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final ProductRepository productRepository;

    @ApiOperation(value = "카트 조회하기", notes = "카트의 모든 상품을 불러옵니다. ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/items")
    public Response showMyCart(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        return Response.success(cartService.showMyCartItems(user));
    }

}
