package com.studyProjectA.ShoppingMall.entity;

import com.studyProjectA.ShoppingMall.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productName;

    @JoinColumn(name = "seller_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User seller;

    @Column(nullable = false)
    private int price;

    @Column
    private int quantity;

    @Column
    private String category;

    @Column
    private long deliveryDate;
}
