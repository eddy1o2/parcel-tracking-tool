package com.hotel.parceltracking.service;

import com.hotel.parceltracking.dto.ParcelDto;
import com.hotel.parceltracking.model.Guest;
import com.hotel.parceltracking.model.Parcel;
import com.hotel.parceltracking.repository.GuestRepository;
import com.hotel.parceltracking.repository.ParcelRepository;
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
 * Unit tests for ParcelService.
 */
@ExtendWith(MockitoExtension.class)
class ParcelServiceTest {

    @Mock
    private ParcelRepository parcelRepository;

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private ParcelService parcelService;

    private Guest testGuest;
    private Parcel testParcel;
    private ParcelDto testParcelDto;

    @BeforeEach
    void setUp() {
        testGuest = new Guest("John Doe", "101", LocalDateTime.now());
        testGuest.setId(1L);

        testParcel = new Parcel("TRK123", "Amazon", "Package", testGuest);
        testParcel.setId(1L);

        testParcelDto = new ParcelDto("TRK123", "Amazon", "Package", 1L);
    }

    @Test
    void acceptParcel_Success() {
        // Given
        when(guestRepository.findById(1L)).thenReturn(Optional.of(testGuest));
        when(parcelRepository.findByTrackingNumber("TRK123")).thenReturn(Optional.empty());
        when(parcelRepository.save(any(Parcel.class))).thenReturn(testParcel);

        // When
        ParcelDto result = parcelService.acceptParcel(testParcelDto);

        // Then
        assertNotNull(result);
        assertEquals("TRK123", result.getTrackingNumber());
        assertEquals("Amazon", result.getSender());
        assertEquals("Package", result.getDescription());
        assertEquals(1L, result.getGuestId());
        verify(guestRepository).findById(1L);
        verify(parcelRepository).findByTrackingNumber("TRK123");
        verify(parcelRepository).save(any(Parcel.class));
    }

    @Test
    void acceptParcel_GuestNotFound() {
        // Given
        when(guestRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parcelService.acceptParcel(testParcelDto)
        );
        assertEquals("Guest not found with ID: 1", exception.getMessage());
        verify(guestRepository).findById(1L);
        verify(parcelRepository, never()).save(any(Parcel.class));
    }

    @Test
    void acceptParcel_GuestNotCheckedIn() {
        // Given
        testGuest.checkOut(); // Guest is not checked in
        when(guestRepository.findById(1L)).thenReturn(Optional.of(testGuest));

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parcelService.acceptParcel(testParcelDto)
        );
        assertEquals("Cannot accept parcel for guest who is not checked in: John Doe", exception.getMessage());
        verify(guestRepository).findById(1L);
        verify(parcelRepository, never()).save(any(Parcel.class));
    }

    @Test
    void acceptParcel_DuplicateTrackingNumber() {
        // Given
        when(guestRepository.findById(1L)).thenReturn(Optional.of(testGuest));
        when(parcelRepository.findByTrackingNumber("TRK123")).thenReturn(Optional.of(testParcel));

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parcelService.acceptParcel(testParcelDto)
        );
        assertEquals("Parcel with tracking number TRK123 already exists", exception.getMessage());
        verify(guestRepository).findById(1L);
        verify(parcelRepository).findByTrackingNumber("TRK123");
        verify(parcelRepository, never()).save(any(Parcel.class));
    }

    @Test
    void collectParcel_Success() {
        // Given
        when(parcelRepository.findById(1L)).thenReturn(Optional.of(testParcel));
        when(parcelRepository.save(any(Parcel.class))).thenReturn(testParcel);

        // When
        ParcelDto result = parcelService.collectParcel(1L);

        // Then
        assertNotNull(result);
        assertEquals("TRK123", result.getTrackingNumber());
        verify(parcelRepository).findById(1L);
        verify(parcelRepository).save(any(Parcel.class));
    }

    @Test
    void collectParcel_ParcelNotFound() {
        // Given
        when(parcelRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parcelService.collectParcel(1L)
        );
        assertEquals("Parcel not found with ID: 1", exception.getMessage());
        verify(parcelRepository).findById(1L);
        verify(parcelRepository, never()).save(any(Parcel.class));
    }

    @Test
    void collectParcel_AlreadyCollected() {
        // Given
        testParcel.markAsCollected(); // Parcel is already collected
        when(parcelRepository.findById(1L)).thenReturn(Optional.of(testParcel));

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parcelService.collectParcel(1L)
        );
        assertEquals("Parcel is already collected", exception.getMessage());
        verify(parcelRepository).findById(1L);
        verify(parcelRepository, never()).save(any(Parcel.class));
    }

    @Test
    void collectParcelByTrackingNumber_Success() {
        // Given
        when(parcelRepository.findByTrackingNumber("TRK123")).thenReturn(Optional.of(testParcel));
        when(parcelRepository.findById(1L)).thenReturn(Optional.of(testParcel));
        when(parcelRepository.save(any(Parcel.class))).thenReturn(testParcel);

        // When
        ParcelDto result = parcelService.collectParcelByTrackingNumber("TRK123");

        // Then
        assertNotNull(result);
        assertEquals("TRK123", result.getTrackingNumber());
        verify(parcelRepository).findByTrackingNumber("TRK123");
        verify(parcelRepository).findById(1L);
        verify(parcelRepository).save(any(Parcel.class));
    }

    @Test
    void getAvailableParcelsForGuest_Success() {
        // Given
        List<Parcel> uncollectedParcels = Arrays.asList(testParcel);
        when(parcelRepository.findUncollectedParcelsByGuestId(1L)).thenReturn(uncollectedParcels);

        // When
        List<ParcelDto> result = parcelService.getAvailableParcelsForGuest(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TRK123", result.get(0).getTrackingNumber());
        verify(parcelRepository).findUncollectedParcelsByGuestId(1L);
    }

    @Test
    void getAllUncollectedParcels_Success() {
        // Given
        List<Parcel> uncollectedParcels = Arrays.asList(testParcel);
        when(parcelRepository.findAllUncollectedParcels()).thenReturn(uncollectedParcels);

        // When
        List<ParcelDto> result = parcelService.getAllUncollectedParcels();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TRK123", result.get(0).getTrackingNumber());
        verify(parcelRepository).findAllUncollectedParcels();
    }

    @Test
    void getParcelByTrackingNumber_Success() {
        // Given
        when(parcelRepository.findByTrackingNumber("TRK123")).thenReturn(Optional.of(testParcel));

        // When
        ParcelDto result = parcelService.getParcelByTrackingNumber("TRK123");

        // Then
        assertNotNull(result);
        assertEquals("TRK123", result.getTrackingNumber());
        assertEquals("Amazon", result.getSender());
        verify(parcelRepository).findByTrackingNumber("TRK123");
    }

    @Test
    void getParcelByTrackingNumber_NotFound() {
        // Given
        when(parcelRepository.findByTrackingNumber("TRK123")).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parcelService.getParcelByTrackingNumber("TRK123")
        );
        assertEquals("Parcel not found with tracking number: TRK123", exception.getMessage());
        verify(parcelRepository).findByTrackingNumber("TRK123");
    }
} 