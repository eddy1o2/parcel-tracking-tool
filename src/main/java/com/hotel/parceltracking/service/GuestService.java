package com.hotel.parceltracking.service;

import com.hotel.parceltracking.dto.GuestDto;
import com.hotel.parceltracking.model.Guest;
import com.hotel.parceltracking.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing hotel guests.
 * Handles business logic for guest check-in, check-out, and tracking operations.
 */
@Service
@Transactional
public class GuestService {
    
    private final GuestRepository guestRepository;
    private final ParcelService parcelService;
    
    @Autowired
    public GuestService(GuestRepository guestRepository, ParcelService parcelService) {
        this.guestRepository = guestRepository;
        this.parcelService = parcelService;
    }
    
    /**
     * Checks in a new guest to the hotel.
     * @param guestDto the guest information
     * @return the checked-in guest
     * @throws IllegalArgumentException if room is already occupied
     */
    public GuestDto checkInGuest(GuestDto guestDto) {
        // Check if room is already occupied
        if (guestRepository.isGuestCheckedInByRoomNumber(guestDto.getRoomNumber())) {
            throw new IllegalArgumentException("Room " + guestDto.getRoomNumber() + " is already occupied");
        }
        
        Guest guest = new Guest(guestDto.getName(), guestDto.getRoomNumber(), LocalDateTime.now());
        Guest savedGuest = guestRepository.save(guest);
        
        return convertToDto(savedGuest);
    }
    
    /**
     * Checks out a guest from the hotel.
     * @param guestId the guest ID
     * @return the checked-out guest
     * @throws IllegalArgumentException if guest not found or already checked out
     */
    public GuestDto checkOutGuest(Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Guest not found with ID: " + guestId));
        
        if (!guest.isCheckedIn()) {
            throw new IllegalArgumentException("Guest is already checked out");
        }
        
        guest.checkOut();
        Guest savedGuest = guestRepository.save(guest);
        
        return convertToDto(savedGuest);
    }
    
    /**
     * Checks out a guest by room number.
     * @param roomNumber the room number
     * @return the checked-out guest
     * @throws IllegalArgumentException if no checked-in guest found in the room
     */
    public GuestDto checkOutGuestByRoomNumber(String roomNumber) {
        List<Guest> checkedInGuests = guestRepository.findAllCheckedInGuests();
        Guest guest = checkedInGuests.stream()
                .filter(g -> g.getRoomNumber().equals(roomNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No checked-in guest found in room: " + roomNumber));
        
        return checkOutGuest(guest.getId());
    }
    
    /**
     * Gets all currently checked-in guests.
     * @return list of checked-in guests
     */
    @Transactional(readOnly = true)
    public List<GuestDto> getAllCheckedInGuests() {
        return guestRepository.findAllCheckedInGuests().stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * Gets all guests (checked-in and checked-out).
     * @return list of all guests
     */
    @Transactional(readOnly = true)
    public List<GuestDto> getAllGuests() {
        return guestRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * Finds a guest by ID.
     * @param guestId the guest ID
     * @return the guest if found
     * @throws IllegalArgumentException if guest not found
     */
    @Transactional(readOnly = true)
    public GuestDto getGuestById(Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Guest not found with ID: " + guestId));
        return convertToDto(guest);
    }
    
    /**
     * Finds a guest by room number.
     * @param roomNumber the room number
     * @return the guest if found
     */
    @Transactional(readOnly = true)
    public Optional<GuestDto> getGuestByRoomNumber(String roomNumber) {
        return guestRepository.findByRoomNumber(roomNumber)
                .map(this::convertToDto);
    }
    
    /**
     * Checks if a guest is currently checked in by room number.
     * @param roomNumber the room number
     * @return true if guest is checked in, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean isGuestCheckedIn(String roomNumber) {
        return guestRepository.isGuestCheckedInByRoomNumber(roomNumber);
    }
    
    /**
     * Converts Guest entity to GuestDto.
     * @param guest the guest entity
     * @return the guest DTO
     */
    private GuestDto convertToDto(Guest guest) {
        GuestDto dto = new GuestDto();
        dto.setId(guest.getId());
        dto.setName(guest.getName());
        dto.setRoomNumber(guest.getRoomNumber());
        dto.setCheckInTime(guest.getCheckInTime());
        dto.setCheckOutTime(guest.getCheckOutTime());
        dto.setCheckedIn(guest.isCheckedIn());
        
        // Include parcels if needed (lazy loading consideration)
        if (guest.getParcels() != null && !guest.getParcels().isEmpty()) {
            dto.setParcels(guest.getParcels().stream()
                    .map(parcelService::convertToDto)
                    .toList());
        }
        
        return dto;
    }
} 