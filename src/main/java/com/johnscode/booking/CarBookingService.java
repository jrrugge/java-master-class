package com.johnscode.booking;

import com.johnscode.car.Car;
import com.johnscode.car.CarService;
import com.johnscode.user.User;
import com.johnscode.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private boolean isCarBooked(UUID carId, LocalDate startDate, LocalDate endDate) {
        List<CarBooking> bookings = carBookingDao.getBookings();

        for (CarBooking currentBooking : bookings) {
            boolean sameCar = currentBooking.getCar().getId().equals(carId);
            boolean activeBooking = currentBooking.getStatus() == BookingStatus.ACTIVE;
            boolean overlappingDates = currentBooking.getStartDate().isBefore(endDate)
                    && startDate.isBefore(currentBooking.getEndDate());

            if (sameCar && activeBooking && overlappingDates) {
                return true;
            }
        }

        return false;
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

        List<CarBooking> allBookings = carBookingDao.getBookings();
        List<CarBooking> userBookings = new ArrayList<>();

        for (CarBooking currentBooking : allBookings) {
            if (currentBooking.getUser().getId().equals(userId)) {
                userBookings.add(currentBooking);
            }
        }

        return userBookings;
    }

    public List<Car> getAvailableCars() {
        List<Car> allCars = carService.getAllCars();
        List<Car> availableCars = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        for (Car currentCar : allCars) {
            if (!isCarBooked(currentCar.getId(), today, tomorrow)) {
                availableCars.add(currentCar);
            }
        }

        return availableCars;
    }

    public List<Car> getAvailableElectricCars() {
        List<Car> availableCars = getAvailableCars();
        List<Car> availableElectricCars = new ArrayList<>();

        for (Car currentCar : availableCars) {
            if (currentCar.isElectric()) {
                availableElectricCars.add(currentCar);
            }
        }

        return availableElectricCars;
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
