package com.hotel.parceltracking.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Used for cases like guest not found, parcel not found, etc.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 