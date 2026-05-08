package com.example.sb_ecom_v1.controller;


import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;


    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }


    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category){

        categoryService.createCategory(category);
        return new ResponseEntity<>("Category added successfully",HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {

            String status = categoryService.deleteCategory(categoryId);
            return  new ResponseEntity<>(status,HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")

    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId) {

            Category saveCategory =categoryService.updateCategory(category,categoryId);

            // Trebuie să returnăm rezultatul!
            return  new ResponseEntity<>("Category with category id "+categoryId,HttpStatus.OK);

    }

    }

