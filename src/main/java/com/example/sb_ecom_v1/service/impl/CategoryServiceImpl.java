package com.example.sb_ecom_v1.service.impl;

import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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
                .filter(c->c.getCategoryId().equals(categoryId)).findFirst().orElse(null);
        if(category==null){
            return "Category not found";
        }

        categories.remove(category);
        return "Category with categoryId: " +categoryId + " deleted successfully !!";
    }


}
