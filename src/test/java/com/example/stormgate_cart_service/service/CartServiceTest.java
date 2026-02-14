package com.example.stormgate_cart_service.service;

import com.example.stormgate_cart_service.dto.AddItemRequest;
import com.example.stormgate_cart_service.dto.CartResponse;
import com.example.stormgate_cart_service.dto.UpdateQuantityRequest;
import com.example.stormgate_cart_service.entity.Cart;
import com.example.stormgate_cart_service.entity.CartItem;
import com.example.stormgate_cart_service.exception.CartNotFoundException;
import com.example.stormgate_cart_service.exception.ItemNotFoundException;
import com.example.stormgate_cart_service.repository.CartItemRepository;
import com.example.stormgate_cart_service.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for CartService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CartService Tests")
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private CartItem cartItem;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-456";
    private static final String PRODUCT_ID = "product-789";
    private static final String CART_ID = "cart-123";

    @BeforeEach
    void setUp() {
        cart = Cart.builder()
                .cartId(CART_ID)
                .tenantId(TENANT_ID)
                .userId(USER_ID)
                .items(new ArrayList<>())
                .totalAmount(BigDecimal.ZERO)
                .currency("USD")
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        cartItem = CartItem.builder()
                .itemId("item-1")
                .cart(cart)
                .productId(PRODUCT_ID)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(2)
                .subtotal(new BigDecimal("199.98"))
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        cart.getItems().add(cartItem);
    }

    @Test
    @DisplayName("GetCart should return cart response when cart exists")
    void testGetCartSuccess() {
        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.of(cart));

        CartResponse response = cartService.getCart(TENANT_ID, USER_ID);

        assertNotNull(response);
        assertEquals(CART_ID, response.getCartId());
        assertEquals(TENANT_ID, response.getTenantId());
        assertEquals(USER_ID, response.getUserId());
        verify(cartRepository).findByTenantIdAndUserId(TENANT_ID, USER_ID);
    }

    @Test
    @DisplayName("GetCart should throw CartNotFoundException when cart doesn't exist")
    void testGetCartNotFound() {
        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () ->
                cartService.getCart(TENANT_ID, USER_ID));

        verify(cartRepository).findByTenantIdAndUserId(TENANT_ID, USER_ID);
    }

    @Test
    @DisplayName("AddItemToCart should create new item when product doesn't exist")
    void testAddItemToCartNewItem() {
        AddItemRequest request = AddItemRequest.builder()
                .productId(PRODUCT_ID)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(1)
                .build();

        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(CART_ID, PRODUCT_ID))
                .thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse response = cartService.addItemToCart(TENANT_ID, USER_ID, request);

        assertNotNull(response);
        assertEquals(CART_ID, response.getCartId());
        verify(cartRepository).findByTenantIdAndUserId(TENANT_ID, USER_ID);
        verify(cartItemRepository).findByCartIdAndProductId(CART_ID, PRODUCT_ID);
    }

    @Test
    @DisplayName("AddItemToCart should update quantity when product exists")
    void testAddItemToCartExistingItem() {
        AddItemRequest request = AddItemRequest.builder()
                .productId(PRODUCT_ID)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(1)
                .build();

        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(CART_ID, PRODUCT_ID))
                .thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse response = cartService.addItemToCart(TENANT_ID, USER_ID, request);

        assertNotNull(response);
        assertEquals(CART_ID, response.getCartId());
        assertEquals(3, cartItem.getQuantity());
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("AddItemToCart should restore deleted item")
    void testAddItemToCartRestoredItem() {
        cartItem.setIsDeleted(true);

        AddItemRequest request = AddItemRequest.builder()
                .productId(PRODUCT_ID)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(1)
                .build();

        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(CART_ID, PRODUCT_ID))
                .thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse response = cartService.addItemToCart(TENANT_ID, USER_ID, request);

        assertNotNull(response);
        assertEquals(false, cartItem.getIsDeleted());
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("UpdateItemQuantity should update item quantity successfully")
    void testUpdateItemQuantitySuccess() {
        UpdateQuantityRequest request = UpdateQuantityRequest.builder()
                .quantity(5)
                .build();

        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(CART_ID, PRODUCT_ID))
                .thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse response = cartService.updateItemQuantity(TENANT_ID, USER_ID,
                PRODUCT_ID, request);

        assertNotNull(response);
        assertEquals(5, cartItem.getQuantity());
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("UpdateItemQuantity should throw error when cart not found")
    void testUpdateItemQuantityCartNotFound() {
        UpdateQuantityRequest request = UpdateQuantityRequest.builder()
                .quantity(5)
                .build();

        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () ->
                cartService.updateItemQuantity(TENANT_ID, USER_ID, PRODUCT_ID, request));
    }

    @Test
    @DisplayName("UpdateItemQuantity should throw error when item not found")
    void testUpdateItemQuantityItemNotFound() {
        UpdateQuantityRequest request = UpdateQuantityRequest.builder()
                .quantity(5)
                .build();

        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(CART_ID, PRODUCT_ID))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () ->
                cartService.updateItemQuantity(TENANT_ID, USER_ID, PRODUCT_ID, request));
    }

    @Test
    @DisplayName("RemoveItemFromCart should soft delete item")
    void testRemoveItemFromCartSuccess() {
        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(CART_ID, PRODUCT_ID))
                .thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponse response = cartService.removeItemFromCart(TENANT_ID, USER_ID, PRODUCT_ID);

        assertNotNull(response);
        assertEquals(true, cartItem.getIsDeleted());
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("RemoveItemFromCart should throw error when cart not found")
    void testRemoveItemFromCartNotFound() {
        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () ->
                cartService.removeItemFromCart(TENANT_ID, USER_ID, PRODUCT_ID));
    }

    @Test
    @DisplayName("RemoveItemFromCart should throw error when item not found")
    void testRemoveItemFromCartItemNotFound() {
        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndProductId(CART_ID, PRODUCT_ID))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () ->
                cartService.removeItemFromCart(TENANT_ID, USER_ID, PRODUCT_ID));
    }

    @Test
    @DisplayName("ClearCart should mark all items as deleted")
    void testClearCartSuccess() {
        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.clearCart(TENANT_ID, USER_ID);

        assertEquals(true, cartItem.getIsDeleted());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("ClearCart should throw error when cart not found")
    void testClearCartNotFound() {
        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () ->
                cartService.clearCart(TENANT_ID, USER_ID));
    }

    @Test
    @DisplayName("AddItemToCart should create new cart when it doesn't exist")
    void testAddItemToCartCreateNewCart() {
        AddItemRequest request = AddItemRequest.builder()
                .productId(PRODUCT_ID)
                .name("Test Product")
                .price(new BigDecimal("99.99"))
                .quantity(1)
                .build();

        when(cartRepository.findByTenantIdAndUserId(TENANT_ID, USER_ID))
                .thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartItemRepository.findByCartIdAndProductId(CART_ID, PRODUCT_ID))
                .thenReturn(Optional.empty());

        CartResponse response = cartService.addItemToCart(TENANT_ID, USER_ID, request);

        assertNotNull(response);
        verify(cartRepository, times(2)).save(any(Cart.class));
    }
}
