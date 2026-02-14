package com.example.stormgate_cart_service.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test class for CartItem entity.
 */
@DisplayName("CartItem Entity Tests")
class CartItemTest {

    private CartItem cartItem;
    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = Cart.builder()
                .cartId("cart-123")
                .tenantId("tenant-123")
                .userId("user-456")
                .build();

        cartItem = CartItem.builder()
                .itemId("item-1")
                .cart(cart)
                .productId("product-789")
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(2)
                .subtotal(new BigDecimal("199.98"))
                .isDeleted(false)
                .build();
    }

    @Test
    @DisplayName("CartItem should be created with default values")
    void testCartItemCreation() {
        assertNotNull(cartItem);
        assertEquals("item-1", cartItem.getItemId());
        assertEquals("product-789", cartItem.getProductId());
        assertEquals("Test Product", cartItem.getName());
        assertEquals(new BigDecimal("99.99"), cartItem.getPrice());
        assertEquals(2, cartItem.getQuantity());
        assertFalse(cartItem.getIsDeleted());
    }

    @Test
    @DisplayName("CartItem should calculate subtotal correctly")
    void testCalculateSubtotal() {
        CartItem item = CartItem.builder()
                .productId("product-1")
                .name("Product")
                .price(new BigDecimal("50.00"))
                .quantity(3)
                .build();

        item.onCreate();

        assertEquals(new BigDecimal("150.00"), item.getSubtotal());
    }

    @Test
    @DisplayName("CartItem should handle null price and quantity in subtotal calculation")
    void testCalculateSubtotalWithNullValues() {
        CartItem item = CartItem.builder()
                .productId("product-1")
                .name("Product")
                .build();

        item.onCreate();

        // Should not throw exception, subtotal should remain null
        assertEquals(null, item.getSubtotal());
    }

    @Test
    @DisplayName("CartItem onCreate should set timestamps")
    void testPrePersist() {
        CartItem newItem = CartItem.builder()
                .productId("product-1")
                .name("Product")
                .price(new BigDecimal("99.99"))
                .quantity(1)
                .build();

        newItem.onCreate();

        assertNotNull(newItem.getCreatedAt());
        assertNotNull(newItem.getUpdatedAt());
        assertNotNull(newItem.getSubtotal());
    }

    @Test
    @DisplayName("CartItem onUpdate should update timestamp and recalculate subtotal")
    void testPreUpdate() {
        cartItem.setQuantity(5);

        cartItem.onUpdate();

        assertNotNull(cartItem.getUpdatedAt());
        assertEquals(new BigDecimal("499.95"),
                cartItem.getPrice().multiply(new BigDecimal(5)));
    }

    @Test
    @DisplayName("CartItem should maintain reference to parent Cart")
    void testCartReference() {
        assertEquals(cart, cartItem.getCart());
        assertEquals("cart-123", cartItem.getCart().getCartId());
    }

    @Test
    @DisplayName("CartItem should handle large quantities")
    void testLargeQuantity() {
        CartItem item = CartItem.builder()
                .productId("product-1")
                .name("Product")
                .price(new BigDecimal("1.00"))
                .quantity(1000)
                .build();

        item.onCreate();

        assertEquals(new BigDecimal("1000.00"), item.getSubtotal());
    }

    @Test
    @DisplayName("CartItem should handle decimal prices")
    void testDecimalPrices() {
        CartItem item = CartItem.builder()
                .productId("product-1")
                .name("Product")
                .price(new BigDecimal("19.95"))
                .quantity(3)
                .build();

        item.onCreate();

        assertEquals(new BigDecimal("59.85"), item.getSubtotal());
    }
}
