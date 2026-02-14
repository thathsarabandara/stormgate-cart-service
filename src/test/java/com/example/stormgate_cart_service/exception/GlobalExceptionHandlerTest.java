package com.example.stormgate_cart_service.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for GlobalExceptionHandler.
 */
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest mockRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/cart");
    }

    @Test
    @DisplayName("Handle CartNotFoundException should return 404 Not Found")
    void testHandleCartNotFoundException() {
        CartNotFoundException exception = new CartNotFoundException("Cart not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler
                .handleCartNotFoundException(exception, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Cart not found", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Handle ItemNotFoundException should return 404 Not Found")
    void testHandleItemNotFoundException() {
        ItemNotFoundException exception = new ItemNotFoundException("Item not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler
                .handleItemNotFoundException(exception, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Item not found", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Handle IllegalArgumentException should return 400 Bad Request")
    void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<ErrorResponse> response = exceptionHandler
                .handleIllegalArgumentException(exception, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Invalid argument", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Handle generic Exception should return 500 Internal Server Error")
    void testHandleGenericException() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response = exceptionHandler
                .handleGenericException(exception, mockRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }

    @Test
    @DisplayName("ErrorResponse should include timestamp")
    void testErrorResponseTimestamp() {
        CartNotFoundException exception = new CartNotFoundException("Cart not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler
                .handleCartNotFoundException(exception, mockRequest);

        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("ErrorResponse should include path")
    void testErrorResponsePath() {
        CartNotFoundException exception = new CartNotFoundException("Cart not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler
                .handleCartNotFoundException(exception, mockRequest);

        assertEquals("/api/cart", response.getBody().getPath());
    }

    @Test
    @DisplayName("ErrorResponse should not include null fields")
    void testErrorResponseNullFields() {
        CartNotFoundException exception = new CartNotFoundException("Cart not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler
                .handleCartNotFoundException(exception, mockRequest);

        assertNotNull(response.getBody());
        // ValidationErrors should be null for non-validation exceptions
        // This is handled by @JsonInclude(JsonInclude.Include.NON_NULL)
    }
}
