package com.johnscode;

import com.johnscode.booking.CarBooking;
import com.johnscode.booking.CarBookingDao;
import com.johnscode.booking.CarBookingFileDataAccessService;
import com.johnscode.booking.CarBookingService;
import com.johnscode.car.Car;
import com.johnscode.car.CarArrayDataAccessService;
import com.johnscode.car.CarDao;
import com.johnscode.car.CarService;
import com.johnscode.user.User;
import com.johnscode.user.UserArrayDataAccessService;
import com.johnscode.user.UserDao;
import com.johnscode.user.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CarBookingDao carBookingDao = new CarBookingFileDataAccessService("bookings.dat");
        // CarBookingDao carBookingDao = new CarBookingArrayDataAccessService();

        CarDao carDao = new CarArrayDataAccessService();
        CarService carService = new CarService(carDao);

        UserDao userDao = new UserArrayDataAccessService();
        UserService userService = new UserService(userDao);

        CarBookingService carBookingService = new CarBookingService(
                carBookingDao, carService, userService
        );

        boolean running = true;
        while (running) {
            displayMenu();
            int selectedOption = readMenuOption(scanner);

            switch (selectedOption) {
                case 1:
                    bookCar(scanner, userService, carBookingService);
                    break;
                case 2:
                    deleteBooking(scanner, carBookingService);
                    break;
                case 3:
                    displayUserBookings(scanner, userService, carBookingService);
                    break;
                case 4:
                    displayAllBookings(carBookingService);
                    break;
                case 5:
                    displayAvailableCars(carBookingService);
                    break;
                case 6:
                    displayAvailableElectricCars(carBookingService);
                    break;
                case 7:
                    displayAllUsers(userService);
                    break;
                case 8:
                    running = false;
                    System.out.println("Thank you for using the car booking system");
                    break;
                default:
                    System.out.println("Invalid option. Please select a number from 1 to 8 only from Menu provided");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("===================================");
        System.out.println("       CAR BOOKING SYSTEM          ");
        System.out.println("===================================");
        System.out.println("1. Book a car");
        System.out.println("2. Delete booking");
        System.out.println("3. View user bookings");
        System.out.println("4. View all Bookings");
        System.out.println("5. View all available cars");
        System.out.println("6. View all available electric cars");
        System.out.println("7. View all users");
        System.out.println("8. Exit");
        System.out.println("===================================");
    }

    private static int readMenuOption(Scanner scanner) {
        System.out.println("Select an option: ");

        String input = scanner.nextLine().trim();

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            System.out.println("Invalid input. Please enter a whole number");
            return -1;
        }
    }

    private static void displayAllUsers(UserService userService) {
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found");
            return;
        }

        System.out.println();
        System.out.println("ALL USERS");
        System.out.println("---------------------------");

        for (int i = 0; i < users.size(); i++) {
            User currentUser = users.get(i);
            System.out.println("User number: " + (i + 1));
            System.out.println("ID: " + currentUser.id());
            System.out.println("Name: " + currentUser.name());
            System.out.println("--------------------------------");
        }
    }

    private static void displayAvailableCars(CarBookingService carBookingService) {
        List<Car> availableCars = carBookingService.getAvailableCars();

        if (availableCars.isEmpty()) {
            System.out.println("There are currently no available cars");
            return;
        }

        System.out.println();
        System.out.println("AVAILABLE CARS");
        displayCars(availableCars);
    }

    private static void displayAvailableElectricCars(CarBookingService carBookingService) {
        List<Car> electricCars = carBookingService.getAvailableElectricCars();

        if (electricCars.isEmpty()) {
            System.out.println("The are currently no available electric cars.");
            return;
        }

        System.out.println();
        System.out.println("AVAILABLE ELECTRIC CARS");
        displayCars(electricCars);
    }

    private static void displayCars(List<Car> cars) {
        System.out.println("---------------------------------");

        for (int i = 0; i < cars.size(); i++) {
            Car currentCar = cars.get(i);

            System.out.println("Car number: " + (i + 1));
            System.out.println("ID: " + currentCar.id());
            System.out.println("Brand: " + currentCar.brand());
            System.out.println("Registration: " + currentCar.regNumber());
            System.out.println("Daily Price: " + currentCar.rentalPricePerDay());
            System.out.println("Electric: " + currentCar.electric());
            System.out.println("--------------------------------------------------");
        }
    }

    private static void displayAllBookings(CarBookingService carBookingService) {
        List<CarBooking> bookings = carBookingService.getAllBookings();

        System.out.println();
        System.out.println("ALL BOOKINGS");
        displayBookings(bookings);
    }

    private static void displayBookings(List<CarBooking> bookings) {
        if (bookings.isEmpty()) {
            System.out.println("No bookings were found.");
            return;
        }

        System.out.println("------------------------------------------");

        for (int i = 0; i < bookings.size(); i++) {
            CarBooking currentBooking = bookings.get(i);

            System.out.println("Booking number: " + (i + 1));
            System.out.println("Booking ID: " + currentBooking.getId());
            System.out.println("User: " + currentBooking.getUser().name());
            System.out.println("User ID: " + currentBooking.getUser().id());
            System.out.println("Car: " + currentBooking.getCar().brand());
            System.out.println("Car ID: " + currentBooking.getCar().id());
            System.out.println("Registration: " + currentBooking.getCar().regNumber());
            System.out.println("Start date: " + currentBooking.getStartDate());
            System.out.println("End date: " + currentBooking.getEndDate());
            System.out.println("Total price: £" + currentBooking.getPrice());
            System.out.println("Status: " + currentBooking.getStatus());
            System.out.println("Booked at: " + currentBooking.getBookedAt());
            System.out.println("-----------------------------------------------------");
        }
    }

    private static void displayUserBookings(
            Scanner scanner,
            UserService userService,
            CarBookingService carBookingService
    ) {
        displayAllUsers(userService);
        System.out.println("Enter user ID: ");

        String userIdInput = scanner.nextLine().trim();

        try {
            UUID userId = UUID.fromString(userIdInput);
            List<CarBooking> userBookings = carBookingService.getBookingsByUserId(userId);

            System.out.println();
            System.out.println("USER BOOKINGS");
            displayBookings(userBookings);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to retrieve bookings: " + e.getMessage());
        }
    }

    private static void bookCar(
            Scanner scanner,
            UserService userService,
            CarBookingService carBookingService
    ) {
        displayAllUsers(userService);
        System.out.println();

        displayAvailableCars(carBookingService);
        System.out.println();

        try {
            System.out.println("Enter user ID: ");
            String userIdInput = scanner.nextLine().trim();
            UUID userId = UUID.fromString(userIdInput);

            System.out.println("Enter the car ID: ");
            String carIdInput = scanner.nextLine().trim();
            UUID carId = UUID.fromString(carIdInput);

            System.out.println("Enter the start date: ");
            String startDateInput = scanner.nextLine().trim();
            LocalDate startDate = LocalDate.parse(startDateInput);

            System.out.println("Enter the end date: ");
            String endDateInput = scanner.nextLine().trim();
            LocalDate endDate = LocalDate.parse(endDateInput);

            CarBooking booking = carBookingService.bookCar(userId, carId, startDate, endDate);

            System.out.println();
            System.out.println("BOOKING SUCCESSFUL");
            System.out.println("---------------------------------");
            System.out.println("Booking ID: " + booking.getId());
            System.out.println("User: " + booking.getUser().name());
            System.out.println("Car: " + booking.getCar().brand());
            System.out.println("Registration: " + booking.getCar().regNumber());
            System.out.println("Start date: " + booking.getStartDate());
            System.out.println("End date: " + booking.getEndDate());
            System.out.println("Total price: £" + booking.getPrice());
            System.out.println("Status: " + booking.getStatus());
            System.out.println("----------------------------------------");
        } catch (DateTimeParseException e) {
            System.out.println("Unable to create booking: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to create booking; Enter correct details: " + e.getMessage());
        }
    }

    private static void deleteBooking(Scanner scanner, CarBookingService carBookingService) {
        List<CarBooking> bookings = carBookingService.getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("There are no bookings to delete.");
            return;
        }

        System.out.println();
        System.out.println("CURRENT BOOKINGS");
        displayBookings(bookings);

        System.out.println("Enter booking ID you want to delete: ");
        String bookingIdInput = scanner.nextLine().trim();

        try {
            UUID bookingId = UUID.fromString(bookingIdInput);
            boolean deleted = carBookingService.deleteBooking(bookingId);

            if (deleted) {
                System.out.println("Booking deleted successfully.");
            } else {
                System.out.println("No booking was found with that ID.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid booking ID. Please enter a valid UUID.");
        }
    }
}
