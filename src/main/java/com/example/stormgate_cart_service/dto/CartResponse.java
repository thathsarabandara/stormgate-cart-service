package com.example.stormgate_cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Response DTO for cart operations.
 * Contains complete cart information including items and totals.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    /**
     * Unique identifier of the cart.
     */
    private String cartId;

    /**
     * Tenant identifier that owns this cart.
     */
    private String tenantId;

    /**
     * User identifier who owns this cart.
     */
    private String userId;

    /**
     * List of items in the cart.
     */
    private List<CartItemResponse> items;

    /**
     * Total number of items in the cart.
     */
    private Integer itemCount;

    /**
     * Total amount for all items in the cart.
     */
    private BigDecimal totalAmount;

    /**
     * Currency code (e.g., USD, EUR).
     */
    private String currency;

    /**
     * Last update timestamp for the cart.
     */
    private LocalDateTime updatedAt;

    /**
     * Gets a defensive copy of the items list.
     *
     * @return defensive copy of items list
     */
    public List<CartItemResponse> getItems() {
        return items == null ? null : new ArrayList<>(items);
    }

    /**
     * Sets items with defensive copying.
     *
     * @param items the items to set
     */
    public void setItems(final List<CartItemResponse> items) {
        this.items = items == null ? null : new ArrayList<>(items);
    }
}
