package com.studyProjectA.ShoppingMall.controller;

import com.studyProjectA.ShoppingMall.dto.ProductResponseDto;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import com.studyProjectA.ShoppingMall.response.Response;
import com.studyProjectA.ShoppingMall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.studyProjectA.ShoppingMall.response.Response.success;


@RequiredArgsConstructor
@RestController

@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    private final UserRepository userRepository;

    // 전체 품목 조회
    @ApiOperation(value = "전체 물품 보기", notes = "전체 품목을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/")
    public Response getProducts() {
        return Response.success(productService.getProducts());
    }

    // 개별 품목 조회
    @ApiOperation(value = "개별 품목보기", notes = "개별 품목을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/")
    public Response getProduct(@RequestParam Long itemId) {
        return Response.success(productService.getProduct(itemId));
    }

    // 품목이름 검색하기
    @ApiOperation(value = "품목 검색하기",notes = "검색한 품목을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/byName/")
    public Response getSearchProducts(@RequestParam String name){
        return Response.success(productService.getSearchProducts(name));
    }

    //사용자검색으로 아이템 목록보기
    //예를 들어 /product/search/{searchProductName}이거랑
    ///products/search/{userName} 이거랑 둘다 String으로 받아서 서버에서 둘 중
    //어디로 갈지 모름 그래서 경로를 다르게함 String 뿐만아니라 Long도 같음음

    @ApiOperation(value = "검색한 유저가 등록한 품목 검색하기",notes = "검색한 유저의 품목을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/byUser/")
    public Response getUserProducts(@RequestParam String userName){
        return Response.success(productService.getUserProducts(userName));
    }

    // 물품 등록
    @ApiOperation(value = "아이템 등록", notes = "아이템을 등록한다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/products/")
    public Response loadProduct(@RequestBody ProductResponseDto productResponseDto) {
        return Response.success(productService.addProduct(getLoginUserInfo(), productResponseDto));
    }

    // 물품 수정
    @ApiOperation(value = "아이템 수정", notes = "아이템을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/products/")
    public Response updateProduct(@RequestParam Long itemId, @RequestBody ProductResponseDto productResponseDto) {
        return Response.success(productService.updateProduct(getLoginUserInfo(), itemId, productResponseDto));

    }

    // 물품 삭제
    @ApiOperation(value = "물품 삭제", notes = "물품을 삭제한다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/products/")
    public Response deleteProduct(@RequestParam Long itemId) {
        return Response.success(productService.deleteProduct(getLoginUserInfo(), itemId));
    }

    @ApiOperation(value = "나의 판매상품보기", notes = "내가 등록한 판매상품을 확인합니다. ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/my")
    public Response showMyProduct(){
        return success(productService.getMyProducts(getLoginUserInfo()));
    }

    private User getLoginUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
    }
}