package com.studyProjectA.ShoppingMall.controller;

import com.studyProjectA.ShoppingMall.auth.PrincipalDetails;
import com.studyProjectA.ShoppingMall.dto.ProductDto;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.response.Response;
import com.studyProjectA.ShoppingMall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController

public class ProductController {

    private final ProductService productService;
    // private final UserRepository userRepository; -> 이거 지워?????

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
    @GetMapping("/products/{id}")
    public Response getProduct(@PathVariable("id") Integer id) {
        return Response.success(productService.getProduct(id));
    }

    // 물품 등록
    @ApiOperation(value = "아이템 등록", notes = "아이템을 등록한다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/products/load")
    public Response loadProduct(@RequestBody ProductDto productDto, Authentication authentication) {
        
        // 이거는 솔직히 잘 몰라서 참고해서 만들었어(Authentication authentication 이부분을 참고했어 사실 잘 몰라)
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        return Response.success(productService.addProduct(productDto, user));
    }
    

    // 물품 수정
    @ApiOperation(value = "아이템 수정", notes = "아이템을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/products/update/{id}")
    public Response updateProduct(@PathVariable("id") Integer id, @RequestBody ProductDto productDto,
                                  Authentication authentication) {

        // 이거도....
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        if (user.getUsername().equals(productService.getProduct(id).getUserName())) {

            return Response.success(productService.updateProduct(id, productDto));
        } else {
            return Response.failure(404, "해당 작성자만 수정할 수 있습니다.");
        }
    }

    // 물품 삭제
    @ApiOperation(value = "물품 삭제", notes = "물품을 삭제한다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/products/delete/{id}")
    public Response deleteProduct(@PathVariable("id") Integer id, Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        if (user.getUsername().equals(productService.getProduct(id).getUserName())) {

            productService.deleteProduct(id);
            return Response.success("삭제 성공");

        } else {

            return Response.failure(404, "해당 작성자만 수정할 수 있습니다.");
        }
    }
}

