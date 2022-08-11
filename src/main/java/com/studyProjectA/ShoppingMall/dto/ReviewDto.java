package com.studyProjectA.ShoppingMall.dto;

import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.Review;
import com.studyProjectA.ShoppingMall.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    @NotNull
    private int id;

    @NotNull
    private Product productId;

    @NotNull(message = "점수를 등록해주세요")
    private int rate;

    @NotNull(message = "리뷰 내용을 입력해주세요")
    private String comment;

    @NotNull
    private Date date;

    @NotNull
    private User userId;

    public static ReviewDto toDto(Review review){
        return new ReviewDto(
                review.getId(),
                review.getProductId(),
                review.getRate(),
                review.getComment(),
                review.getDate(),
                review.getUserId()
        );
    }
}
