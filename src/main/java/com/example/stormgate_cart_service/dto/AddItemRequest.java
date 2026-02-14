package com.example.stormgate_cart_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Request DTO for adding an item to the shopping cart.
 * Contains product details and quantity information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemRequest {

    /**
     * Unique identifier of the product to add.
     */
    @NotBlank(message = "productId is required")
    private String productId;

    /**
     * Display name of the product.
     */
    @NotBlank(message = "name is required")
    private String name;

    /**
     * Price of the product per unit.
     */
    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "price must be greater than 0")
    private BigDecimal price;

    /**
     * Quantity of the product to add to cart.
     */
    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    @Max(value = 1000, message = "quantity cannot exceed 1000")
    private Integer quantity;
}
