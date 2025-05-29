package com.hotel.parceltracking.service;

import com.hotel.parceltracking.dto.ParcelDto;
import com.hotel.parceltracking.model.Guest;
import com.hotel.parceltracking.model.Parcel;
import com.hotel.parceltracking.repository.GuestRepository;
import com.hotel.parceltracking.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing parcels.
 * Handles business logic for parcel acceptance, tracking, and collection operations.
 */
@Service
@Transactional
public class ParcelService {
    
    private final ParcelRepository parcelRepository;
    private final GuestRepository guestRepository;
    
    @Autowired
    public ParcelService(ParcelRepository parcelRepository, GuestRepository guestRepository) {
        this.parcelRepository = parcelRepository;
        this.guestRepository = guestRepository;
    }
    
    /**
     * Accepts a new parcel for a guest.
     * Validates that the guest is currently checked in before accepting the parcel.
     * @param parcelDto the parcel information
     * @return the accepted parcel
     * @throws IllegalArgumentException if guest not found or not checked in
     */
    public ParcelDto acceptParcel(ParcelDto parcelDto) {
        // Find the guest
        Guest guest = guestRepository.findById(parcelDto.getGuestId())
                .orElseThrow(() -> new IllegalArgumentException("Guest not found with ID: " + parcelDto.getGuestId()));
        
        // Check if guest is currently checked in
        if (!guest.isCheckedIn()) {
            throw new IllegalArgumentException("Cannot accept parcel for guest who is not checked in: " + guest.getName());
        }
        
        // Check if tracking number already exists
        if (parcelRepository.findByTrackingNumber(parcelDto.getTrackingNumber()).isPresent()) {
            throw new IllegalArgumentException("Parcel with tracking number " + parcelDto.getTrackingNumber() + " already exists");
        }
        
        Parcel parcel = new Parcel(
            parcelDto.getTrackingNumber(),
            parcelDto.getSender(),
            parcelDto.getDescription(),
            guest
        );
        
        Parcel savedParcel = parcelRepository.save(parcel);
        return convertToDto(savedParcel);
    }
    
    /**
     * Marks a parcel as collected.
     * @param parcelId the parcel ID
     * @return the collected parcel
     * @throws IllegalArgumentException if parcel not found or already collected
     */
    public ParcelDto collectParcel(Long parcelId) {
        Parcel parcel = parcelRepository.findById(parcelId)
                .orElseThrow(() -> new IllegalArgumentException("Parcel not found with ID: " + parcelId));
        
        if (parcel.isCollected()) {
            throw new IllegalArgumentException("Parcel is already collected");
        }
        
        parcel.markAsCollected();
        Parcel savedParcel = parcelRepository.save(parcel);
        
        return convertToDto(savedParcel);
    }
    
    /**
     * Marks a parcel as collected by tracking number.
     * @param trackingNumber the tracking number
     * @return the collected parcel
     * @throws IllegalArgumentException if parcel not found or already collected
     */
    public ParcelDto collectParcelByTrackingNumber(String trackingNumber) {
        Parcel parcel = parcelRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new IllegalArgumentException("Parcel not found with tracking number: " + trackingNumber));
        
        return collectParcel(parcel.getId());
    }
    
    /**
     * Gets all parcels available for pickup for a specific guest.
     * @param guestId the guest ID
     * @return list of available parcels
     */
    @Transactional(readOnly = true)
    public List<ParcelDto> getAvailableParcelsForGuest(Long guestId) {
        return parcelRepository.findUncollectedParcelsByGuestId(guestId).stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * Gets all parcels available for pickup for a guest by room number.
     * @param roomNumber the room number
     * @return list of available parcels
     */
    @Transactional(readOnly = true)
    public List<ParcelDto> getAvailableParcelsForGuestByRoomNumber(String roomNumber) {
        return parcelRepository.findUncollectedParcelsByGuestRoomNumber(roomNumber).stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * Gets all uncollected parcels in the system.
     * @return list of all uncollected parcels
     */
    @Transactional(readOnly = true)
    public List<ParcelDto> getAllUncollectedParcels() {
        return parcelRepository.findAllUncollectedParcels().stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * Gets all parcels for currently checked-in guests.
     * @return list of parcels for checked-in guests
     */
    @Transactional(readOnly = true)
    public List<ParcelDto> getParcelsForCheckedInGuests() {
        return parcelRepository.findParcelsForCheckedInGuests().stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * Gets all parcels in the system.
     * @return list of all parcels
     */
    @Transactional(readOnly = true)
    public List<ParcelDto> getAllParcels() {
        return parcelRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }
    
    /**
     * Finds a parcel by tracking number.
     * @param trackingNumber the tracking number
     * @return the parcel if found
     * @throws IllegalArgumentException if parcel not found
     */
    @Transactional(readOnly = true)
    public ParcelDto getParcelByTrackingNumber(String trackingNumber) {
        Parcel parcel = parcelRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new IllegalArgumentException("Parcel not found with tracking number: " + trackingNumber));
        return convertToDto(parcel);
    }
    
    /**
     * Converts Parcel entity to ParcelDto.
     * @param parcel the parcel entity
     * @return the parcel DTO
     */
    public ParcelDto convertToDto(Parcel parcel) {
        return ParcelDto.of(
            parcel.getId(),
            parcel.getTrackingNumber(),
            parcel.getSender(),
            parcel.getDescription(),
            parcel.getArrivalTime(),
            parcel.getCollectionTime(),
            parcel.isCollected(),
            parcel.getGuest().getId(),
            parcel.getGuest().getName(),
            parcel.getGuest().getRoomNumber()
        );
    }
} 