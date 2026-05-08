package com.example.sb_ecom_v1.service.impl;

import com.example.sb_ecom_v1.exceptions.APIException;
import com.example.sb_ecom_v1.exceptions.ResourceNotFoundException;
import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.repository.CategoryRepository;
import com.example.sb_ecom_v1.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private CategoryRepository categoryRepository;


    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIException("No category created till now");
         return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory!=null){
            throw new APIException("Category with this name "+category.getCategoryName()+ "already exists !!!");
        }
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
