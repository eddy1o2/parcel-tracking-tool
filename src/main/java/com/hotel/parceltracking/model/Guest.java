package com.hotel.parceltracking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a hotel guest.
 * Tracks guest check-in/check-out status and associated parcels.
 */
@Entity
@Table(name = "guests")
public class Guest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Guest name is required")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Room number is required")
    @Column(name = "room_number", nullable = false)
    private String roomNumber;
    
    @NotNull(message = "Check-in time is required")
    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;
    
    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;
    
    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Parcel> parcels = new ArrayList<>();
    
    // Constructors
    public Guest() {}
    
    public Guest(String name, String roomNumber, LocalDateTime checkInTime) {
        this.name = name;
        this.roomNumber = roomNumber;
        this.checkInTime = checkInTime;
    }
    
    // Business methods
    
    /**
     * Checks if the guest is currently checked in (not checked out yet).
     * @return true if guest is checked in, false otherwise
     */
    public boolean isCheckedIn() {
        return checkOutTime == null;
    }
    
    /**
     * Checks out the guest by setting the check-out time.
     */
    public void checkOut() {
        this.checkOutTime = LocalDateTime.now();
    }
    
    /**
     * Gets all parcels that are available for pickup (not yet collected).
     * @return list of available parcels
     */
    public List<Parcel> getAvailableParcels() {
        return parcels.stream()
                .filter(parcel -> !parcel.isCollected())
                .toList();
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
    
    public List<Parcel> getParcels() {
        return parcels;
    }
    
    public void setParcels(List<Parcel> parcels) {
        this.parcels = parcels;
    }
    
    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", checkInTime=" + checkInTime +
                ", checkOutTime=" + checkOutTime +
                ", isCheckedIn=" + isCheckedIn() +
                '}';
    }
} 