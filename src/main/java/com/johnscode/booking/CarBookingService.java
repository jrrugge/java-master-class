package com.johnscode.booking;

import com.johnscode.car.Car;
import com.johnscode.car.CarService;
import com.johnscode.user.User;
import com.johnscode.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CarBookingService {

    private final UserService userService;
    private final CarService carService;
    private final CarBookingDao carBookingDao;

    public CarBookingService(
            CarBookingDao carBookingDao,
            CarService carService,
            UserService userService
    ) {
        this.carBookingDao = carBookingDao;
        this.carService = carService;
        this.userService = userService;
    }

    public CarBooking bookCar(UUID userId, UUID carId, LocalDate startDate, LocalDate endDate) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (carId == null) {
            throw new IllegalArgumentException("Car ID cannot be null");
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("No user was found with ID: " + userId);
        }

        Car car = carService.findCarById(carId);
        if (car == null) {
            throw new IllegalArgumentException("No car was found with ID: " + carId);
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("The start date cannot be in the past");
        }

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("The end date must be after the start date");
        }

        if (isCarBooked(carId, startDate, endDate)) {
            throw new IllegalStateException("The car is already booked and is not available");
        }

        long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal totalPrice = car.getRentalPricePerDay().multiply(BigDecimal.valueOf(numberOfDays));

        CarBooking newBooking = new CarBooking(
                UUID.randomUUID(),
                user,
                car,
                startDate,
                endDate,
                totalPrice,
                BookingStatus.ACTIVE,
                LocalDateTime.now()
        );

        carBookingDao.saveBooking(newBooking);
        return newBooking;
    }

    // Phase 4 - anyMatch checks if at least one active booking overlaps these dates
    private boolean isCarBooked(UUID carId, LocalDate startDate, LocalDate endDate) {
        return carBookingDao.getBookings().stream()
                .anyMatch(booking ->
                        booking.getCar().getId().equals(carId)
                                && booking.getStatus() == BookingStatus.ACTIVE
                                && booking.getStartDate().isBefore(endDate)
                                && startDate.isBefore(booking.getEndDate())
                );
    }

    public List<CarBooking> getAllBookings() {
        return carBookingDao.getBookings();
    }

    public List<CarBooking> getBookingsByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("No user was found with ID: " + userId);
        }

        return carBookingDao.getBookings().stream()
                .filter(booking -> booking.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Car> getAvailableCars() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        return carService.getAllCars().stream()
                .filter(car -> !isCarBooked(car.getId(), today, tomorrow))
                .collect(Collectors.toList());
    }

    public List<Car> getAvailableElectricCars() {
        return getAvailableCars().stream()
                .filter(Car::isElectric)
                .collect(Collectors.toList());
    }

    public boolean deleteBooking(UUID bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }

        CarBooking booking = carBookingDao.findBookingById(bookingId);
        if (booking == null) {
            return false;
        }

        carBookingDao.deleteBooking(bookingId);
        return true;
    }
}
