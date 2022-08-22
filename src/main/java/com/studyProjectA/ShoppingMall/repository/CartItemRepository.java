package com.studyProjectA.ShoppingMall.repository;

import com.studyProjectA.ShoppingMall.dto.CartItemDto;
import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.CartItem;
import com.studyProjectA.ShoppingMall.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<List<CartItem>> findAllByCart(Cart cart);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
