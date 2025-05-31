package com.hotel.parceltracking.controller;

import com.hotel.parceltracking.dto.GuestDto;
import com.hotel.parceltracking.service.GuestService;
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
 * REST Controller for managing hotel guests.
 * Provides endpoints for guest check-in, check-out, and tracking operations.
 */
@RestController
@RequestMapping("/api/guests")
@Tag(name = "Guest Management", description = "APIs for managing hotel guests")
public class GuestController {
    
    private final GuestService guestService;
    
    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }
    
    /**
     * Checks in a new guest to the hotel.
     */
    @PostMapping("/check-in")
    @Operation(summary = "Check in a guest", description = "Registers a new guest check-in to the hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Guest checked in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or room already occupied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GuestDto> checkInGuest(@Valid @RequestBody GuestDto guestDto) {
        GuestDto checkedInGuest = guestService.checkInGuest(guestDto);
        return new ResponseEntity<>(checkedInGuest, HttpStatus.CREATED);
    }
    
    /**
     * Checks out a guest from the hotel by guest ID.
     */
    @PutMapping("/{guestId}/check-out")
    @Operation(summary = "Check out a guest by ID", description = "Checks out a guest from the hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest checked out successfully"),
            @ApiResponse(responseCode = "400", description = "Guest already checked out"),
            @ApiResponse(responseCode = "404", description = "Guest not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GuestDto> checkOutGuest(@Parameter(description = "Guest ID") @PathVariable Long guestId) {
        GuestDto checkedOutGuest = guestService.checkOutGuest(guestId);
        return ResponseEntity.ok(checkedOutGuest);
    }
    
    /**
     * Gets all currently checked-in guests.
     */
    @GetMapping("/checked-in")
    @Operation(summary = "Get all checked-in guests", description = "Retrieves a list of all currently checked-in guests")
    @ApiResponse(responseCode = "200", description = "List of checked-in guests retrieved successfully")
    public ResponseEntity<List<GuestDto>> getAllCheckedInGuests() {
        List<GuestDto> checkedInGuests = guestService.getAllCheckedInGuests();
        return ResponseEntity.ok(checkedInGuests);
    }
    
    /**
     * Gets all guests (checked-in and checked-out).
     */
    @GetMapping
    @Operation(summary = "Get all guests", description = "Retrieves a list of all guests in the system")
    @ApiResponse(responseCode = "200", description = "List of all guests retrieved successfully")
    public ResponseEntity<List<GuestDto>> getAllGuests() {
        List<GuestDto> allGuests = guestService.getAllGuests();
        return ResponseEntity.ok(allGuests);
    }
    
    /**
     * Gets a guest by ID.
     */
    @GetMapping("/{guestId}")
    @Operation(summary = "Get guest by ID", description = "Retrieves a specific guest by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest found"),
            @ApiResponse(responseCode = "404", description = "Guest not found")
    })
    public ResponseEntity<GuestDto> getGuestById(@Parameter(description = "Guest ID") @PathVariable Long guestId) {
        GuestDto guest = guestService.getGuestById(guestId);
        return ResponseEntity.ok(guest);
    }
    
    /**
     * Checks if a guest is currently checked in by room number.
     */
    @GetMapping("/room/{roomNumber}/status")
    @Operation(summary = "Check guest status by room number", description = "Checks if a guest is currently checked in for a specific room")
    @ApiResponse(responseCode = "200", description = "Guest status retrieved successfully")
    public ResponseEntity<Boolean> isGuestCheckedIn(@Parameter(description = "Room number") @PathVariable String roomNumber) {
        boolean isCheckedIn = guestService.isGuestCheckedIn(roomNumber);
        return ResponseEntity.ok(isCheckedIn);
    }
} 