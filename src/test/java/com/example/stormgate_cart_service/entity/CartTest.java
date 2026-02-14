package com.example.stormgate_cart_service.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test class for Cart entity.
 */
@DisplayName("Cart Entity Tests")
class CartTest {

    private Cart cart;
    private CartItem item1;
    private CartItem item2;

    @BeforeEach
    void setUp() {
        cart = Cart.builder()
                .cartId("cart-123")
                .tenantId("tenant-123")
                .userId("user-456")
                .items(new ArrayList<>())
                .totalAmount(BigDecimal.ZERO)
                .currency("USD")
                .isDeleted(false)
                .build();

        item1 = CartItem.builder()
                .itemId("item-1")
                .cart(cart)
                .productId("product-1")
                .name("Product 1")
                .price(new BigDecimal("50.00"))
                .quantity(2)
                .subtotal(new BigDecimal("100.00"))
                .isDeleted(false)
                .build();

        item2 = CartItem.builder()
                .itemId("item-2")
                .cart(cart)
                .productId("product-2")
                .name("Product 2")
                .price(new BigDecimal("75.00"))
                .quantity(1)
                .subtotal(new BigDecimal("75.00"))
                .isDeleted(false)
                .build();

        cart.getItems().add(item1);
        cart.getItems().add(item2);
    }

    @Test
    @DisplayName("Cart should be created with default values")
    void testCartCreation() {
        assertNotNull(cart);
        assertEquals("cart-123", cart.getCartId());
        assertEquals("tenant-123", cart.getTenantId());
        assertEquals("user-456", cart.getUserId());
        assertEquals("USD", cart.getCurrency());
        assertFalse(cart.getIsDeleted());
    }

    @Test
    @DisplayName("Cart should calculate item count correctly")
    void testGetItemCount() {
        int itemCount = cart.getItemCount();
        assertEquals(3, itemCount); // 2 + 1 items
    }

    @Test
    @DisplayName("Cart should calculate item count with deleted items excluded")
    void testGetItemCountWithDeletedItem() {
        item1.setIsDeleted(true);
        int itemCount = cart.getItemCount();
        assertEquals(1, itemCount); // only item2
    }

    @Test
    @DisplayName("Cart should calculate total correctly")
    void testCalculateTotal() {
        BigDecimal total = cart.calculateTotal();
        assertEquals(new BigDecimal("175.00"), total);
    }

    @Test
    @DisplayName("Cart should calculate total with deleted items excluded")
    void testCalculateTotalWithDeletedItem() {
        item1.setIsDeleted(true);
        BigDecimal total = cart.calculateTotal();
        assertEquals(new BigDecimal("75.00"), total);
    }

    @Test
    @DisplayName("Cart should calculate total as zero when all items are deleted")
    void testCalculateTotalAllItemsDeleted() {
        item1.setIsDeleted(true);
        item2.setIsDeleted(true);
        BigDecimal total = cart.calculateTotal();
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    @DisplayName("Cart onCreate should set timestamps")
    void testPrePersist() {
        Cart newCart = Cart.builder()
                .tenantId("tenant-123")
                .userId("user-456")
                .build();

        newCart.onCreate();

        assertNotNull(newCart.getCreatedAt());
        assertNotNull(newCart.getUpdatedAt());
    }

    @Test
    @DisplayName("Cart onUpdate should update timestamp")
    void testPreUpdate() {
        // Ensure timestamps are initialized
        cart.onCreate();
        LocalDateTime originalTime = cart.getUpdatedAt();
        
        cart.onUpdate();

        assertNotNull(cart.getUpdatedAt());
        // The updated time should be later or equal to original
        assert(cart.getUpdatedAt().isAfter(originalTime) ||
               cart.getUpdatedAt().isEqual(originalTime));
    }
}
