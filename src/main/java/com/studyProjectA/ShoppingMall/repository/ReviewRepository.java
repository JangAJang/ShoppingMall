package com.studyProjectA.ShoppingMall.repository;

import com.studyProjectA.ShoppingMall.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Review findByUserId(int id);
}
