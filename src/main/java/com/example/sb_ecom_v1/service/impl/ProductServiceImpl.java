package com.example.sb_ecom_v1.service.impl;

import com.example.sb_ecom_v1.exceptions.ResourceNotFoundException;
import com.example.sb_ecom_v1.model.Category;
import com.example.sb_ecom_v1.model.Product;
import com.example.sb_ecom_v1.payload.ProductDTO;
import com.example.sb_ecom_v1.payload.ProductResponse;
import com.example.sb_ecom_v1.repository.CategoryRepository;
import com.example.sb_ecom_v1.repository.ProductRepository;
import com.example.sb_ecom_v1.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {

        // fetch the category from db -> throw exception if not found
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice =  product.getPrice() - (product.getDiscount() *0.01) *product.getPrice();
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products= productRepository.findAll();
        
        //Transformation of list of products in product
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {

        // Get the category from the database or throw ResourceNotFoundException
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        // Find all products that belong to that category
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {

        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }
}
