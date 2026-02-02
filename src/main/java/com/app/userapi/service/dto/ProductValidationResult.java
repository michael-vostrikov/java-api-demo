package com.app.userapi.service.dto;

import com.app.common.entity.Product;
import com.app.common.entity.ProductChange;
import lombok.Getter;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Getter
public class ProductValidationResult {
    // field => errors
    private Map<String, List<String>> errors;

    private Product product;
    private ProductChange productChange;

    public ProductValidationResult(Product product, ProductChange productChange) {
        this.product = product;
        this.productChange = productChange;
        this.errors = new HashMap<String, List<String>>();
    }

    public void addError(String field, String error) {
        product = null;
        productChange = null;

        if (!errors.containsKey(field)) {
            errors.put(field, new ArrayList<String>());
        }

        errors.get(field).add(error);
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }
}
