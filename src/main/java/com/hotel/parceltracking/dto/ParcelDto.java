package com.hotel.parceltracking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Parcel entity.
 * Used for API requests and responses.
 */
public class ParcelDto {
    
    private Long id;
    
    @NotBlank(message = "Tracking number is required")
    private String trackingNumber;
    
    @NotBlank(message = "Sender name is required")
    private String sender;
    
    private String description;
    
    private LocalDateTime arrivalTime;
    private LocalDateTime collectionTime;
    private boolean collected;
    
    @NotNull(message = "Guest ID is required")
    private Long guestId;
    
    private String guestName;
    private String guestRoomNumber;
    
    // Constructors
    public ParcelDto() {}
    
    public ParcelDto(String trackingNumber, String sender, String description, Long guestId) {
        this.trackingNumber = trackingNumber;
        this.sender = sender;
        this.description = description;
        this.guestId = guestId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTrackingNumber() {
        return trackingNumber;
    }
    
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
    
    public String getSender() {
        return sender;
    }
    
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public LocalDateTime getCollectionTime() {
        return collectionTime;
    }
    
    public void setCollectionTime(LocalDateTime collectionTime) {
        this.collectionTime = collectionTime;
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
    
    public Long getGuestId() {
        return guestId;
    }
    
    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }
    
    public String getGuestName() {
        return guestName;
    }
    
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
    
    public String getGuestRoomNumber() {
        return guestRoomNumber;
    }
    
    public void setGuestRoomNumber(String guestRoomNumber) {
        this.guestRoomNumber = guestRoomNumber;
    }
} 