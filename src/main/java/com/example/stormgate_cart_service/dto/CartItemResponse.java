package com.example.stormgate_cart_service.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
