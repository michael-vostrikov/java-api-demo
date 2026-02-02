package com.app.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "category_id", nullable = true)
    private Integer categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Product() {
    }

    public Product(Product other) {
        this.id = other.id;
        this.userId = other.userId;
        this.categoryId = other.categoryId;
        this.name = other.name;
        this.description = other.description;
        this.status = other.status;
        this.createdAt = other.createdAt;
    }
}
