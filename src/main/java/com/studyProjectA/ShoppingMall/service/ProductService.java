package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.ProductResponseDto;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public ProductResponseDto getProduct(int id){
        Product product = productRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("해당 품목을 찾을 수 업습니다.");
        });
        return ProductResponseDto.toDto(product);
    }
    /*
    <<<<<<< HEAD
        // 아이템 등록.
    =======
    >>>>>>> main
     */
    // 아이템 등록
    @Transactional
    public ProductResponseDto addProduct(ProductResponseDto productResponseDto, User user){

        Product product = new Product();
        product.setProductName(productResponseDto.getProductName());
        product.setCategory(productResponseDto.getCategory());
        product.setDeliveryDate(productResponseDto.getDeliveryDate());
        product.setPrice(productResponseDto.getPrice());
        product.setQuantity(productResponseDto.getQuantity());
        product.setUser(user);
        productRepository.save(product);
        return ProductResponseDto.toDto(product);
    }

    // 아이템 수정
    @Transactional
    public ProductResponseDto updateProduct(int itemId, ProductResponseDto productResponseDto, User loginUser) {
        Product product = productRepository.findById(itemId).orElseThrow(() -> {
            return new IllegalArgumentException("해당 품목을 찾을 수 없습니다.");
        });
        if(loginUser.equals(product.getUser())) {

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
            throw new IllegalArgumentException("게시글 작성자가 일치하지 않습니다.");
        }
    }

    // 아이템 삭제
    @Transactional
    public void deleteProduct(int itemId, User loginUser) {

        Product product = productRepository.findById(itemId).orElseThrow(() -> {
            return new IllegalArgumentException("해당 품목을 찾을 수 없습니다.");
        });

        if(loginUser.equals(product.getUser())) {
            productRepository.deleteById(itemId);
        } else {
            throw new IllegalArgumentException("게시글 작성자가 일치하지 않습니다.");
        }
    }

}