package com.example.stormgate_cart_service.controller;

import com.example.stormgate_cart_service.dto.AddItemRequest;
import com.example.stormgate_cart_service.dto.CartItemResponse;
import com.example.stormgate_cart_service.dto.CartResponse;
import com.example.stormgate_cart_service.dto.UpdateQuantityRequest;
import com.example.stormgate_cart_service.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for CartController.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CartController Tests")
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private CartResponse cartResponse;
    private AddItemRequest addItemRequest;
    private UpdateQuantityRequest updateQuantityRequest;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";
    private static final String PRODUCT_ID = "product-789";

    @BeforeEach
    void setUp() {
        CartItemResponse item = CartItemResponse.builder()
                .productId(PRODUCT_ID)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(2)
                .subtotal(new BigDecimal("199.98"))
                .build();

        cartResponse = CartResponse.builder()
                .cartId("cart-123")
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .items(Arrays.asList(item))
                .itemCount(2)
                .totalAmount(new BigDecimal("199.98"))
                .currency("USD")
                .updatedAt(LocalDateTime.now())
                .build();

        addItemRequest = AddItemRequest.builder()
                .productId(PRODUCT_ID)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(1)
                .build();

        updateQuantityRequest = UpdateQuantityRequest.builder()
                .quantity(5)
                .build();
    }

    @Test
    @DisplayName("Get cart should return cart details")
    void testGetCart() {
        when(cartService.getCart(TENANT_ID, USER_ID)).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.getCart(TENANT_ID, USER_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TENANT_ID, response.getBody().getTenantId());
        verify(cartService).getCart(TENANT_ID, USER_ID);
    }

    @Test
    @DisplayName("Add item to cart should return created status")
    void testAddItemToCart() {
        when(cartService.addItemToCart(TENANT_ID, USER_ID, addItemRequest))
                .thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.addItemToCart(
                TENANT_ID, USER_ID, addItemRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cartService).addItemToCart(TENANT_ID, USER_ID, addItemRequest);
    }

    @Test
    @DisplayName("Update item quantity should return 200 OK")
    void testUpdateItemQuantity() {
        when(cartService.updateItemQuantity(TENANT_ID, USER_ID,
                PRODUCT_ID, updateQuantityRequest))
                .thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.updateItemQuantity(
                TENANT_ID, USER_ID, PRODUCT_ID, updateQuantityRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService).updateItemQuantity(TENANT_ID, USER_ID,
                PRODUCT_ID, updateQuantityRequest);
    }

    @Test
    @DisplayName("Remove item from cart should return 200 OK")
    void testRemoveItemFromCart() {
        when(cartService.removeItemFromCart(TENANT_ID, USER_ID, PRODUCT_ID))
                .thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.removeItemFromCart(
                TENANT_ID, USER_ID, PRODUCT_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService).removeItemFromCart(TENANT_ID, USER_ID, PRODUCT_ID);
    }

    @Test
    @DisplayName("Clear cart should return 204 No Content")
    void testClearCart() {
        doNothing().when(cartService).clearCart(TENANT_ID, USER_ID);

        ResponseEntity<Void> response = cartController.clearCart(TENANT_ID, USER_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cartService).clearCart(TENANT_ID, USER_ID);
    }
}
