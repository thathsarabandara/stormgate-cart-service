package com.example.stormgate_cart_service.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemRequest {

    @NotBlank(message = "tenantId is required")
    private String tenantId;

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "productId is required")
    private String productId;

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    @Max(value = 1000, message = "quantity cannot exceed 1000")
    private Integer quantity;
}
