package com.example.sb_ecom_v1.service;

import com.example.sb_ecom_v1.payload.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
}
