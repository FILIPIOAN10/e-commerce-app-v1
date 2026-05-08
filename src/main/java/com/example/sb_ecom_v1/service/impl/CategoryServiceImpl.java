package com.example.sb_ecom_v1.service.impl;

import com.example.sb_ecom_v1.exception.ResourceNotFoundException;
import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.repository.CategoryRepository;
import com.example.sb_ecom_v1.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private CategoryRepository categoryRepository;


    @Override
    public List<Category> getAllCategories() {
         return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        //Verify if category exist in db
        Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() ->new ResourceNotFoundException("Category","categoryId",categoryId));
        // if exist i will delete
        categoryRepository.delete(category);
        return "Category with categoryId: " +categoryId + " deleted successfully !!";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        // 1. verify if exist
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        // 2. update only necessary fields
        existingCategory.setCategoryName(category.getCategoryName());

        // 3. save and return
        return categoryRepository.save(existingCategory);
    }


}
