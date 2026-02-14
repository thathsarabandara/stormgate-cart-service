package com.example.stormgate_cart_service.exception;

/**
 * Exception thrown when a requested item is not found in the cart.
 */
public class ItemNotFoundException extends RuntimeException {

    /**
     * Creates a new ItemNotFoundException with the specified message.
     *
     * @param message the error message
     */
    public ItemNotFoundException(final String message) {
        super(message);
    }
}
