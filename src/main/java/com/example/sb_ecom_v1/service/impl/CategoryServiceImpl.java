package com.example.sb_ecom_v1.service.impl;

import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {

    private List<Category> categories= new ArrayList<>();
    private Long nextId = 1L;
    @Override
    public List<Category> getAllCategories() {
         return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        //
        Category category =
                // converted the list into a stream
                categories.stream()
                        // filter the stream for every category checking if the category is equal with category id requested
                .filter(c->c.getCategoryId().equals(categoryId)).findFirst().
                        orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource Not Found"));

        categories.remove(category);
        return "Category with categoryId: " +categoryId + " deleted successfully !!";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Optional<Category> optionalCategory =
                // converted the list into a stream
                categories.stream()
                        // filter the stream for every category checking if the category is equal with category id requested
                        .filter(c->c.getCategoryId().equals(categoryId)).findFirst();
         if(optionalCategory.isPresent()){
             Category existingCategory = optionalCategory.get();
             existingCategory.setCategoryName(category.getCategoryName());
             return existingCategory;
         }else {
             throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource Not Found");
         }
    }


}
