package com.studyProjectA.ShoppingMall.controller;

import com.studyProjectA.ShoppingMall.dto.ReviewRequestDto;
import com.studyProjectA.ShoppingMall.entity.Review;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.ReviewNotFoundException;
import com.studyProjectA.ShoppingMall.excpetion.UserNotEqualsException;
import com.studyProjectA.ShoppingMall.excpetion.UserNotFoundException;
import com.studyProjectA.ShoppingMall.repository.ReviewRepository;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
import com.studyProjectA.ShoppingMall.response.Response;
import com.studyProjectA.ShoppingMall.service.ReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/api/products")
public class ReviewController {

    private final ReviewService reviewService;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;


    @ApiOperation(value = "전체 리뷰 게시글 보기", notes = "제품의 전체 리뷰를 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{productId}/reviews/")
    public Response findAll(@PathVariable("productId") Long productId) {
        return success(reviewService.getProductReviews(productId));
    }

    @ApiOperation(value = "리뷰 작성자로 검색",notes = "해당 유저의 제품 리뷰를 검색힙니다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{productId}/reviews/byUser/{username}")
    public Response findReviewByUsername(@PathVariable("productId") Long productId, @PathVariable("username") String username){
        return success(reviewService.getProductReviewsByUsername(productId, username));
    }

    @ApiOperation(value = "리뷰 내용으로 검색", notes = "리뷰에 등록된 내용을 검색합니다. ")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{productId}/reviews/byComment/{comment}")
    public Response findReviewByComment(@PathVariable("productId")Long productId, @PathVariable("comment")String comment){
        return success(reviewService.getProductReviewsByComment(productId, comment));
    }


    @ApiOperation(value = "리뷰 게시글 작성", notes = "리뷰 게시글을 작성합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{productId}/reviews/write")
    public Response saveReview(@RequestBody ReviewRequestDto reviewRequestDto, @PathVariable("productId") Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User writer = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        return success(reviewService.saveReview(reviewRequestDto, writer, productId));
    }

    @ApiOperation(value = "리뷰 게시글 수정", notes = "리뷰 게시글을 수정합니다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{productId}/reviews/{reviewId}/update")
    public Response updateReview( @PathVariable("productId") String productId, @PathVariable("reviewId") Long reviewId, @RequestBody ReviewRequestDto reviewRequestDto) {
        Review oldReview = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        User writer = oldReview.getUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        if(loginUser.equals(writer)){
            return success(reviewService.updateReview(reviewId, reviewRequestDto));
        }
        else{
            throw new UserNotEqualsException();
        }
    }

    @ApiOperation(value = "리뷰 게시글 삭제", notes = "리뷰 게시글을 삭제합니다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/reviews/{reviewId}/delete")
    public Response deleteReview(@PathVariable("reviewId") Long reviewId, @PathVariable("id") String id) {
        Review oldReview = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        User writer = oldReview.getUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = userRepository.findByUsername(authentication.getName()).orElseThrow(UserNotFoundException::new);
        if(loginUser.equals(writer)){
            reviewService.deleteReview(reviewId);
            return success("삭제 완료");
        }
        else{
            throw new UserNotEqualsException();
        }
    }

}