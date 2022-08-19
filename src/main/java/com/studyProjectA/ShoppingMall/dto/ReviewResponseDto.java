package com.studyProjectA.ShoppingMall.dto;

import com.studyProjectA.ShoppingMall.entity.Review;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/*
Client에게 Review 객체정보를 다 주지않고
원하는 데이터만 줄 수 있게 하는 Dto

user 이름
review 평점
product 판매자 회사이름 or 개인이름
review 작성날짜
product 상품 이름
review 리뷰멘트

이렇게만 페이지에 표시해주면 된다.
 */

@Data
public class ReviewResponseDto {

    @NotBlank
    private String buyerName;

    @NotNull
    private int rate;

    @NotBlank
    private String sellerName;

    @NotBlank
    private LocalDate date;

    @NotBlank
    private String productName;

    @NotBlank
    private String comment;

    // Constructor
    public ReviewResponseDto(String buyerName, int rate, String sellerName, LocalDate date, String productName, String comment) {
        this.buyerName = buyerName;
        this.rate = rate;
        this.sellerName = sellerName;
        this.date = date;
        this.productName = productName;
        this.comment = comment;
    }

    // toDto
    public static ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(
                review.getUserId().getUsername(),
                review.getRate(),
                review.getProductId().getSeller().getUsername(),
                review.getCreateDate(),
                review.getProductId().getProductName(),
                review.getComment()
        );
    }
}