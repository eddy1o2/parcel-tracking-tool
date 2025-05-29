package com.hotel.parceltracking.service;

import com.hotel.parceltracking.dto.GuestDto;
import com.hotel.parceltracking.model.Guest;
import com.hotel.parceltracking.repository.GuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GuestService.
 */
@ExtendWith(MockitoExtension.class)
class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private ParcelService parcelService;

    @InjectMocks
    private GuestService guestService;

    private Guest testGuest;
    private GuestDto testGuestDto;

    @BeforeEach
    void setUp() {
        testGuest = new Guest("John Doe", "101", LocalDateTime.now());
        testGuest.setId(1L);

        testGuestDto = new GuestDto("John Doe", "101");
    }

    @Test
    void checkInGuest_Success() {
        // Given
        when(guestRepository.isGuestCheckedInByRoomNumber("101")).thenReturn(false);
        when(guestRepository.save(any(Guest.class))).thenReturn(testGuest);

        // When
        GuestDto result = guestService.checkInGuest(testGuestDto);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("101", result.getRoomNumber());
        assertTrue(result.isCheckedIn());
        verify(guestRepository).isGuestCheckedInByRoomNumber("101");
        verify(guestRepository).save(any(Guest.class));
    }

    @Test
    void checkInGuest_RoomAlreadyOccupied() {
        // Given
        when(guestRepository.isGuestCheckedInByRoomNumber("101")).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> guestService.checkInGuest(testGuestDto)
        );
        assertEquals("Room 101 is already occupied", exception.getMessage());
        verify(guestRepository).isGuestCheckedInByRoomNumber("101");
        verify(guestRepository, never()).save(any(Guest.class));
    }

    @Test
    void checkOutGuest_Success() {
        // Given
        when(guestRepository.findById(1L)).thenReturn(Optional.of(testGuest));
        when(guestRepository.save(any(Guest.class))).thenReturn(testGuest);

        // When
        GuestDto result = guestService.checkOutGuest(1L);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("101", result.getRoomNumber());
        verify(guestRepository).findById(1L);
        verify(guestRepository).save(any(Guest.class));
    }

    @Test
    void checkOutGuest_GuestNotFound() {
        // Given
        when(guestRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> guestService.checkOutGuest(1L)
        );
        assertEquals("Guest not found with ID: 1", exception.getMessage());
        verify(guestRepository).findById(1L);
        verify(guestRepository, never()).save(any(Guest.class));
    }

    @Test
    void checkOutGuest_AlreadyCheckedOut() {
        // Given
        testGuest.checkOut(); // Guest is already checked out
        when(guestRepository.findById(1L)).thenReturn(Optional.of(testGuest));

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> guestService.checkOutGuest(1L)
        );
        assertEquals("Guest is already checked out", exception.getMessage());
        verify(guestRepository).findById(1L);
        verify(guestRepository, never()).save(any(Guest.class));
    }

    @Test
    void getAllCheckedInGuests_Success() {
        // Given
        Guest guest1 = new Guest("John Doe", "101", LocalDateTime.now());
        guest1.setId(1L);
        Guest guest2 = new Guest("Jane Smith", "102", LocalDateTime.now());
        guest2.setId(2L);
        List<Guest> checkedInGuests = Arrays.asList(guest1, guest2);

        when(guestRepository.findAllCheckedInGuests()).thenReturn(checkedInGuests);

        // When
        List<GuestDto> result = guestService.getAllCheckedInGuests();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
        verify(guestRepository).findAllCheckedInGuests();
    }

    @Test
    void getGuestById_Success() {
        // Given
        when(guestRepository.findById(1L)).thenReturn(Optional.of(testGuest));

        // When
        GuestDto result = guestService.getGuestById(1L);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("101", result.getRoomNumber());
        verify(guestRepository).findById(1L);
    }

    @Test
    void getGuestById_NotFound() {
        // Given
        when(guestRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> guestService.getGuestById(1L)
        );
        assertEquals("Guest not found with ID: 1", exception.getMessage());
        verify(guestRepository).findById(1L);
    }

    @Test
    void isGuestCheckedIn_True() {
        // Given
        when(guestRepository.isGuestCheckedInByRoomNumber("101")).thenReturn(true);

        // When
        boolean result = guestService.isGuestCheckedIn("101");

        // Then
        assertTrue(result);
        verify(guestRepository).isGuestCheckedInByRoomNumber("101");
    }

    @Test
    void isGuestCheckedIn_False() {
        // Given
        when(guestRepository.isGuestCheckedInByRoomNumber("101")).thenReturn(false);

        // When
        boolean result = guestService.isGuestCheckedIn("101");

        // Then
        assertFalse(result);
        verify(guestRepository).isGuestCheckedInByRoomNumber("101");
    }

    @Test
    void getAllGuests_Success() {
        // Given
        List<Guest> guests = List.of(testGuest);
        when(guestRepository.findAll()).thenReturn(guests);

        // When
        List<GuestDto> result = guestService.getAllGuests();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("101", result.get(0).getRoomNumber());
        verify(guestRepository).findAll();
    }
} 