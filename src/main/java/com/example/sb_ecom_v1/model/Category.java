package com.example.sb_ecom_v1.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity(name = "categories")
public class Category {

    @Id
    private Long categoryId;

    private String categoryName;

    public Category(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Category setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Category setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }
}
