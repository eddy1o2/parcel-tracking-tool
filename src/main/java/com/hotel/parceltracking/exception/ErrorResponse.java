package com.hotel.parceltracking.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standardized error response format for the API.
 * Used by the global exception handler to provide consistent error responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
     * Error type/category.
     */
    private String error;
    
    /**
     * Human-readable error message.
     */
    private String message;
    
    /**
     * API endpoint path where the error occurred.
     */
    private String path;
    
    /**
     * Validation errors (field-specific errors).
     * Only populated for validation failures.
     */
    private Map<String, String> validationErrors;
} 