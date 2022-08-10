package com.studyProjectA.ShoppingMall.repository;

import com.studyProjectA.ShoppingMall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByProductName(String productName);
}
