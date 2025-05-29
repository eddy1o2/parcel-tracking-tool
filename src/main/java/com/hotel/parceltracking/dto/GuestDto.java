package com.hotel.parceltracking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Guest entity.
 * Used for API requests and responses.
 * Using Lombok for reduced boilerplate code.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestDto {
    
    private Long id;
    
    @NotBlank(message = "Guest name is required")
    private String name;
    
    @NotBlank(message = "Room number is required")
    private String roomNumber;
    
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private boolean checkedIn;
    private List<ParcelDto> parcels;
    
    // Custom constructor for common use case (check-in)
    public GuestDto(String name, String roomNumber) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.checkedIn = false;
    }
    
    // Factory method for creating response DTOs
    public static GuestDto of(Long id, String name, String roomNumber, 
                             LocalDateTime checkInTime, LocalDateTime checkOutTime, 
                             boolean checkedIn, List<ParcelDto> parcels) {
        GuestDto dto = new GuestDto();
        dto.setId(id);
        dto.setName(name);
        dto.setRoomNumber(roomNumber);
        dto.setCheckInTime(checkInTime);
        dto.setCheckOutTime(checkOutTime);
        dto.setCheckedIn(checkedIn);
        dto.setParcels(parcels);
        return dto;
    }
} 