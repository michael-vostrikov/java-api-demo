package com.app.internalapi.controller;

import com.app.common.entity.Review;
import com.app.common.repository.ReviewRepository;
import com.app.common.system.LockService;
import com.app.internalapi.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReviewController {

    @Autowired
    private LockService lockService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    public final int LOCK_TIMEOUT_MS = 2000;

    @PostMapping("/review/accept")
    public ResponseEntity<Review> accept(@RequestParam(name = "id") int id) {
        return lockService.withLock(Review.class.getName(), id, LOCK_TIMEOUT_MS, () -> {
            Review review = findEntity(id);
            review = reviewService.accept(review);

            return new ResponseEntity<>(review, HttpStatus.OK);
        });
    }

    @PostMapping("/review/decline")
    public ResponseEntity<Review> decline(@RequestParam(name = "id") int id) {
        return lockService.withLock(Review.class.getName(), id, LOCK_TIMEOUT_MS, () -> {
            Review review = findEntity(id);
            review = reviewService.decline(review);

            return new ResponseEntity<>(review, HttpStatus.OK);
        });
    }

    private Review findEntity(int id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));

        return review;
    }

}
