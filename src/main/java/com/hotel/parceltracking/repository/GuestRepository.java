package com.hotel.parceltracking.repository;

import com.hotel.parceltracking.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Guest entity.
 * Provides data access operations for hotel guests.
 */
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    
    /**
     * Finds a guest by room number.
     * @param roomNumber the room number to search for
     * @return Optional containing the guest if found
     */
    Optional<Guest> findByRoomNumber(String roomNumber);
    
    /**
     * Finds all guests who are currently checked in (check-out time is null).
     * @return list of checked-in guests
     */
    @Query("SELECT g FROM Guest g WHERE g.checkOutTime IS NULL")
    List<Guest> findAllCheckedInGuests();
    
    /**
     * Finds all guests who have checked out (check-out time is not null).
     * @return list of checked-out guests
     */
    @Query("SELECT g FROM Guest g WHERE g.checkOutTime IS NOT NULL")
    List<Guest> findAllCheckedOutGuests();
    
    /**
     * Finds a guest by name (case-insensitive).
     * @param name the guest name to search for
     * @return list of guests with matching names
     */
    List<Guest> findByNameContainingIgnoreCase(String name);
    
    /**
     * Checks if a guest with the given room number is currently checked in.
     * @param roomNumber the room number to check
     * @return true if guest is checked in, false otherwise
     */
    @Query("SELECT COUNT(g) > 0 FROM Guest g WHERE g.roomNumber = :roomNumber AND g.checkOutTime IS NULL")
    boolean isGuestCheckedInByRoomNumber(String roomNumber);
} 