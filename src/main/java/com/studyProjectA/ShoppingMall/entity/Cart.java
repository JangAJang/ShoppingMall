package com.studyProjectA.ShoppingMall.entity;
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
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "User_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User buyer;

    @Builder
    public Cart(User buyer){
        this.buyer = buyer;
    }
}
