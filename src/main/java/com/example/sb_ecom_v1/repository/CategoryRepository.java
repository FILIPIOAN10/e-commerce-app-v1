package com.example.sb_ecom_v1.repository;

import com.example.sb_ecom_v1.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
