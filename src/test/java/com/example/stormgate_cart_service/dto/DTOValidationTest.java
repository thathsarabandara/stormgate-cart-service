package com.example.stormgate_cart_service.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for DTOs validation.
 */
@DisplayName("DTO Validation Tests")
class DTOValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("AddItemRequest should be valid with correct data")
    void testAddItemRequestValid() {
        AddItemRequest request = AddItemRequest.builder()
                .productId("product-1")
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(1)
                .build();

        Set<ConstraintViolation<AddItemRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("AddItemRequest should fail with missing productId")
    void testAddItemRequestMissingProductId() {
        AddItemRequest request = AddItemRequest.builder()
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(1)
                .build();

        Set<ConstraintViolation<AddItemRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("productId")));
    }

    @Test
    @DisplayName("AddItemRequest should fail with missing name")
    void testAddItemRequestMissingName() {
        AddItemRequest request = AddItemRequest.builder()
                .productId("product-1")
                .price(new BigDecimal("99.99"))
                .quantity(1)
                .build();

        Set<ConstraintViolation<AddItemRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    @DisplayName("AddItemRequest should fail with zero price")
    void testAddItemRequestZeroPrice() {
        AddItemRequest request = AddItemRequest.builder()
                .productId("product-1")
                .name("Test Product")
                .price(BigDecimal.ZERO)
                .quantity(1)
                .build();

        Set<ConstraintViolation<AddItemRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price")));
    }

    @Test
    @DisplayName("AddItemRequest should fail with zero quantity")
    void testAddItemRequestZeroQuantity() {
        AddItemRequest request = AddItemRequest.builder()
                .productId("product-1")
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(0)
                .build();

        Set<ConstraintViolation<AddItemRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("quantity")));
    }

    @Test
    @DisplayName("AddItemRequest should fail with quantity exceeding max")
    void testAddItemRequestExceedsMaxQuantity() {
        AddItemRequest request = AddItemRequest.builder()
                .productId("product-1")
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(1001)
                .build();

        Set<ConstraintViolation<AddItemRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("quantity")));
    }

    @Test
    @DisplayName("UpdateQuantityRequest should be valid with correct data")
    void testUpdateQuantityRequestValid() {
        UpdateQuantityRequest request = UpdateQuantityRequest.builder()
                .quantity(5)
                .build();

        Set<ConstraintViolation<UpdateQuantityRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("UpdateQuantityRequest should fail with zero quantity")
    void testUpdateQuantityRequestZeroQuantity() {
        UpdateQuantityRequest request = UpdateQuantityRequest.builder()
                .quantity(0)
                .build();

        Set<ConstraintViolation<UpdateQuantityRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
    }

    @Test
    @DisplayName("CartResponse should be created with all fields")
    void testCartResponseCreation() {
        CartResponse response = CartResponse.builder()
                .cartId("cart-123")
                .tenantId("tenant-123")
                .userId("user-456")
                .items(java.util.Arrays.asList())
                .itemCount(0)
                .totalAmount(BigDecimal.ZERO)
                .currency("USD")
                .build();

        assertNotNull(response);
        assertEquals("cart-123", response.getCartId());
        assertEquals("USD", response.getCurrency());
    }

    @Test
    @DisplayName("CartItemResponse should be created with all fields")
    void testCartItemResponseCreation() {
        CartItemResponse response = CartItemResponse.builder()
                .productId("product-1")
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(2)
                .subtotal(new BigDecimal("199.98"))
                .build();

        assertNotNull(response);
        assertEquals("product-1", response.getProductId());
        assertEquals("Test Product", response.getName());
        assertEquals(new BigDecimal("99.99"), response.getPrice());
    }
}
