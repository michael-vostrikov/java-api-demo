package com.app.internalapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.app.common.entity.Product;
import com.app.common.entity.ProductStatus;
import com.app.common.entity.Review;
import com.app.common.entity.ReviewStatus;
import com.app.common.repository.ProductRepository;
import com.app.common.repository.ProductChangeRepository;
import com.app.common.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class ReviewService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductChangeRepository productChangeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public Review accept(Review review) {
        if (review.getStatus() != ReviewStatus.SENT) {
            throw new RuntimeException("Review is already processed");
        }

        var product = productRepository.findById(review.getProductId()).orElse(null);

        saveReviewResult(review, ReviewStatus.ACCEPTED);
        acceptProductChanges(product, review);

        return review;
    }

    @Transactional
    public Review decline(Review review) {
        if (review.getStatus() != ReviewStatus.SENT) {
            throw new RuntimeException("Review is already processed");
        }

        var product = productRepository.findById(review.getProductId()).orElse(null);

        saveReviewResult(review, ReviewStatus.DECLINED);
        declineProductChanges(product);

        return review;
    }

    private void saveReviewResult(Review review, ReviewStatus status) {
        review.setStatus(status);
        review.setProcessedAt(Instant.now());
        reviewRepository.save(review);
    }

    private void acceptProductChanges(Product product, Review review) {
        JsonNode fieldValues = review.getFieldValues();

        if (fieldValues.get("categoryId") != null) {
            product.setCategoryId(fieldValues.get("categoryId").get("new").asInt());
        }

        if (fieldValues.get("name") != null) {
            product.setName(fieldValues.get("name").get("new").asText());
        }

        if (fieldValues.get("description") != null) {
            product.setDescription(fieldValues.get("description").get("new").asText());
        }

        product.setStatus(ProductStatus.PUBLISHED);

        productRepository.save(product);
        productChangeRepository.deleteByProductId(product.getId());
    }

    private void declineProductChanges(Product product) {
        product.setStatus(ProductStatus.HIDDEN);

        productRepository.save(product);
        productChangeRepository.deleteByProductId(product.getId());
    }

}
