package com.studyProjectA.ShoppingMall.dto;

import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.CartItem;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemDto {

    @NotNull
    private String productName;

    @NotNull
    private Integer priceEach;

    @NotNull
    private Integer quantity;

    public static CartItemDto toDto(CartItem cartItem){
        return new CartItemDto(
                cartItem.getProduct().getProductName(), cartItem.getProduct().getPrice(), cartItem.getQuantity()
        );
    }
}
