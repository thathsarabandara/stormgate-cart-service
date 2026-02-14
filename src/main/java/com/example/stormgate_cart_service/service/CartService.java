package com.example.stormgate_cart_service.service;

import com.example.stormgate_cart_service.dto.AddItemRequest;
import com.example.stormgate_cart_service.dto.CartItemResponse;
import com.example.stormgate_cart_service.dto.CartResponse;
import com.example.stormgate_cart_service.dto.UpdateQuantityRequest;
import com.example.stormgate_cart_service.entity.Cart;
import com.example.stormgate_cart_service.entity.CartItem;
import com.example.stormgate_cart_service.exception.CartNotFoundException;
import com.example.stormgate_cart_service.exception.ItemNotFoundException;
import com.example.stormgate_cart_service.repository.CartItemRepository;
import com.example.stormgate_cart_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * View cart for tenant and user
     */
    public CartResponse getCart(String tenantId, String userId) {
        Cart cart = cartRepository.findByTenantIdAndUserId(tenantId, userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for tenant: " + tenantId + " and user: " + userId));

        return mapToCartResponse(cart);
    }

    /**
     * Add item to cart
     */
    public CartResponse addItemToCart(AddItemRequest request) {
        String tenantId = request.getTenantId();
        String userId = request.getUserId();

        // Check if cart exists, if not create one
        Cart cart = cartRepository.findByTenantIdAndUserId(tenantId, userId)
                .orElseGet(() -> createNewCart(tenantId, userId));

        // Check if product already exists in cart
        CartItem existingItem = cartItemRepository
                .findByCartIdAndProductId(cart.getCartId(), request.getProductId())
                .orElse(null);

        if (existingItem != null && !existingItem.getIsDeleted()) {
            // Update quantity if item exists
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else if (existingItem != null && existingItem.getIsDeleted()) {
            // Restore deleted item and update quantity
            existingItem.setIsDeleted(false);
            existingItem.setQuantity(request.getQuantity());
            existingItem.setPrice(request.getPrice());
            existingItem.setName(request.getName());
            cartItemRepository.save(existingItem);
        } else {
            // Create new item
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productId(request.getProductId())
                    .name(request.getName())
                    .price(request.getPrice())
                    .quantity(request.getQuantity())
                    .subtotal(request.getPrice().multiply(new BigDecimal(request.getQuantity())))
                    .isDeleted(false)
                    .build();
            cart.getItems().add(newItem);
        }

        // Update cart totals
        updateCartTotals(cart);
        cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    /**
     * Update item quantity in cart
     */
    public CartResponse updateItemQuantity(String tenantId, String userId, String productId, UpdateQuantityRequest request) {
        Cart cart = cartRepository.findByTenantIdAndUserId(tenantId, userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for tenant: " + tenantId + " and user: " + userId));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found in cart with productId: " + productId));

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        // Update cart totals
        updateCartTotals(cart);
        cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    /**
     * Remove item from cart (soft delete)
     */
    public CartResponse removeItemFromCart(String tenantId, String userId, String productId) {
        Cart cart = cartRepository.findByTenantIdAndUserId(tenantId, userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for tenant: " + tenantId + " and user: " + userId));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found in cart with productId: " + productId));

        item.setIsDeleted(true);
        cartItemRepository.save(item);

        // Update cart totals
        updateCartTotals(cart);
        cartRepository.save(cart);

        return mapToCartResponse(cart);
    }

    /**
     * Clear cart (soft delete all items)
     */
    public void clearCart(String tenantId, String userId) {
        Cart cart = cartRepository.findByTenantIdAndUserId(tenantId, userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for tenant: " + tenantId + " and user: " + userId));

        cart.getItems().forEach(item -> item.setIsDeleted(true));
        updateCartTotals(cart);
        cartRepository.save(cart);
    }

    /**
     * Helper method to create a new cart
     */
    private Cart createNewCart(String tenantId, String userId) {
        Cart cart = Cart.builder()
                .tenantId(tenantId)
                .userId(userId)
                .currency("USD")
                .totalAmount(BigDecimal.ZERO)
                .isDeleted(false)
                .build();
        return cartRepository.save(cart);
    }

    /**
     * Update cart totals
     */
    private void updateCartTotals(Cart cart) {
        BigDecimal total = cart.calculateTotal();
        cart.setTotalAmount(total);
    }

    /**
     * Map Cart entity to CartResponse DTO
     */
    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .filter(item -> !item.getIsDeleted())
                .map(item -> CartItemResponse.builder()
                        .productId(item.getProductId())
                        .name(item.getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .tenantId(cart.getTenantId())
                .userId(cart.getUserId())
                .items(items)
                .itemCount(cart.getItemCount())
                .totalAmount(cart.getTotalAmount())
                .currency(cart.getCurrency())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}
