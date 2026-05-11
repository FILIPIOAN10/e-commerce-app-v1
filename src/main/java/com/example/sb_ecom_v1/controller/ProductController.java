package com.example.sb_ecom_v1.controller;


import com.example.sb_ecom_v1.model.Product;
import com.example.sb_ecom_v1.payload.ProductDTO;
import com.example.sb_ecom_v1.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    // add a product to category
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product,
                                                 @PathVariable Long categoryId){
       ProductDTO productDTO = productService.addProduct(categoryId,product);
       return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }
}
