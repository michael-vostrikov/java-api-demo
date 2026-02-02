package com.app.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.common.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
