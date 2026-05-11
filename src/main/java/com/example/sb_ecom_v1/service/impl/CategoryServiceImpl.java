package com.example.sb_ecom_v1.service.impl;

import com.example.sb_ecom_v1.exceptions.APIException;
import com.example.sb_ecom_v1.exceptions.ResourceNotFoundException;
import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.payload.CategoryDTO;
import com.example.sb_ecom_v1.payload.CategoryResponse;
import com.example.sb_ecom_v1.repository.CategoryRepository;
import com.example.sb_ecom_v1.service.CategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;


    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty())
            throw new APIException("No category created till now");
        List<CategoryDTO> categoryDTOS=
                // converting category to stream
                categories.stream()
                        // for every object in the stream we are mapping category to categoryDTO
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        //I transform the DTO received from the request into a Category object so I can save it to the database
        //Because the repository only knows how to save entities (Category), not DTOs.
        Category category = modelMapper.map(categoryDTO, Category.class);
        //I search the database to see if a category with the same name already exists.
        //So as not to save duplicates.
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDb!=null){
            throw new APIException("Category with this name "+categoryDTO.getCategoryName()+ "already exists !!!");
        }
        //I save the category in the database.
        Category savedCategory = categoryRepository.save(category);
        //Transform entity-ul salvat înapoi într-un DTO pe care îl trimit ca response către client.
        //Because APIs usually return DTOs, not entities directly.
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {

        //Search the database for the category to be deleted.
        //To check if the category exists before deleting.
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        //Delete the category from the database.
        //Because the delete() method removes the entity from the DB.
        categoryRepository.delete(category);
        //I transform the deleted category into a DTO and send it back to the client.
        //So that the client knows which category was deleted.
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {

        //I search the database for the category that needs to be modified.
        //To check if the category with that ID exists.
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        //Transform the DTO received from the request into a Category object.
        //Because the repository only works with entities.
        Category category = modelMapper.map(categoryDTO, Category.class);

        //Set the ID of the category to be updated.
        //Because the DTO in the request may not contain the ID.
        category.setCategoryId(categoryId);

        // Because save() updates if the object already has an ID.
        savedCategory = categoryRepository.save(category);

        //I transform the updated category into a DTO and send it back to the client.
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }


}
