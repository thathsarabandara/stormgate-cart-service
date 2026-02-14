package com.example.stormgate_cart_service.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private String cartId;
    private String tenantId;
    private String userId;
    private List<CartItemResponse> items;
    private Integer itemCount;
    private BigDecimal totalAmount;
    private String currency;
    private LocalDateTime updatedAt;
}
