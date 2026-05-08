package com.example.sb_ecom_v1.controller;


import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }


    @PostMapping("/categories")
    public String createCategory(@RequestBody Category category){

        categoryService.createCategory(category);
        return "Category added ";
    }

    @DeleteMapping("/categories/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId){
        String status = categoryService.deleteCategory(categoryId);
        return status;
    }
}
