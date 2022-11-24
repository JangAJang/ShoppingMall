package com.studyProjectA.ShoppingMall.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.studyProjectA.ShoppingMall.dto.ReviewRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Review {

    // 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품 매핑
    @JoinColumn(name = "Product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Product product;

    // 평점
    @Column(nullable = false)
    @Max(100)
    @Min(0)
    private Integer rate;

    // 리뷰 코멘트
    @Column(nullable = false)
    private String comment;

    // 리뷰생성날짜
    @Column(nullable = false)
    @CreatedDate
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
    @JsonIgnore
    private User user;
}