package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.ReviewRequestDto;
import com.studyProjectA.ShoppingMall.dto.ReviewResponseDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.Review;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/*
repository(DB)에서 데이터를 가져오는 역할 담당

- 리뷰 모두 불러오기
- 리뷰 저장하기
- 리뷰 삭제하기
- 리뷰 수정하기
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private ReviewRepository reviewRepository;

    // Read All
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviews() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for(Review review : reviews) {
            reviewResponseDtos.add(ReviewResponseDto.toDto(review));
        }
        return reviewResponseDtos;
    }

    // Create
    @Transactional
    public Review saveReview(ReviewRequestDto reviewRequestDto, UserDto userdto) {
        Review review = new Review();
        User user = new User();
        // 다음부터 모르겠음
        review = ReviewRequestDto.toDto(reviewRequestDto);
        return reviewRepository.save(review);
    }

    // Update
    @Transactional
    public Review updateReview(Integer id, Review review) {
        // 원래 있던 review 객체 불러옴
        Review originalReview = reviewRepository.findByUserId(id);
        // 오리지날리뷰에 새 리뷰객체의 정보 덮어쓰기
        originalReview.setRate(review.getRate());
        originalReview.setComment(review.getComment());
        originalReview.setCreateDate(review.getCreateDate());
        // 저장 후 리턴
        return reviewRepository.save(originalReview);
    }

    // Delete
    @Transactional
    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
        // 리뷰 못찾으면 예외 처리 추가해야함
    }

}