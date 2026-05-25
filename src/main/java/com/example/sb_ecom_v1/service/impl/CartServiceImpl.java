package com.example.sb_ecom_v1.service.impl;

import com.example.sb_ecom_v1.exceptions.APIException;
import com.example.sb_ecom_v1.exceptions.ResourceNotFoundException;
import com.example.sb_ecom_v1.model.Cart;
import com.example.sb_ecom_v1.model.CartItem;
import com.example.sb_ecom_v1.model.Product;
import com.example.sb_ecom_v1.payload.CartDTO;
import com.example.sb_ecom_v1.payload.ProductDTO;
import com.example.sb_ecom_v1.repository.CartItemRepository;
import com.example.sb_ecom_v1.repository.CartRepository;
import com.example.sb_ecom_v1.repository.ProductRepository;
import com.example.sb_ecom_v1.service.CartService;
import com.example.sb_ecom_v1.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final AuthUtil authUtil;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //  Find existing cart or create one
        Cart cart = creatCart();
        // Retrieve Product Details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        // Perform Validations

        CartItem cartItem = cartItemRepository.findCartItemByProductProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIException("Product" + product.getProductName() + " already exists in the cart ");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName() + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        // Create Cart Item
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        // Save Cart Item
        cartItemRepository.save(newCartItem);
//        // update the product quantity in db
        product.setQuantity(product.getQuantity());
        List<CartItem> currentCartItems = cartItemRepository.findByCartCartId(cart.getCartId());

        double newTotalPrice = currentCartItems.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(newTotalPrice);
        cartRepository.save(cart);

        // Return updated cart

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);


        Stream<ProductDTO> productStream = currentCartItems.stream()
                .map(item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    // updating the quantity from card Item
                    productDTO.setQuantity(item.getQuantity());
                    return productDTO;
                });


        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }


    private Cart creatCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }
}
