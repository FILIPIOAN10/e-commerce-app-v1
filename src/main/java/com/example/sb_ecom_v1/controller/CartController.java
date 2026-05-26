package com.example.sb_ecom_v1.controller;

import com.example.sb_ecom_v1.model.Cart;
import com.example.sb_ecom_v1.payload.CartDTO;
import com.example.sb_ecom_v1.repository.CartRepository;
import com.example.sb_ecom_v1.service.impl.CartServiceImpl;
import com.example.sb_ecom_v1.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {


    private final CartServiceImpl cartService;
    private final AuthUtil authUtil;
    private final CartRepository cartRepository;



    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);

    }
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS,HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(){
        // get the email from user section
        String emailId = authUtil.loggedInEmail();
        // getting the cart of the user from db
        Cart cart = cartRepository.findCartByEmail(emailId);
        // getting the cartId from the cart

        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(emailId,cartId);

        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable String operation){
        CartDTO cartDTO =  cartService.updateProductQuantityInCart(productId,operation.equalsIgnoreCase("delete") ? -1 : 1 );
        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(Long cartId,Long productId){
        String status = cartService.deleteProductFromCart(cartId,productId);

        return new ResponseEntity<>(status,HttpStatus.OK);
    }
}
