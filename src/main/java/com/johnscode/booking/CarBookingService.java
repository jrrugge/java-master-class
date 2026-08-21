package com.johnscode.booking;

import com.johnscode.car.Car;
import com.johnscode.car.CarService;
import com.johnscode.user.User;
import com.johnscode.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


//CarBookingService contains the business logic for making bookings.

public class CarBookingService {

    //UserService provides user-related operations
    private final UserService userService;
    //CarService provides car-related operations
    private final CarService carService;
    //CarBookingDao stores, retrieves and updates bookings
    private final CarBookingDao carBookingDao;


    // Phase 2 Part C - Main wires all three dependencies in, nothing created with "new" in here anymore
    public CarBookingService(CarBookingDao carBookingDao,
                             CarService carService,
                             UserService userService) {
        this.carBookingDao = carBookingDao;
        this.carService = carService;
        this.userService = userService;
    }


    //This method Creates and saves a new car booking.
    //It returns the newly created CarBooking object


    public CarBooking bookCar(
            UUID userId,
            UUID carId,
            LocalDate startDate,
            LocalDate endDate
    ) {


        //Validate that a user ID was supplied.
        if (userId == null) {
            throw new IllegalArgumentException(
                    "User ID cannot be null"
            );
        }


        //Validate that a car ID was supplied.

        if (carId == null) {
            throw new IllegalArgumentException(
                    "Car ID cannot be null"
            );
        }


        //Validate that both booking dates were supplied.

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Start date and end date are required"
            );
        }


        //Ask UserService to find the user with the supplied UUID.

        //UserService returns null when no matching user exists.

        User user = userService.getUserById(userId);

        if (user == null) {
            throw new IllegalArgumentException(
                    "No user was found with ID: " + userId
            );
        }


        //Ask CarService to find the car with the supplied UUID.

        //CarServic returns null when no matching car exists.

        Car car = carService.findCarById(carId);

        if (car == null) {
            throw new IllegalArgumentException(
                    "No car was found with ID: " + carId
            );
        }


        //The booking may begin today or in the future.


        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "The start date cannot be in the past"
            );
        }


        //The end date must be later than the start date.

        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException(
                    "The end date must be after the start date"
            );
        }


        //Check whether the selected car already has
        //an active booking and rejects if true.

        if (isCarBooked(carId, startDate, endDate)) {
            throw new IllegalStateException(
                    "The car is already booked and is not available"
            );
        }


        //Calculate the number of rental days.

        long numberOfDays = ChronoUnit.DAYS.between(
                startDate,
                endDate
        );

        //Calculate the total booking price:

        //rental price per day × number of rental days

        //BigDecimal.valueOf converts the number of days
        //into a BigDecimal before multiplication


        BigDecimal totalPrice = car
                .getRentalPricePerDay()
                .multiply(BigDecimal.valueOf(numberOfDays));

        //Create the new CarBooking object.
        //Each new booking receives a newly generated UUID
        //contains the selected user
        //contains the selected car
        //records the booking dates
        //stores the calculated price
        //begins with ACTIVE status
        //records the current date and time

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
        //Ask CarBookingDao to store the new booking.

        carBookingDao.saveBooking(newBooking);

        //Return the created booking to the caller.

        //Main.java can later display its details.

        return newBooking;
    }
    //Checks whether an active booking already exists
    //for the supplied car ID.

    //The method is private because it is only used
    //internally by CarBookingService.

    private boolean isCarBooked(UUID carId, LocalDate startDate, LocalDate endDate) {
        //Retrieve every booking currently stored.

        CarBooking[] bookings = carBookingDao.getBookings();

        //Examine each booking in the array.

        for (int i = 0; i < bookings.length; i++) {
            //Retrieve the booking at the current position.
            CarBooking currentBooking = bookings[i];

            //Check whether the current booking contains
            //the same car that we are trying to book.

            boolean sameCar = currentBooking
                    .getCar()
                    .getId()
                    .equals(carId);
            //Check if existing booking is active.
            boolean activeBooking = currentBooking.getStatus() == BookingStatus.ACTIVE;
            //overlap comparison
            boolean overlappingDates = currentBooking
                    .getStartDate()
                    .isBefore(endDate)
                    && startDate.isBefore(currentBooking.getEndDate());
            //The car is unavailable when it is the same car
            //AND
            //the existing booking is active

            if (sameCar && activeBooking && overlappingDates) {
                return true;
            }
        }


        //No active booking was found for this car
        return false;
    }

    // getAllBookings kept for Main - DAO method is getBookings() on the interface
    public CarBooking[] getAllBookings() {
        return carBookingDao.getBookings();
    }

    // Phase 2 Part A - getBookingsByUserId isn't on the CarBookingDao interface so I moved the
    // filter loops here. DAO gives all bookings, service filters by user id.
    public CarBooking[] getBookingsByUserId(UUID userId) {
        //Validate that a user ID was supplied
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        //confirm that the selected user actually exists
        User user = userService.getUserById(userId);

        if (user == null) {
            throw new IllegalArgumentException(("No user was found with ID: ") + userId);
        }

        CarBooking[] allBookings = carBookingDao.getBookings();
        // filter by user - same two-loop pattern I had in the old DAO class
        int matchingBookingCount = 0;

        for (int i = 0; i < allBookings.length; i++) {

            CarBooking currentBooking = allBookings[i];

            //Get the user stored inside the booking, then compare that users iD with userID
            boolean sameUser = currentBooking.getUser().getId().equals(userId);

            if (sameUser) {
                matchingBookingCount++;
            }
        }

        //Create an array large enough to hold all the matches. If no bookings match, this creates an empty array (new CarBooking)
        CarBooking[] userBookings = new CarBooking[matchingBookingCount];

        //this tracks the next available position in the new userBookings array
        int userBookingIndex = 0;
        //Loop through the bookings again and copy matching bookings into the new array.

        for (int i = 0; i < allBookings.length; i++) {
            CarBooking currentBooking = allBookings[i];
            boolean sameUser = currentBooking.getUser().getId().equals(userId);

            if (sameUser) {
                userBookings[userBookingIndex] = currentBooking;
                userBookingIndex++;
            }
        }
        //Return only the bookings belonging to this user
        return userBookings;
    }

    //Add getAvailableCars Method
    public Car[] getAvailableCars() {
        //Return all cars that do not have an active booking overlapping today
        Car[] allCars = carService.getAllCars();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        int availableCarCount = 0;
        //Loop 1 - count every car not booked today
        for (int i = 0; i < allCars.length; i++) {
            Car currentCar = allCars[i];
            if (!isCarBooked(currentCar.getId(), today, tomorrow)) {
                availableCarCount++;
            }
        }
        //Create Array with exactly enough space for the available cars
        Car[] availableCars = new Car[availableCarCount];

        //Tracks the next empty position in availableCars
        int availableCarIndex = 0;

        //Second Loop - Copy each available car into the new array
        for (int i = 0; i < allCars.length; i++) {
            Car currentCar = allCars[i];

            if (!isCarBooked(currentCar.getId(), today, tomorrow)) {
                availableCars[availableCarIndex] = currentCar;

                availableCarIndex++;
            }
        }
        //Return the array containing only available cars
        return availableCars;
    }

    //Add a method that return available electric cars
    public Car[] getAvailableElectricCars() {
        //reuse get available cars

        Car[] availableCars = getAvailableCars();

        int electricCarCount = 0;

        for (int i = 0; i < availableCars.length; i++) {
            Car currentCar = availableCars[i];

            if (currentCar.isElectric()) {
                electricCarCount++;
            }
        }
        //Array of the exact required size
        Car[] availableElectricCars = new Car[electricCarCount];

        int electricCarIndex = 0; //Tracks the next empty position in the new array

        //Copy the electric cars into the new array
        for (int i = 0; i < availableCars.length; i++) {
            Car currentCar = availableCars[i];

            if (currentCar.isElectric()) {
                availableElectricCars[electricCarIndex] = currentCar;
                electricCarIndex++;
            }
        }
        //Return only cars that are available and electric
        return availableElectricCars;
    }


    // Phase 2 Part A - deleteBooking on the DAO is void now, so service checks if booking
    // exists first and returns true/false for Main (same behaviour as before for the menu)
    public boolean deleteBooking(UUID bookingId) {

        //Validate that a booking ID was supplied.

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
