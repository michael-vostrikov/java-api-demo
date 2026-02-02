package com.app.userapi.service;

import com.app.common.entity.Review;
import com.app.common.entity.ReviewStatus;
import com.app.common.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;

@Service
public class ReviewSenderService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    @Scheduled(fixedDelay = 4000)
    public void sendReviews() {
        List<Review> reviewToSend = reviewRepository.findByStatus(ReviewStatus.CREATED);

        for (Review review : reviewToSend) {
            try {
                sendToAnotherSystem(review);
                productService.markReviewAsSent(review);
            } catch (Exception e) {
                // here could be more complicated logic how to process when sending same review failed several times
            }
        }
    }

    private void sendToAnotherSystem(Review review) {
        // send HTTP request
    }
}
