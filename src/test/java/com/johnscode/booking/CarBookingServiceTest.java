package com.johnscode.booking;

import com.johnscode.car.Brand;
import com.johnscode.car.Car;
import com.johnscode.car.CarService;
import com.johnscode.user.User;
import com.johnscode.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarBookingServiceTest {

    @Mock
    private CarBookingDao carBookingDao;

    @Mock
    private CarService carService;

    @Mock
    private UserService userService;

    private CarBookingService underTest;

    private UUID userId;
    private UUID carId;
    private User user;
    private Car car;

    @BeforeEach
    void setUp() {
        underTest = new CarBookingService(carBookingDao, carService, userService);

        userId = UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3");
        carId = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
        user = new User(userId, "James");
        car = new Car(carId, "AB12 CDE", new BigDecimal("50.00"), Brand.TESLA, true);
    }

    @Test
    void itShouldGetAllBookings() {
        CarBooking booking = new CarBooking(
                UUID.randomUUID(), user, car,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(3),
                new BigDecimal("100.00"), BookingStatus.ACTIVE, LocalDateTime.now()
        );
        when(carBookingDao.getBookings()).thenReturn(List.of(booking));

        List<CarBooking> result = underTest.getAllBookings();

        assertEquals(1, result.size());
        assertEquals(booking, result.get(0));     // first item is the booking we set up, confirms content match
        verify(carBookingDao).getBookings();
    }

    @Test
    void itShouldThrowWhenCarIsAlreadyBooked() {
        LocalDate startDate = LocalDate.now().plusDays(2);
        LocalDate endDate = LocalDate.now().plusDays(5);

        CarBooking existingBooking = new CarBooking(
                UUID.randomUUID(), user, car,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(4),
                new BigDecimal("150.00"), BookingStatus.ACTIVE, LocalDateTime.now()
        );
        when(userService.getUserById(userId)).thenReturn(user);
        when(carService.findCarById(carId)).thenReturn(car);
        when(carBookingDao.getBookings()).thenReturn(List.of(existingBooking));
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> underTest.bookCar(userId, carId, startDate, endDate)
        );
        assertEquals("The car is already booked and is not available", exception.getMessage());
        verify(carBookingDao, never()).saveBooking(any());
    }

    @Test
    void itShouldBookCar() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(4);  // 3 days → £150 at £50/day

        when(userService.getUserById(userId)).thenReturn(user);
        when(carService.findCarById(carId)).thenReturn(car);
        when(carBookingDao.getBookings()).thenReturn(List.of());

        CarBooking result = underTest.bookCar(userId, carId, startDate, endDate);

        assertEquals(user, result.getUser());
        assertEquals(car, result.getCar());
        assertEquals(BookingStatus.ACTIVE, result.getStatus());
        assertEquals(new BigDecimal("150.00"), result.getPrice());
        verify(carBookingDao).saveBooking(result);
    }

    @Test
    void itShouldDeleteBooking() {
        UUID bookingId = UUID.randomUUID();
        CarBooking booking = new CarBooking(
                bookingId, user, car,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(3),
                new BigDecimal("100.00"), BookingStatus.ACTIVE, LocalDateTime.now()
        );
        when(carBookingDao.findBookingById(bookingId)).thenReturn(booking);

        boolean deleted = underTest.deleteBooking(bookingId);

        assertTrue(deleted);
        verify(carBookingDao).deleteBooking(bookingId);
    }

    @Test
    void itShouldReturnFalseWhenBookingNotFound() {
        UUID bookingId = UUID.randomUUID();
        when(carBookingDao.findBookingById(bookingId)).thenReturn(null);

        boolean deleted = underTest.deleteBooking(bookingId);

        assertFalse(deleted);
        verify(carBookingDao, never()).deleteBooking(bookingId);
    }
}

