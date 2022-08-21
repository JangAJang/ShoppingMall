package com.studyProjectA.ShoppingMall.controller;


import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import com.studyProjectA.ShoppingMall.response.Response;
import com.studyProjectA.ShoppingMall.service.CartService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.studyProjectA.ShoppingMall.response.Response.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @ApiOperation(value = "결제하기", notes = "장바구니의 모든 품목의 가격을 합산해 알려줍니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users/myPage/myCart/checkout")
    public Response checkPayment(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        return success(cartService.checkPayment(user));
    }

    @ApiOperation(value = "장바구니 품목 삭제", notes = "장바구니에서 선택한 품목을 제거합니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users/myPage/myCart/delete/{cartItem}")
    public Response excludeFromMyCart(@PathVariable("cartItem")Long cartItem){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        return success(cartService.excludeProductFromCart(user, cartItem));
    }

    @ApiOperation(value = "장바구니 담기", notes = "장바구니에 선택한 품목을 선택합니다. ")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/products/{productId}/take/{quantity}")
    public Response includeProductToMyCart(@PathVariable("productId")String productId, @PathVariable("quantity") String quantity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        return success(cartService.includeProductToCart(loginUser, Long.parseLong(productId), Integer.parseInt(quantity)));
    }

    @ApiOperation(value = "장바구니 품목 보기", notes = "전체 장바구니 품목을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/myPage/myCart")
    public Response showMyCart(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        return success(cartService.getMyCart(user));
    }




}
