package com.hotel.parceltracking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Parcel entity.
 * Used for API requests and responses.
 * Using Lombok for reduced boilerplate code.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    // Custom constructor for common use case (accepting parcel)
    public ParcelDto(String trackingNumber, String sender, String description, Long guestId) {
        this.trackingNumber = trackingNumber;
        this.sender = sender;
        this.description = description;
        this.guestId = guestId;
        this.collected = false;
    }
    
    // Factory method for creating response DTOs
    public static ParcelDto of(Long id, String trackingNumber, String sender, String description,
                              LocalDateTime arrivalTime, LocalDateTime collectionTime, boolean collected,
                              Long guestId, String guestName, String guestRoomNumber) {
        ParcelDto dto = new ParcelDto();
        dto.setId(id);
        dto.setTrackingNumber(trackingNumber);
        dto.setSender(sender);
        dto.setDescription(description);
        dto.setArrivalTime(arrivalTime);
        dto.setCollectionTime(collectionTime);
        dto.setCollected(collected);
        dto.setGuestId(guestId);
        dto.setGuestName(guestName);
        dto.setGuestRoomNumber(guestRoomNumber);
        return dto;
    }
} 