package com.studyProjectA.ShoppingMall.repository;

import com.studyProjectA.ShoppingMall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductName(String productName);
    List<Product> findAllBySeller_Username(String userName);
    List<Product> findAllByProductNameContaining(String searchProductName);
}