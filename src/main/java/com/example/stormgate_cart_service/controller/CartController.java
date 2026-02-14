package com.example.stormgate_cart_service.controller;

import com.example.stormgate_cart_service.dto.AddItemRequest;
import com.example.stormgate_cart_service.dto.CartResponse;
import com.example.stormgate_cart_service.dto.UpdateQuantityRequest;
import com.example.stormgate_cart_service.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Cart management operations.
 * Handles all cart-related HTTP requests in the Stormgate e-commerce platform.
 * Requires X-Tenant-ID and X-User-ID headers for tenant-aware operations.
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Health check endpoint to verify service availability.
     *
     * @return response entity with success message
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Cart Service is running");
    }

    /**
     * Retrieves the shopping cart for a specific tenant and user.
     *
     * @param tenantId the tenant identifier from header
     * @param userId the user identifier from header
     * @return response entity containing the cart details
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader(value = "X-Tenant-ID", required = true) final String tenantId,
            @RequestHeader(value = "X-User-ID", required = true) final String userId) {
        final CartResponse cart = cartService.getCart(tenantId, userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Adds an item to the shopping cart.
     *
     * @param tenantId the tenant identifier from header
     * @param userId the user identifier from header
     * @param request the add item request containing product details
     * @return response entity with updated cart
     */
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(
            @RequestHeader(value = "X-Tenant-ID", required = true) final String tenantId,
            @RequestHeader(value = "X-User-ID", required = true) final String userId,
            @Valid @RequestBody final AddItemRequest request) {
        final CartResponse cart = cartService.addItemToCart(tenantId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    /**
     * Updates the quantity of an item in the shopping cart.
     *
     * @param productId the product identifier to update
     * @param tenantId the tenant identifier from header
     * @param userId the user identifier from header
     * @param request the update quantity request
     * @return response entity with updated cart
     */
    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @RequestHeader(value = "X-Tenant-ID", required = true) final String tenantId,
            @RequestHeader(value = "X-User-ID", required = true) final String userId,
            @PathVariable final String productId,
            @Valid @RequestBody final UpdateQuantityRequest request) {
        final CartResponse cart = cartService.updateItemQuantity(tenantId, userId, productId, request);
        return ResponseEntity.ok(cart);
    }

    /**
     * Removes an item from the shopping cart.
     *
     * @param productId the product identifier to remove
     * @param tenantId the tenant identifier from header
     * @param userId the user identifier from header
     * @return response entity with updated cart
     */
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItemFromCart(
            @RequestHeader(value = "X-Tenant-ID", required = true) final String tenantId,
            @RequestHeader(value = "X-User-ID", required = true) final String userId,
            @PathVariable final String productId) {
        final CartResponse cart = cartService.removeItemFromCart(tenantId, userId, productId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Clears all items from the shopping cart.
     *
     * @param tenantId the tenant identifier from header
     * @param userId the user identifier from header
     * @return response entity with no content
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @RequestHeader(value = "X-Tenant-ID", required = true) final String tenantId,
            @RequestHeader(value = "X-User-ID", required = true) final String userId) {
        cartService.clearCart(tenantId, userId);
        return ResponseEntity.noContent().build();
    }
}
