package com.studyProjectA.ShoppingMall.controller;

import com.studyProjectA.ShoppingMall.dto.ProductResponseDto;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import com.studyProjectA.ShoppingMall.response.Response;
import com.studyProjectA.ShoppingMall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController

@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    private final UserRepository userRepository;

    // 전체 품목 조회
    @ApiOperation(value = "전체 물품 보기", notes = "전체 품목을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products")
    public Response getProducts() {

        return Response.success(productService.getProducts());
    }

    // 개별 품목 조회
    @ApiOperation(value = "개별 품목보기", notes = "개별 품목을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/{itemId}")
    public Response getProduct(@PathVariable("itemId") Long itemId) {
        return Response.success(productService.getProduct(itemId));
    }


    // 물품 등록
    @ApiOperation(value = "아이템 등록", notes = "아이템을 등록한다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/products/load")
    public Response loadProduct(@RequestBody ProductResponseDto productResponseDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        return Response.success(productService.addProduct(productResponseDto, loginUser));
    }

    // 물품 수정
    @ApiOperation(value = "아이템 수정", notes = "아이템을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/products/update/{itemId}")
    public Response updateProduct(@PathVariable("itemId") Integer itemId, @RequestBody ProductResponseDto productResponseDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        return Response.success(productService.updateProduct(itemId, productResponseDto, loginUser));

    }

    // 물품 삭제
    @ApiOperation(value = "물품 삭제", notes = "물품을 삭제한다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/products/delete/{itemId}")
    public Response deleteProduct(@PathVariable("itemId") Long itemId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(MemberNotFoundException::new);

        productService.deleteProduct(itemId, loginUser);

        return Response.success("삭제 완료");
    }