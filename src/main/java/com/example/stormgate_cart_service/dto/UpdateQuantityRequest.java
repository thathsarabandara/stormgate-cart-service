package com.example.stormgate_cart_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating the quantity of an item in the shopping cart.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuantityRequest {

    /**
     * New quantity for the product in the cart.
     */
    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    @Max(value = 1000, message = "quantity cannot exceed 1000")
    private Integer quantity;
}
