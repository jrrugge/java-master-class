package com.johnscode.booking;

import com.johnscode.car.Car;
import com.johnscode.car.CarDao;
import com.johnscode.user.User;
import com.johnscode.user.UserDao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

//CarBookingService contains the business logic for making bookings.

public class CarBookingService {


     //UserDao provides access to the seeded users.

    private final UserDao userDao;


     //CarDao provides access to the seeded cars.

    private final CarDao carDao;


    //CarBookingDao stores, retrieves and deletes bookings.

    private final CarBookingDao carBookingDao;


    //Constructor used to create a new carBookingService object instance.

    //This runs whenever we create a new CarBookingService:

    public CarBookingService() {
        this.userDao = new UserDao();
        this.carDao = new CarDao();
        this.carBookingDao = new CarBookingDao();
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


         //Ask UserDao to find the user with the supplied UUID.

         //UserDao returns null when no matching user exists.

        User user = userDao.getUserById(userId);

        if (user == null) {
            throw new IllegalArgumentException(
                    "No user was found with ID: " + userId
            );
        }


         //Ask CarDao to find the car with the supplied UUID.

         //CarDao returns null when no matching car exists.

        Car car = carDao.getCarById(carId);

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

        if (isCarBooked(carId)) {
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

    private boolean isCarBooked(UUID carId) {


        //Retrieve every booking currently stored.

        CarBooking[] bookings =
                carBookingDao.getAllBookings();


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

            boolean activeBooking =
                    currentBooking.getStatus()
                            == BookingStatus.ACTIVE;


             //The car is unavailable when it is the same car
             //AND
             //he existing booking is active

            if (sameCar && activeBooking) {
                return true;
            }
        }


         //No active booking was found for this car
        return false;
    }

    //Returns every booking currently stored.

    public CarBooking[] getAllBookings() {

        /*
         * Delegate the retrieval operation to CarBookingDao.
         */
        return carBookingDao.getAllBookings();
    }

    //Exposing userBookings through CarBookingService
    //Returns all bookings belonging to one user
    public CarBooking[] getBookingsByUserId(UUID userId) {
        //Validate that a user ID was supplied
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        //confirm that the selected user actually exists
        User user = userDao.getUserById(userId);

        if (user == null) {
            throw new IllegalArgumentException(("No user was found with ID: ") + userId);
        }
        //Return only bookings belonging to this user
        return carBookingDao.getBookingsByUserId(userId);
    }

    //Add getAvailableCars Method
     public Car[] getAvailableCars() {
        //should return all cars that dont have an active booking
    //Retrieve all cars from carDao
         Car[] allCars = carDao.getAllCars();
         //arrays have a fixed size we must count how many are currently available

         int availableCarCount = 0;
         //Loop 1 - count every car that does not have an active booking
         for (int i = 0; i < allCars.length; i++) {

             Car currentCar = allCars[i];
             //isCarBooked - return true or false - if the car is not booked count it as available and add to the availableCars array to return to the application
             if(!isCarBooked(currentCar.getId())) {
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

             if(!isCarBooked(currentCar.getId())) {
                 availableCars[availableCarIndex] = currentCar;

                 availableCarIndex++;
             }
         }
         //Return the array containing only available cars
         return availableCars;
    }

    //Add a method that return availabe electric cars
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
        for(int i = 0; i < availableCars.length; i++) {
            Car currentCar = availableCars[i];

            if(currentCar .isElectric()) {
                availableElectricCars[electricCarIndex] = currentCar;
                electricCarIndex++;
            }
        }
        //Return only cars that are available and electric
        return availableElectricCars;
    }


    //deletes a booking
    public boolean deleteBooking(UUID bookingId) {

        //Validate that a booking ID was supplied.

        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }

        //Delegate the deletion operation to CarBookingDao.

        return carBookingDao.deleteBookingById(bookingId);
    }

    //after adding helped methods for option 4 then update CarBookingDao
    //returns every booking currently stored
}
