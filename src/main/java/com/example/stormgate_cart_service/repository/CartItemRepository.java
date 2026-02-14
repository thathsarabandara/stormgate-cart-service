package com.example.stormgate_cart_service.repository;

import com.example.stormgate_cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId = :cartId AND ci.productId = :productId AND ci.isDeleted = false")
    Optional<CartItem> findByCartIdAndProductId(@Param("cartId") String cartId, @Param("productId") String productId);

    @Query("UPDATE CartItem ci SET ci.isDeleted = true WHERE ci.cart.cartId = :cartId")
    void softDeleteByCartId(@Param("cartId") String cartId);
}
