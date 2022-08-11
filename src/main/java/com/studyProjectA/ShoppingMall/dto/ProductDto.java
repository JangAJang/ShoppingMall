package com.studyProjectA.ShoppingMall.dto;

import com.studyProjectA.ShoppingMall.entity.Product;
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
    private String userName;
    //(질문) 왜 User이야???(우리가 String으로 바꿨어.)

    @NotNull
    private int price;

    @NotNull
    private int quantity;

    @NotNull
    private String category;

    @NotNull
    private long deliveryDate;

    public static ProductDto toDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getProductName(),
                product.getUserId().getUsername(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getDeliveryDate()
        );
    }
}
