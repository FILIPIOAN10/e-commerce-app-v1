package com.example.sb_ecom_v1.repository;

import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);
}
