package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.ProductResponseDto;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.ProductNotFoundException;
import com.studyProjectA.ShoppingMall.excpetion.UserNotEqualsException;
import com.studyProjectA.ShoppingMall.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;



    // 전체 물품 조회
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProducts(){
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponseDto = new ArrayList<>();
        products.forEach(s-> productResponseDto.add(ProductResponseDto.toDto(s)));
        return productResponseDto;
    }

    // 단건 물품 조회
    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(long id){
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        return ProductResponseDto.toDto(product);
    }

    // 아이템 등록
    @Transactional
    public ProductResponseDto addProduct(ProductResponseDto productResponseDto, User user){

        Product product = new Product();
        product.setProductName(productResponseDto.getProductName());
        product.setCategory(productResponseDto.getCategory());
        product.setDeliveryDate(productResponseDto.getDeliveryDate());
        product.setPrice(productResponseDto.getPrice());
        product.setQuantity(productResponseDto.getQuantity());
        product.setSeller(user);
        System.out.println(product);
        productRepository.save(product);
        return ProductResponseDto.toDto(product);
    }

    // 아이템 수정
    @Transactional
    public ProductResponseDto updateProduct(long itemId, ProductResponseDto productResponseDto, User loginUser) {

        Product product = productRepository.findById(itemId).orElseThrow(ProductNotFoundException::new);

        if(loginUser.equals(product.getSeller())) {

            // 토큰 보낸 사람과, 게시글 작성자가 같다면 성공!
            product.setProductName(productResponseDto.getProductName());
            product.setPrice(productResponseDto.getPrice());
            product.setQuantity(productResponseDto.getQuantity());
            product.setCategory(productResponseDto.getCategory());
            product.setDeliveryDate(productResponseDto.getDeliveryDate());
            productRepository.save(product);
            return ProductResponseDto.toDto(product);
        } else {
            // 토큰 보낸 사람과 게시글 작성자가 다르다면 실패!
            throw new UserNotEqualsException();
        }
    }


    // 아이템 삭제
    // 아이템 삭제
    @Transactional
    public void deleteProduct(Long itemId, User loginUser) {
        Product product = productRepository.findById(itemId).orElseThrow(ProductNotFoundException::new);
        if(loginUser.equals(product.getSeller())) {
            productRepository.deleteById(itemId);
        } else {
            throw new UserNotEqualsException();
        }
    }
}