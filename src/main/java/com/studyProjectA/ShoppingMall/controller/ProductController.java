package com.studyProjectA.ShoppingMall.controller;

import com.studyProjectA.ShoppingMall.dto.ProductResponseDto;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import com.studyProjectA.ShoppingMall.response.Response;
import com.studyProjectA.ShoppingMall.service.CartService;
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
    private final CartService cartService;

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

    // 품목이름 검색하기
    @ApiOperation(value = "품목 검색하기",notes = "검색한 품목을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/search/{searchProductName}")
    public Response getSearchProducts(@PathVariable("searchProductName") String searchProductName){
        return Response.success(productService.getSearchProducts(searchProductName));
    }

    //사용자검색으로 아이템 목록보기
    //예를 들어 /product/search/{searchProductName}이거랑
    ///products/search/{userName} 이거랑 둘다 String으로 받아서 서버에서 둘 중
    //어디로 갈지 모름 그래서 경로를 다르게함 String 뿐만아니라 Long도 같음음

    @ApiOperation(value = "검색한 유저가 등록한 품목 검색하기",notes = "검색한 유저의 품목을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/searchUser/{userName}")
    public Response getUserProducts(@PathVariable("userName") String userName){
        return Response.success(productService.getUserProducts(userName));
    }

    // 물품 등록
    @ApiOperation(value = "아이템 등록", notes = "아이템을 등록한다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/products/load")
    public Response loadProduct(@RequestBody ProductResponseDto productResponseDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);

        return Response.success(productService.addProduct(productResponseDto, loginUser));
    }

    // 물품 수정
    @ApiOperation(value = "아이템 수정", notes = "아이템을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/products/update/{itemId}")
    public Response updateProduct(@PathVariable("itemId") Integer itemId, @RequestBody ProductResponseDto productResponseDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        return Response.success(productService.updateProduct(itemId, productResponseDto, loginUser));

    }

    // 물품 삭제
    @ApiOperation(value = "물품 삭제", notes = "물품을 삭제한다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/products/delete/{itemId}")
    public Response deleteProduct(@PathVariable("itemId") Long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        productService.deleteProduct(itemId, loginUser);
        return Response.success("삭제 완료");
    }

    @ApiOperation(value = "장바구니 담기", notes = "장바구니에 선택한 품목을 선택합니다. ")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/products/{productId}/take/{quantity}")
    public Response includeProductToMyCart(@PathVariable("productId")String productId, @PathVariable("quantity") String quantity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        return success(cartService.includeProductToCart(loginUser, Long.parseLong(productId), Integer.parseInt(quantity)));
    }
}