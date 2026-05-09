package com.example.sb_ecom_v1.controller;


import com.example.sb_ecom_v1.config.AppConstants;
import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.payload.CategoryDTO;
import com.example.sb_ecom_v1.payload.CategoryResponse;
import com.example.sb_ecom_v1.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;




    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize){
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize);
        return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
    }


    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){

        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);

        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }


    @PutMapping("/admin/categories/{categoryId}")

    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {

        CategoryDTO savedCategoryDTO =categoryService.updateCategory(categoryDTO,categoryId);

            // Trebuie să returnăm rezultatul!
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);

    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {

        CategoryDTO deleteCategory = categoryService.deleteCategory(categoryId);
        return  new ResponseEntity<>(deleteCategory,HttpStatus.OK);
    }


}

