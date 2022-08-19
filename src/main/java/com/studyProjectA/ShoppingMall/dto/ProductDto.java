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

    @NotNull(message = "제품명을 입력해주세요")
    private String productName;

    @NotNull
    private User userId;

    @NotNull(message = "가격을 입력해주세요")
    private int price;

    @NotNull(message = "수량을 입력해주세요")
    private int quantity;

    @NotNull(message = "카테고리를 선택해주세요")
    private String category;

    private long deliveryDate;

    public static ProductDto toDto(Product product){
        return new ProductDto(
                product.getProductName(),
                product.getSeller(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getDeliveryDate()
        );
    }
}
