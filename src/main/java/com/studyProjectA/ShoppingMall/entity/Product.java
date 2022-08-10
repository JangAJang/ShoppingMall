package com.studyProjectA.ShoppingMall.entity;

import com.studyProjectA.ShoppingMall.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String productName;

    @JoinColumn(name = "User_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User userId;

    @Column(nullable = false)
    private int price;

    @Column
    private int quantity;

    @Column
    private String category;

    @Column
    private long deliveryDate;
}
