package com.studyProjectA.ShoppingMall.repository;

import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart(Cart myCart);
}
