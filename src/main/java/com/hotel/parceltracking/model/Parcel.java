package com.hotel.parceltracking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Entity representing a parcel received for a hotel guest.
 * Tracks parcel details, arrival time, and collection status.
 */
@Entity
@Table(name = "parcels")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "guest") // Exclude to avoid circular reference
public class Parcel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Tracking number is required")
    @Column(name = "tracking_number", nullable = false, unique = true)
    private String trackingNumber;
    
    @NotBlank(message = "Sender name is required")
    @Column(nullable = false)
    private String sender;
    
    @Column(name = "description")
    private String description;
    
    @NotNull(message = "Arrival time is required")
    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;
    
    @Column(name = "collection_time")
    private LocalDateTime collectionTime;
    
    @Column(name = "is_collected", nullable = false)
    private boolean collected = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    @NotNull(message = "Guest is required")
    private Guest guest;
    
    // Custom constructor
    public Parcel(String trackingNumber, String sender, String description, Guest guest) {
        this.trackingNumber = trackingNumber;
        this.sender = sender;
        this.description = description;
        this.guest = guest;
        this.arrivalTime = LocalDateTime.now();
    }
    
    // Business methods
    
    /**
     * Marks the parcel as collected by setting collection time and status.
     */
    public void markAsCollected() {
        this.collected = true;
        this.collectionTime = LocalDateTime.now();
    }
    
    /**
     * Checks if the parcel is available for pickup (not collected yet).
     * @return true if parcel is available, false if already collected
     */
    public boolean isAvailableForPickup() {
        return !collected;
    }
} 