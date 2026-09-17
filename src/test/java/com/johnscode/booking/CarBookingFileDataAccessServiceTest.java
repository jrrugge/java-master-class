package com.johnscode.booking;

import com.johnscode.car.Brand;
import com.johnscode.car.Car;
import com.johnscode.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarBookingFileDataAccessServiceTest {

    @TempDir
    Path tempDir;

    private CarBookingFileDataAccessService underTest;

    @BeforeEach
    void setUp() {
        String filePath = tempDir.resolve("bookings.dat").toString();
        underTest = new CarBookingFileDataAccessService(filePath);
    }

    @Test
    void itShouldSaveAndRetrieveBooking() {
        UUID bookingId = UUID.randomUUID();
        CarBooking booking = new CarBooking(
                bookingId,
                new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "James"),
                new Car(
                        UUID.fromString("df63c985-4e76-48af-9c8f-a539de9269c4"),
                        "EF78 GHI",
                        new BigDecimal("55.00"),
                        Brand.TOYOTA,
                        false
                ),
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 12),
                new BigDecimal("110.00"),
                BookingStatus.ACTIVE,
                LocalDateTime.now()
        );

        underTest.saveBooking(booking);
        List<CarBooking> bookings = underTest.getBookings();

        assertEquals(1, bookings.size());
        assertEquals(bookingId, bookings.get(0).getId());
    }

    @Test
    void itShouldCancelBookingOnDelete() {
        UUID bookingId = UUID.randomUUID();
        CarBooking booking = new CarBooking(
                bookingId,
                new User(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), "John"),
                new Car(
                        UUID.fromString("a40b7081-3c55-4f87-81d5-cd03c02f0021"),
                        "AB12 CDE",
                        new BigDecimal("75.00"),
                        Brand.TESLA,
                        true
                ),
                LocalDate.of(2026, 9, 15),
                LocalDate.of(2026, 9, 18),
                new BigDecimal("225.00"),
                BookingStatus.ACTIVE,
                LocalDateTime.now()
        );

        underTest.saveBooking(booking);
        underTest.deleteBooking(bookingId);

        CarBooking found = underTest.findBookingById(bookingId);

        assertNotNull(found);
        assertEquals(BookingStatus.CANCELLED, found.getStatus());
    }
}
