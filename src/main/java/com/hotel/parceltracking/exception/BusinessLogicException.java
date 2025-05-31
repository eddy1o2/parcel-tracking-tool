package com.hotel.parceltracking.exception;

/**
 * Exception thrown when business logic rules are violated.
 * Used for cases like room occupied, guest not checked in, parcel already collected, etc.
 */
public class BusinessLogicException extends RuntimeException {
    
    public BusinessLogicException(String message) {
        super(message);
    }
    
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
} 