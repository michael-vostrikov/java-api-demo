package com.app.userapi.controller;

import com.app.common.entity.AppUser;
import com.app.common.entity.Product;
import com.app.common.entity.Review;
import com.app.common.repository.ProductRepository;
import com.app.common.system.LockService;
import com.app.userapi.service.ProductService;
import com.app.userapi.service.dto.CreateProductDto;
import com.app.userapi.service.dto.SaveProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class ProductController {

    @Autowired
    private LockService lockService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    public final int LOCK_TIMEOUT_MS = 12000;

    @GetMapping("/product/view")
    public ResponseEntity<Product> view(@RequestParam(name = "id") int id, @AuthenticationPrincipal AppUser user) {
        Product product = findEntity(id, user);
        Product productWithChanges = productService.view(product);

        return new ResponseEntity<>(productWithChanges, HttpStatus.OK);
    }

    @PostMapping("/product/create")
    public ResponseEntity<Product> create(@RequestBody CreateProductDto dto, @AuthenticationPrincipal AppUser user) {
        Product product = productService.create(dto, user);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }


    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = Product.class))
        ),
        @ApiResponse(
            responseCode = "400",
            content = @Content(schema = @Schema(
                type = "object",
                additionalProperties = Schema.AdditionalPropertiesValue.TRUE,
                example = "{\"field\": [\"error message 1\", \"error message 2\"]}"
            ))
        )
    })
    @PostMapping("/product/save")
    public ResponseEntity<Object> save(
        @RequestParam(name = "id") int id,
        @Valid @RequestBody SaveProductDto dto,
        @AuthenticationPrincipal AppUser user
    ) {
        return lockService.withLock(Product.class.getName(), id, LOCK_TIMEOUT_MS, () -> {
            Product product = findEntity(id, user);

            var validationResult = productService.isEditAllowed(product);
            if (validationResult.hasErrors()) {
                return new ResponseEntity<>(validationResult.getErrors(), HttpStatus.BAD_REQUEST);
            }

            Product productWithChanges = productService.save(validationResult, dto);

            return new ResponseEntity<>(productWithChanges, HttpStatus.OK);
        });
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = Review.class))
        ),
        @ApiResponse(
            responseCode = "400",
            content = @Content(schema = @Schema(
                type = "object",
                additionalProperties = Schema.AdditionalPropertiesValue.TRUE,
                example = "{\"field\": [\"error message 1\", \"error message 2\"]}"
            ))
        )
    })
    @PostMapping("/product/send-for-review")
    public ResponseEntity<Object> sendForReview(@RequestParam(name = "id") int id, @AuthenticationPrincipal AppUser user) {
        return lockService.withLock(Product.class.getName(), id, LOCK_TIMEOUT_MS, () -> {
            Product product = findEntity(id, user);

            var validationResult = productService.isSendForReviewAllowed(product);
            if (validationResult.hasErrors()) {
                return new ResponseEntity<>(validationResult.getErrors(), HttpStatus.BAD_REQUEST);
            }

            Review review = productService.sendForReview(validationResult, user);

            return new ResponseEntity<>(review, HttpStatus.OK);
        });
    }

    private Product findEntity(int id, AppUser user) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));

        var isAccessAllowed = product.getUserId() == user.getId();
        if (!isAccessAllowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return product;
    }

}
