package com.example.stormgate_cart_service.exception;

/**
 * Exception thrown when a requested cart is not found.
 */
public class CartNotFoundException extends RuntimeException {

    /**
     * Creates a new CartNotFoundException with the specified message.
     *
     * @param message the error message
     */
    public CartNotFoundException(final String message) {
        super(message);
    }
}
