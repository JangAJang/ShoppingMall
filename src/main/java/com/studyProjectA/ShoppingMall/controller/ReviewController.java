package com.studyProjectA.ShoppingMall.controller;

import com.studyProjectA.ShoppingMall.dto.ReviewRequestDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.Review;
import com.studyProjectA.ShoppingMall.response.Response;
import com.studyProjectA.ShoppingMall.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.studyProjectA.ShoppingMall.response.Response.*;

/*
Client와 가장 맞붙어있으며, 데이터를 받아오고 JSON파일로 넘겨주는 역할 담당

Controller의 역할
- 리뷰 모두 불러오기
- 리뷰 저장하기
- 리뷰 삭제하기
- 리뷰 수정하기
 */

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private ReviewService reviewService;

    @ApiOperation(value = "전체 리뷰 게시글 보기", notes = "전체 리뷰 게시글을 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/reviews")
    public Response findAll() {
        return success(reviewService.getReviews());
    }

    @ApiOperation(value = "리뷰 게시글 작성", notes = "리뷰 게시글을 작성합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reviews/write")
    public Response saveReview(@RequestBody ReviewRequestDto reviewRequestDto, @RequestBody UserDto userdto) {
        return success(reviewService.saveReview(reviewRequestDto, userdto));
    }

    @ApiOperation(value = "리뷰 게시글 수정", notes = "리뷰 게시글을 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/reviews/{id}")
    public Response updateReview(@PathVariable("id") Integer id, Review review) {
        return success(reviewService.updateReview(id, review));
    }

    @ApiOperation(value = "리뷰 게시글 삭제", notes = "리뷰 게시글을 삭제합니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/review/{id}")
    public Response deleteReview(@PathVariable("id") Integer id) {
        reviewService.deleteReview(id);
        return success("성공적으로 지웠습니다.");
    }

}
