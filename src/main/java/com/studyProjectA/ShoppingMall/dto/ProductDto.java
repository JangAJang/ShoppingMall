package com.studyProjectA.ShoppingMall.dto;

import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotNull
    private int id;

    @NotNull
    private String productName;

    @NotNull
    private User userId;

    @NotNull
    private int price;

    @NotNull
    private int quantity;

    @NotNull
    private String category;

    private long deliveryDate;

    public static ProductDto toDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getProductName(),
                product.getUserId(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getDeliveryDate()
        );
    }
}
