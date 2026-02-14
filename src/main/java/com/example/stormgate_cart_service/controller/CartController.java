package com.example.stormgate_cart_service.controller;

import com.example.stormgate_cart_service.dto.AddItemRequest;
import com.example.stormgate_cart_service.dto.CartResponse;
import com.example.stormgate_cart_service.dto.UpdateQuantityRequest;
import com.example.stormgate_cart_service.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Cart Service is running");
    }

    /**
     * View Cart - GET /api/cart?tenantId={tenantId}&userId={userId}
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestParam(required = true) String tenantId,
            @RequestParam(required = true) String userId) {
        CartResponse cart = cartService.getCart(tenantId, userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Add Item to Cart - POST /api/cart/items
     */
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(@Valid @RequestBody AddItemRequest request) {
        CartResponse cart = cartService.addItemToCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    /**
     * Update Item Quantity - PUT /api/cart/items/{productId}
     */
    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @PathVariable String productId,
            @RequestParam(required = true) String tenantId,
            @RequestParam(required = true) String userId,
            @Valid @RequestBody UpdateQuantityRequest request) {
        CartResponse cart = cartService.updateItemQuantity(tenantId, userId, productId, request);
        return ResponseEntity.ok(cart);
    }

    /**
     * Remove Item from Cart - DELETE /api/cart/items/{productId}
     */
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItemFromCart(
            @PathVariable String productId,
            @RequestParam(required = true) String tenantId,
            @RequestParam(required = true) String userId) {
        CartResponse cart = cartService.removeItemFromCart(tenantId, userId, productId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Clear Cart - DELETE /api/cart
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @RequestParam(required = true) String tenantId,
            @RequestParam(required = true) String userId) {
        cartService.clearCart(tenantId, userId);
        return ResponseEntity.noContent().build();
    }
}
