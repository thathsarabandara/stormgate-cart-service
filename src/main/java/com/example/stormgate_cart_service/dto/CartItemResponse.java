package com.example.stormgate_cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Response DTO for cart items.
 * Contains product and pricing information for items in cart.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    /**
     * Unique identifier of the product.
     */
    private String productId;

    /**
     * Display name of the product.
     */
    private String name;

    /**
     * Unit price of the product.
     */
    private BigDecimal price;

    /**
     * Quantity of the product in the cart.
     */
    private Integer quantity;

    /**
     * Subtotal for this item (price * quantity).
     */
    private BigDecimal subtotal;
}
