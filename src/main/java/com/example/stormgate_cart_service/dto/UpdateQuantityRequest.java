package com.example.stormgate_cart_service.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuantityRequest {

    @NotBlank(message = "tenantId is required")
    private String tenantId;

    @NotBlank(message = "userId is required")
    private String userId;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    @Max(value = 1000, message = "quantity cannot exceed 1000")
    private Integer quantity;
}
