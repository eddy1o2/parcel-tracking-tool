package com.hotel.parceltracking.repository;

import com.hotel.parceltracking.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Parcel entity.
 * Provides data access operations for parcels.
 */
@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    
    /**
     * Finds a parcel by its tracking number.
     * @param trackingNumber the tracking number to search for
     * @return Optional containing the parcel if found
     */
    Optional<Parcel> findByTrackingNumber(String trackingNumber);
    
    /**
     * Finds all parcels for a specific guest.
     * @param guestId the guest ID
     * @return list of parcels for the guest
     */
    List<Parcel> findByGuestId(Long guestId);
    
    /**
     * Finds all uncollected parcels for a specific guest.
     * @param guestId the guest ID
     * @return list of uncollected parcels for the guest
     */
    @Query("SELECT p FROM Parcel p WHERE p.guest.id = :guestId AND p.collected = false")
    List<Parcel> findUncollectedParcelsByGuestId(@Param("guestId") Long guestId);
    
    /**
     * Finds all uncollected parcels.
     * @return list of all uncollected parcels
     */
    @Query("SELECT p FROM Parcel p WHERE p.collected = false")
    List<Parcel> findAllUncollectedParcels();
    
    /**
     * Finds all collected parcels.
     * @return list of all collected parcels
     */
    @Query("SELECT p FROM Parcel p WHERE p.collected = true")
    List<Parcel> findAllCollectedParcels();
    
    /**
     * Finds all parcels for guests who are currently checked in.
     * @return list of parcels for checked-in guests
     */
    @Query("SELECT p FROM Parcel p WHERE p.guest.checkOutTime IS NULL")
    List<Parcel> findParcelsForCheckedInGuests();
    
    /**
     * Finds uncollected parcels for a guest by room number.
     * @param roomNumber the room number
     * @return list of uncollected parcels for the guest in that room
     */
    @Query("SELECT p FROM Parcel p WHERE p.guest.roomNumber = :roomNumber AND p.collected = false")
    List<Parcel> findUncollectedParcelsByGuestRoomNumber(@Param("roomNumber") String roomNumber);
} 