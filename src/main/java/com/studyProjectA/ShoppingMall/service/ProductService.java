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

    @Transactional(readOnly = true)
    public List<ProductDto> getProducts(){
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        products.forEach(s-> productDtos.add(ProductDto.toDto(s)));
        return productDtos;
    }

    @Transactional(readOnly = true)
    public ProductDto getProduct(int id){
        Product product = productRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("해당 품목을 찾을 수 업습니다. ");
        });
        return ProductDto.toDto(product);
    }

    @Transactional
    public ProductDto addProduct(ProductDto productDto, User user){
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setCategory(productDto.getCategory());
        product.setDeliveryDate(productDto.getDeliveryDate());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setUserId(user);
        return ProductDto.toDto(product);
    }
}
