package com.hotel.parceltracking.controller;

import com.hotel.parceltracking.dto.ParcelDto;
import com.hotel.parceltracking.service.ParcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing parcels.
 * Provides endpoints for parcel acceptance, tracking, and collection operations.
 */
@RestController
@RequestMapping("/api/parcels")
@Tag(name = "Parcel Management", description = "APIs for managing hotel parcels")
public class ParcelController {
    
    private final ParcelService parcelService;
    
    @Autowired
    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }
    
    /**
     * Accepts a new parcel for a guest.
     */
    @PostMapping("/accept")
    @Operation(summary = "Accept a parcel", description = "Accepts a new parcel for a checked-in guest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parcel accepted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input, guest not found, or guest not checked in"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ParcelDto> acceptParcel(
            @Valid @RequestBody ParcelDto parcelDto) {
        try {
            ParcelDto acceptedParcel = parcelService.acceptParcel(parcelDto);
            return new ResponseEntity<>(acceptedParcel, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Marks a parcel as collected by parcel ID.
     */
    @PutMapping("/{parcelId}/collect")
    @Operation(summary = "Collect a parcel by ID", description = "Marks a parcel as collected")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcel collected successfully"),
            @ApiResponse(responseCode = "400", description = "Parcel not found or already collected"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ParcelDto> collectParcel(
            @Parameter(description = "Parcel ID") @PathVariable Long parcelId) {
        try {
            ParcelDto collectedParcel = parcelService.collectParcel(parcelId);
            return ResponseEntity.ok(collectedParcel);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Marks a parcel as collected by tracking number.
     */
    @PutMapping("/tracking/{trackingNumber}/collect")
    @Operation(summary = "Collect a parcel by tracking number", description = "Marks a parcel as collected using tracking number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcel collected successfully"),
            @ApiResponse(responseCode = "400", description = "Parcel not found or already collected"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ParcelDto> collectParcelByTrackingNumber(
            @Parameter(description = "Tracking number") @PathVariable String trackingNumber) {
        try {
            ParcelDto collectedParcel = parcelService.collectParcelByTrackingNumber(trackingNumber);
            return ResponseEntity.ok(collectedParcel);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Gets all parcels available for pickup for a specific guest.
     */
    @GetMapping("/guest/{guestId}/available")
    @Operation(summary = "Get available parcels for guest", description = "Retrieves all uncollected parcels for a specific guest")
    @ApiResponse(responseCode = "200", description = "Available parcels retrieved successfully")
    public ResponseEntity<List<ParcelDto>> getAvailableParcelsForGuest(
            @Parameter(description = "Guest ID") @PathVariable Long guestId) {
        List<ParcelDto> availableParcels = parcelService.getAvailableParcelsForGuest(guestId);
        return ResponseEntity.ok(availableParcels);
    }
    
    /**
     * Gets all parcels available for pickup for a guest by room number.
     */
    @GetMapping("/room/{roomNumber}/available")
    @Operation(summary = "Get available parcels by room number", description = "Retrieves all uncollected parcels for a guest by room number")
    @ApiResponse(responseCode = "200", description = "Available parcels retrieved successfully")
    public ResponseEntity<List<ParcelDto>> getAvailableParcelsForGuestByRoomNumber(
            @Parameter(description = "Room number") @PathVariable String roomNumber) {
        List<ParcelDto> availableParcels = parcelService.getAvailableParcelsForGuestByRoomNumber(roomNumber);
        return ResponseEntity.ok(availableParcels);
    }
    
    /**
     * Gets all uncollected parcels in the system.
     */
    @GetMapping("/uncollected")
    @Operation(summary = "Get all uncollected parcels", description = "Retrieves all uncollected parcels in the system")
    @ApiResponse(responseCode = "200", description = "Uncollected parcels retrieved successfully")
    public ResponseEntity<List<ParcelDto>> getAllUncollectedParcels() {
        List<ParcelDto> uncollectedParcels = parcelService.getAllUncollectedParcels();
        return ResponseEntity.ok(uncollectedParcels);
    }
    
    /**
     * Gets all parcels for currently checked-in guests.
     */
    @GetMapping("/checked-in-guests")
    @Operation(summary = "Get parcels for checked-in guests", description = "Retrieves all parcels for currently checked-in guests")
    @ApiResponse(responseCode = "200", description = "Parcels for checked-in guests retrieved successfully")
    public ResponseEntity<List<ParcelDto>> getParcelsForCheckedInGuests() {
        List<ParcelDto> parcels = parcelService.getParcelsForCheckedInGuests();
        return ResponseEntity.ok(parcels);
    }
    
    /**
     * Gets all parcels in the system.
     */
    @GetMapping
    @Operation(summary = "Get all parcels", description = "Retrieves all parcels in the system")
    @ApiResponse(responseCode = "200", description = "All parcels retrieved successfully")
    public ResponseEntity<List<ParcelDto>> getAllParcels() {
        List<ParcelDto> allParcels = parcelService.getAllParcels();
        return ResponseEntity.ok(allParcels);
    }
    
    /**
     * Gets a parcel by tracking number.
     */
    @GetMapping("/tracking/{trackingNumber}")
    @Operation(summary = "Get parcel by tracking number", description = "Retrieves a specific parcel by tracking number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcel found"),
            @ApiResponse(responseCode = "404", description = "Parcel not found")
    })
    public ResponseEntity<ParcelDto> getParcelByTrackingNumber(
            @Parameter(description = "Tracking number") @PathVariable String trackingNumber) {
        try {
            ParcelDto parcel = parcelService.getParcelByTrackingNumber(trackingNumber);
            return ResponseEntity.ok(parcel);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
} 