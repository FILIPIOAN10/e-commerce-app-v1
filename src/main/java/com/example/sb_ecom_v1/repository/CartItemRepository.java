package com.example.sb_ecom_v1.repository;

import com.example.sb_ecom_v1.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    CartItem findCartItemByProductProductIdAndCartId(Long cartId, Long productId);

    List<CartItem> findByCartCartId(Long cartId);

}
