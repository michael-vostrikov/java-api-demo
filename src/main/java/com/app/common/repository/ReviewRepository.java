package com.app.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.common.entity.Review;
import com.app.common.entity.ReviewStatus;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByStatus(ReviewStatus status);
}
