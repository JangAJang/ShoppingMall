package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.ReviewRequestDto;
import com.studyProjectA.ShoppingMall.dto.ReviewResponseDto;
import com.studyProjectA.ShoppingMall.dto.UserDto;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.Review;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.ProductNotFoundException;
import com.studyProjectA.ShoppingMall.excpetion.ReviewNotFoundException;
import com.studyProjectA.ShoppingMall.excpetion.UserNotEqualsException;
import com.studyProjectA.ShoppingMall.repository.ProductRepository;
import com.studyProjectA.ShoppingMall.repository.ReviewRepository;
import com.studyProjectA.ShoppingMall.repository.UserRepository;
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

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

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

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getProductReviews(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for(Review review : reviews) {
            if(review.getProduct().equals(product)) {
                reviewResponseDtos.add(ReviewResponseDto.toDto(review));
            }
        }
        return reviewResponseDtos;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getProductReviewsByUsername(Long productId, String username){
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        User user = userRepository.findByUsername(username).orElseThrow(UserNotEqualsException::new);
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for(Review review : reviews){
            if(review.getProduct().getSeller().getUsername().equals(product.getSeller().getUsername()) && review.getUser().getUsername().equals(user.getUsername())){
                reviewResponseDtos.add(ReviewResponseDto.toDto(review));
            }
        }
        return reviewResponseDtos;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getProductReviewsByComment(Long productId, String comment){
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for(Review review : reviews){
            if(review.getProduct().equals(product) && review.getComment().contains(comment)){
                reviewResponseDtos.add(ReviewResponseDto.toDto(review));
            }
        }
        return reviewResponseDtos;
    }

    @Transactional(readOnly = true)
    public Review getReview(Long id){
        return reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
    }

    // Create
    @Transactional
    public List<ReviewResponseDto> saveReview(ReviewRequestDto reviewRequestDto,User writer, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        Review review = Review.builder()
                .comment(reviewRequestDto.getComment())
                .product(product)
                .rate(reviewRequestDto.getRate())
                .user(writer)
                .build();
        reviewRepository.save(review);
        List<Review> reviews = reviewRepository.findAllByProduct_Id(product.getId()).orElseThrow(ReviewNotFoundException::new);
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for(Review review1 : reviews){
            reviewResponseDtos.add(ReviewResponseDto.toDto(review1));
        }
        return reviewResponseDtos;
    }

    // Update
    @Transactional
    public ReviewResponseDto updateReview(Long id, ReviewRequestDto reviewRequestDto) {
        // 원래 있던 review 객체 불러옴
        Review originalReview = reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        // 오리지날리뷰에 새 리뷰객체의 정보 덮어쓰기
        originalReview.setRate(reviewRequestDto.getRate());
        originalReview.setComment(reviewRequestDto.getComment());
        originalReview.createDate();
        // 저장 후 리턴
        reviewRepository.save(originalReview);
        return ReviewResponseDto.toDto(originalReview);
    }

    // Delete
    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        //리뷰 못 찾으면 예외처리
        reviewRepository.deleteById(id);
    }

}