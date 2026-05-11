package com.example.sb_ecom_v1.service;


import com.example.sb_ecom_v1.model.Product;
import com.example.sb_ecom_v1.payload.ProductDTO;
import com.example.sb_ecom_v1.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchProductByKeyword(String keyword);
}
