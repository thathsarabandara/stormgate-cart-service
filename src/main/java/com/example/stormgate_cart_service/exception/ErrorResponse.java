package com.example.stormgate_cart_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response DTO for API error responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code.
     */
    private int status;

    /**
     * Error type or category.
     */
    private String error;

    /**
     * Error message.
     */
    private String message;

    /**
     * Requested path that resulted in the error.
     */
    private String path;

    /**
     * Validation errors with field names as keys.
     */
    private Map<String, String> validationErrors;
}
