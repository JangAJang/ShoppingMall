package com.studyProjectA.ShoppingMall.dto;

import com.studyProjectA.ShoppingMall.entity.Cart;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    @NotNull
    private int id;

    @NotNull
    private User user;

    @NotNull
    private Product product;

    public static CartDto toDto(Cart cart){
        return new CartDto(
                cart.getId(),
                cart.getUser(),
                cart.getProductId()
        );
    }
}
