package com.studyProjectA.ShoppingMall.dto;

import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.Review;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/*
Controller에서 Service로 데이터를 넘겨줄 때
@NotBlank를 통해 데이터의 유효성 검사를 위한 Dto

@NotNull, @NotEmpty, @NotBlank 등은 Advice패키지에서 예외처리해준다.
 */
@Data
public class ReviewRequestDto {

    @NotNull
    private Integer rate;

    @NotBlank
    private String comment;
}