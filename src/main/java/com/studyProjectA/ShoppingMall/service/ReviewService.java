package com.studyProjectA.ShoppingMall.service;

import com.studyProjectA.ShoppingMall.dto.ReviewRequestDto;
import com.studyProjectA.ShoppingMall.dto.ReviewResponseDto;
import com.studyProjectA.ShoppingMall.entity.Product;
import com.studyProjectA.ShoppingMall.entity.Review;
import com.studyProjectA.ShoppingMall.entity.User;
import com.studyProjectA.ShoppingMall.excpetion.ProductNotFoundException;
import com.studyProjectA.ShoppingMall.excpetion.ReviewNotFoundException;
import com.studyProjectA.ShoppingMall.excpetion.UserNotEqualsException;
import com.studyProjectA.ShoppingMall.repository.ProductRepository;
import com.studyProjectA.ShoppingMall.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getProductReviews(Long productId){
        return changeEntityToDto(getAllReviewsByProduct(productId));
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> searchProductReviewsByUsername(Long productId, String username){
        List<Review> review = getAllReviewsByProduct(productId);
        filterByUsername(review, username);
        return changeEntityToDto(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> searchProductReviewsByComment(Long productId, String comment){
        List<Review> review = getAllReviewsByProduct(productId);
        filterByComment(review, comment);
        return changeEntityToDto(review);
    }

    @Transactional(readOnly = true)
    public Review getReview(Long id){
        return reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
    }

    // Create
    @Transactional
    public ReviewResponseDto saveReview(User writer, ReviewRequestDto reviewRequestDto, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        Review review = new Review();
        setDtoToEntity(reviewRequestDto, review);
        review.setUser(writer);
        review.setProduct(product);
        reviewRepository.save(review);
        return ReviewResponseDto.toDto(review);
    }

    // Update
    @Transactional
    public ReviewResponseDto updateReview(User user, Long id, ReviewRequestDto reviewRequestDto) {
        Review review = getReview(id);
        validateUserAuthority(user, review);
        setDtoToEntity(reviewRequestDto, review);
        review.setUser(review.getUser());
        review.setProduct(review.getProduct());
        reviewRepository.save(review);
        return ReviewResponseDto.toDto(review);
    }

    // Delete
    @Transactional
    public String deleteReview(User user, Long id) {
        Review review = getReview(id);
        validateUserAuthority(user, review);
        reviewRepository.delete(review);
        return "삭제 완료";
    }

    public List<Review> getAllReviewsByProduct(Long id){
        List<Review> list =  reviewRepository.findAllByProduct_Id(id).orElseThrow(ReviewNotFoundException::new);
        if(list.isEmpty()) throw new ReviewNotFoundException();
        return list;
    }

    public List<ReviewResponseDto> changeEntityToDto(List<Review> reviews){
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for(Review review : reviews){
            reviewResponseDtos.add(ReviewResponseDto.toDto(review));
        }
        return reviewResponseDtos;
    }

    public void validateUserAuthority(User user, Review review){
        if(!user.equals(review.getUser())) throw new UserNotEqualsException();
    }

    public void filterByUsername(List<Review> reviews, String username){
        reviews.removeIf(review -> !review.getUser().getUsername().contains(username));
    }

    public void filterByComment(List<Review> reviews, String content){
        reviews.removeIf(review -> !review.getComment().contains(content));
    }

    public void setDtoToEntity(ReviewRequestDto reviewRequestDto, Review review){
        review.setComment(reviewRequestDto.getComment());
        review.setRate(reviewRequestDto.getRate());
        review.setCreateDate(review.getCreateDate());
    }

}