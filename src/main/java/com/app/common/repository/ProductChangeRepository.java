package com.app.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.common.entity.ProductChange;

@Repository
public interface ProductChangeRepository extends JpaRepository<ProductChange, Integer> {
    Long deleteByProductId(Integer productId);
}
