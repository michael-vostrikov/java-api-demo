package com.app.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "review")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReviewStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "processed_at", nullable = true)
    private Instant processedAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "field_values", columnDefinition = "jsonb")
    private JsonNode fieldValues;
}
