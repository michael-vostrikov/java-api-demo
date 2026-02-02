package com.app.userapi.service;

import com.app.common.entity.Product;
import com.app.common.entity.ProductChange;
import com.app.common.entity.ProductStatus;
import com.app.common.entity.Review;
import com.app.common.entity.ReviewStatus;
import com.app.common.entity.AppUser;
import com.app.userapi.service.dto.CreateProductDto;
import com.app.userapi.service.dto.SaveProductDto;
import com.app.userapi.service.dto.ProductValidationResult;
import com.app.common.repository.ProductRepository;
import com.app.common.repository.ProductChangeRepository;
import com.app.common.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductChangeRepository productChangeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Product create(CreateProductDto dto, AppUser user) {
        Product product = new Product();

        product.setUserId(user.getId());
        product.setStatus(ProductStatus.HIDDEN);

        product.setName(dto.getName());
        product.setCategoryId(null);
        product.setDescription("");

        productRepository.save(product);

        return product;
    }

    public Product view(Product product) {
        ProductChange productChange = productChangeRepository.findById(product.getId()).orElse(null);

        var productWithChanges = applyChanges(product, productChange);

        return productWithChanges;
    }

    private Product applyChanges(Product product, ProductChange productChange) {
        var productWithChanges = new Product(product);

        if (productChange == null) {
            return productWithChanges;
        }

        JsonNode fieldValues = productChange.getFieldValues();

        if (fieldValues.has("categoryId")) {
            var value = fieldValues.get("categoryId");
            productWithChanges.setCategoryId(value.isNull() ? null : value.asInt());
        }

        if (fieldValues.has("name")) {
            productWithChanges.setName(fieldValues.get("name").asText());
        }

        if (fieldValues.has("description")) {
            productWithChanges.setDescription(fieldValues.get("description").asText());
        }

        return productWithChanges;
    }

    public ProductValidationResult isEditAllowed(Product product) {
        var validationResult = new ProductValidationResult(product, null);

        if (product.getStatus() == ProductStatus.ON_REVIEW) {
            validationResult.addError("status", "Product is on review");
        }

        return validationResult;
    }

    public Product save(ProductValidationResult validationResult, SaveProductDto dto) {
        var product = validationResult.getProduct();

        ProductChange productChange = productChangeRepository.findById(product.getId())
            .orElse(new ProductChange(product.getId()));

        ObjectNode fieldValues = objectMapper.createObjectNode();

        if (!Objects.equals(dto.getCategoryId(), product.getCategoryId())) {
            fieldValues.put("categoryId", dto.getCategoryId());
        }

        if (!Objects.equals(dto.getName(), product.getName())) {
            fieldValues.put("name", dto.getName());
        }

        if (!Objects.equals(dto.getDescription(), product.getDescription())) {
            fieldValues.put("description", dto.getDescription());
        }

        if (fieldValues.size() == 0) {
            productChangeRepository.delete(productChange);
            productChange = null;
        } else {
            productChange.setFieldValues((JsonNode) fieldValues);
            productChangeRepository.save(productChange);
        }

        var productWithChanges = applyChanges(product, productChange);

        return productWithChanges;
    }

    public ProductValidationResult isSendForReviewAllowed(Product product) {
        ProductChange productChange = productChangeRepository.findById(product.getId()).orElse(null);

        var validationResult = new ProductValidationResult(product, productChange);

        var productWithChanges = applyChanges(product, productChange);

        if (productChange == null) {
            validationResult.addError("id", "No changes to send");
        } else if (productWithChanges.getStatus() == ProductStatus.ON_REVIEW) {
            validationResult.addError("status", "Product is already on review");
        } else {
            if (productWithChanges.getCategoryId() == null) {
                validationResult.addError("categoryId", "Category is not set");
            }
            if (productWithChanges.getName() == "") {
                validationResult.addError("name", "Name is not set");
            }
            if (productWithChanges.getDescription() == "") {
                validationResult.addError("description", "Description is not set");
            }
            if (productWithChanges.getDescription().length() < 20) {
                validationResult.addError("description", "Description is too small");
            }
        }

        return validationResult;
    }

    @Transactional
    public Review sendForReview(ProductValidationResult validationResult, AppUser user) {
        var product = validationResult.getProduct();
        var productChange = validationResult.getProductChange();

        JsonNode reviewFieldValues = buildReviewFieldValues(product, productChange);

        var review = new Review();
        review.setUserId(user.getId());
        review.setProductId(product.getId());
        review.setFieldValues(reviewFieldValues);
        review.setStatus(ReviewStatus.CREATED);
        review.setProcessedAt(null);

        product.setStatus(ProductStatus.ON_REVIEW);

        reviewRepository.save(review);
        productRepository.save(product);

        return review;
    }

    private JsonNode buildReviewFieldValues(Product product, ProductChange productChange)
    {
        ObjectNode reviewFieldValues = objectMapper.createObjectNode();

        JsonNode newFieldValues = productChange.getFieldValues();

        if (newFieldValues.has("categoryId")) {
            reviewFieldValues.set("categoryId", objectMapper.valueToTree(Map.of(
                "new", newFieldValues.get("categoryId"),
                "old", product.getCategoryId()
            )));
        }

        if (newFieldValues.has("name")) {
            reviewFieldValues.set("name", objectMapper.valueToTree(Map.of(
                "new", newFieldValues.get("name"),
                "old", product.getName()
            )));
        }

        if (newFieldValues.has("description")) {
            reviewFieldValues.set("description", objectMapper.valueToTree(Map.of(
                "new", newFieldValues.get("description"),
                "old", product.getDescription()
            )));
        }

        return reviewFieldValues;
    }

    @Transactional
    public void markReviewAsSent(Review review) {
        review.setStatus(ReviewStatus.SENT);
        reviewRepository.save(review);
    }

}
