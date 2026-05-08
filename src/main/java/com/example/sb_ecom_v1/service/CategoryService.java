package com.example.sb_ecom_v1.service;

import com.example.sb_ecom_v1.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();
    void createCategory(Category category);

    String deleteCategory(Long categoryId);
}
