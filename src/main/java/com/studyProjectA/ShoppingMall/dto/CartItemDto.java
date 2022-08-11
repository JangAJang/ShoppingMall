package com.studyProjectA.ShoppingMall.dto;

import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.CartItem;
import com.studyProjectA.ShoppingMall.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    @NotNull
    private int id;

    @NotNull
    private Cart cart;

    public CartItemDto toDto(CartItem cartItem){
        return new CartItemDto(
                cartItem.getId(), cartItem.getCart()
        );
    }
}
