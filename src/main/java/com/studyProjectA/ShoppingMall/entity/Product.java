package com.studyProjectA.ShoppingMall.entity;

import com.studyProjectA.ShoppingMall.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productName;

    @JoinColumn(name = "User_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private Integer price;

    @Column
    private Integer quantity;

    @Column
    private String category;

    @Column
    private Long deliveryDate;

    public Product makeProduct(ProductDto productDto, User user){
        this.setProductName(productDto.getProductName());
        this.setPrice(productDto.getPrice());
        this.setQuantity(productDto.getQuantity());
        this.setCategory(productDto.getCategory());
        this.setDeliveryDate(productDto.getDeliveryDate());
        this.setUser(user);
        return this;
    }
}
