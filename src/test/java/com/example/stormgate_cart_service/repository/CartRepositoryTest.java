package com.example.stormgate_cart_service.repository;

import com.example.stormgate_cart_service.entity.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for CartRepository using mocks.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CartRepository Tests")
class CartRepositoryTest {

    @Mock
    private CartRepository cartRepository;

    private Cart cart;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";

    @BeforeEach
    void setUp() {
        cart = Cart.builder()
                .cartId("cart-123")
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .currency("USD")
                .totalAmount(BigDecimal.ZERO)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should find cart by tenant and user ID")
    void testFindByTenantIdAndUserId() {
        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.of(cart));

        Optional<Cart> foundCart = cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID);

        assertTrue(foundCart.isPresent());
        assertEquals(TENANT_ID, foundCart.get().getTenantId());
        assertEquals(USER_ID, foundCart.get().getUserId());
        verify(cartRepository).findByTenantIdAndUserId(TENANT_ID, USER_ID);
    }

    @Test
    @DisplayName("Should not find cart not present")
    void testFindCartNotFound() {
        when(cartRepository.findByTenantIdAndUserId("other-tenant", USER_ID))
                .thenReturn(Optional.empty());

        Optional<Cart> foundCart = cartRepository.findByTenantIdAndUserId("other-tenant", USER_ID);

        assertFalse(foundCart.isPresent());
    }

    @Test
    @DisplayName("Should check if cart exists")
    void testExistsByTenantIdAndUserId() {
        when(cartRepository.existsByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(true);

        boolean exists = cartRepository.existsByTenantIdAndUserId(TENANT_ID, USER_ID);

        assertTrue(exists);
        verify(cartRepository).existsByTenantIdAndUserId(TENANT_ID, USER_ID);
    }

    @Test
    @DisplayName("Should return false when cart doesn't exist")
    void testExistsCartNotFound() {
        when(cartRepository.existsByTenantIdAndUserId("other-tenant", USER_ID))
                .thenReturn(false);

        boolean exists = cartRepository.existsByTenantIdAndUserId("other-tenant", USER_ID);

        assertFalse(exists);
    }

    @Test
    @DisplayName("Should save cart successfully")
    void testSaveCart() {
        when(cartRepository.save(any(Cart.class)))
                .thenReturn(cart);

        Cart savedCart = cartRepository.save(cart);

        assertNotNull(savedCart);
        assertEquals(TENANT_ID, savedCart.getTenantId());
        verify(cartRepository).save(any(Cart.class));
    }
}