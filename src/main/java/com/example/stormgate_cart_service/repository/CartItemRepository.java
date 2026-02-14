package com.example.stormgate_cart_service.repository;

import com.example.stormgate_cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for CartItem entity.
 * Provides database access methods for cart item operations.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    /**
     * Finds a cart item by cart ID and product ID.
     *
     * @param cartId the cart identifier
     * @param productId the product identifier
     * @return optional containing the cart item if found
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.cartId = :cartId "
            + "AND ci.productId = :productId AND ci.isDeleted = false")
    Optional<CartItem> findByCartIdAndProductId(
            @Param("cartId") String cartId,
            @Param("productId") String productId);

    /**
     * Soft deletes all items in a cart.
     *
     * @param cartId the cart identifier
     */
    @Modifying
    @Query("UPDATE CartItem ci SET ci.isDeleted = true "
            + "WHERE ci.cart.cartId = :cartId")
    void softDeleteByCartId(@Param("cartId") String cartId);
}
