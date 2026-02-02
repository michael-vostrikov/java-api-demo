package com.app.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "product_change")
@Data
@NoArgsConstructor
public class ProductChange {
    @Id
    @Column(name = "product_id")
    private int productId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "field_values", columnDefinition = "jsonb")
    private JsonNode fieldValues;

    public ProductChange(int productId) {
        this.productId = productId;
    }

}
