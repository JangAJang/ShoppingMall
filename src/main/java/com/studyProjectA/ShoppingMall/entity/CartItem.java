package com.studyProjectA.ShoppingMall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {

    @Id
    @JoinColumn(name = "Product_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private int id;

    @JoinColumn(name = "Cart_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cart cart;
}
