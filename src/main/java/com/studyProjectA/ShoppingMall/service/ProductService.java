package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.ProductDto;
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
    public List<ProductDto> getProducts(){
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        products.forEach(s-> productDtos.add(ProductDto.toDto(s)));
        return productDtos;
    }

    // 단건 물품 조회
    @Transactional(readOnly = true)
    public ProductDto getProduct(int id){
        Product product = productRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("해당 품목을 찾을 수 없습니다. ");
        });
        return ProductDto.toDto(product);
    }

    // 아이템 등록
    @Transactional
    public ProductDto addProduct(ProductDto productDto, User user){
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setCategory(productDto.getCategory());
        product.setDeliveryDate(productDto.getDeliveryDate());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setUserId(user);
        productRepository.save(product); // 이거 추가했어 장희형 ㅎㅎ
        return ProductDto.toDto(product);
    }
    
    // 아이템 수정
    @Transactional
    public ProductDto updateProduct(int id, ProductDto productDto) {
        Product product = productRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 품목을 찾을 수 없습니다.");
        });
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setCategory(productDto.getCategory());
        product.setDeliveryDate(productDto.getDeliveryDate());
        productRepository.save(product);

        return ProductDto.toDto(product);
    }

    // 아이템 삭제
    @Transactional
    public void deleteProduct(int id) {

        Product product = productRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 품목을 찾을 수 없습니다.");
        });

        productRepository.deleteById(id);
    }

}
