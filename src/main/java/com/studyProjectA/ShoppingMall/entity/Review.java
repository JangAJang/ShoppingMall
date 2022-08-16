package com.studyProjectA.ShoppingMall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {

    // 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 상품 매핑
    @JoinColumn(name = "Product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product productId;

    // 평점
    @Column(nullable = false)
    @Max(100)
    @Min(0)
    private int rate;

    // 리뷰 코멘트
    @Column(nullable = false)
    private String comment;

    // 리뷰생성날짜
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

    @PrePersist
    public void createDate() {
        this.createDate = LocalDate.now();
    }

    // 유저 매핑
    @JoinColumn(name = "User_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User userId;

}