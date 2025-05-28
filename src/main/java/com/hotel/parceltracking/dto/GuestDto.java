package com.hotel.parceltracking.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Guest entity.
 * Used for API requests and responses.
 */
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
    
    // Constructors
    public GuestDto() {}
    
    public GuestDto(String name, String roomNumber) {
        this.name = name;
        this.roomNumber = roomNumber;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }
    
    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
    
    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }
    
    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }
    
    public boolean isCheckedIn() {
        return checkedIn;
    }
    
    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
    
    public List<ParcelDto> getParcels() {
        return parcels;
    }
    
    public void setParcels(List<ParcelDto> parcels) {
        this.parcels = parcels;
    }
} 