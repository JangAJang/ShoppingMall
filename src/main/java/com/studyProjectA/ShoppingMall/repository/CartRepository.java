package com.studyProjectA.ShoppingMall.repository;

import com.studyProjectA.ShoppingMall.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUser(String username);
}
