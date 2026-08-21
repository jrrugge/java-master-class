package com.johnscode;

import com.johnscode.car.CarDao;
import com.johnscode.car.CarArrayDataAccessService;
import com.johnscode.car.CarService;
import com.johnscode.user.UserDao;
import com.johnscode.user.UserArrayDataAccessService;
import com.johnscode.booking.CarBookingArrayDataAccessService;
import com.johnscode.booking.CarBookingFileDataAccessService;

import com.johnscode.booking.CarBookingDao;
import com.johnscode.booking.CarBookingService;
import com.johnscode.user.User;
import com.johnscode.user.UserService;
import com.johnscode.booking.CarBooking; //4

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.UUID;

import java.sql.SQLOutput;
import java.util.UUID;

import java.math.BigDecimal;

import com.johnscode.car.Car;
import com.johnscode.booking.CarBookingService;

import java.util.Scanner;

// TODO 1. create a new branch called initial-implementation
// TODO 2. create a package with your name. i.e com.franco and move this file inside the new package
// TODO 3. implement https://amigoscode.com/learn/java-cli-build/lectures/3a83ecf3-e837-4ae5-85a8-f8ae3f60f7f5
//Main.Java -> Service Class Layer -> DOA classes Layer -> POJO's / domain objects (User, Car, Carbooking, Brand, BookingStatus)
//POJO's describe the real world objects
//DOA - Data Access Objects - Gives the service access to the stored information
//This file (MAIN) will have the logic to interact with the user via CLI for booking the desired car.
//The service class performs the desired function or business logic

//Main is the entry point of the application - the doorway
//Starting with the view menu options first
public class Main {

    public static void main(String[] args) {
        //System.out.println("Java Master Class");

        //Create only one Scanner app and reuse it through the menu loop
        Scanner scanner = new Scanner(System.in);
        //Note main comms with services not directly with DAO - refer to the architecture

        // Phase 2 Part C - dependency injection. I build the DAOs here, pass them into services.
        // Swapping the two lines below switches bookings between file storage and in-memory array.
        CarBookingDao carBookingDao = new CarBookingFileDataAccessService("bookings.dat");
        // CarBookingDao carBookingDao = new CarBookingArrayDataAccessService();

        CarDao carDao = new CarArrayDataAccessService();
        CarService carService = new CarService(carDao);

        UserDao userDao = new UserArrayDataAccessService();
        UserService userService = new UserService(userDao);

        CarBookingService carBookingService = new CarBookingService(
                carBookingDao, carService, userService
        );

        //Variable to control the menu loop
        //The application continues while running is true

        boolean running = true;
        while (running) {
            displayMenu(); //Display all menu options
            int selectedOption = readMenuOption(scanner);

            //Decide action based on selectedOption
            switch (selectedOption) {
                case 1:
                    bookCar(scanner, userService, carBookingService);
                    break;

                case 2:
                    deleteBooking(scanner, carBookingService);
                    break;
                //Delete an existing booking - last implementation after create booking, started with view implementations first
                case 3: //Ask for user and display all bookings belonging to that user
                    displayUserBookings(scanner, userService, carBookingService);
                    break;
                // break; third from last implementation
                case 4:   //4th implementation
                    displayAllBookings(carBookingService);
                    break;
                //System.out.println("View all bookings will be connected later");
                case 5:
                    displayAvailableCars(carBookingService); //Retrieve and display all currently available cars
                    break;
                case 6: //Retrieve and display all cars that are both electric and available
                    displayAvailableElectricCars(carBookingService);
                    break;
                case 7:
                    //System.out.println("View all users");
                    displayAllUsers(userService);
                    break;
                case 8: //Stop the while loop
                    running = false;
                    System.out.println("Thank you for using the car booking system");
                    break;
                default:  //Handle values outside the expected values (Input 1 to 8)
                    System.out.println("Invalid option. Please select a number from 1 to 8 only from Menu provided");
            }
            System.out.println();
        }
        scanner.close(); //Close scanner when the application stops
    }

    //Manu Display
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

    //Reads and validates the menu selection controlled by the switch statement above
    //returns -1 when the entered text is not a number
    private static int readMenuOption(Scanner scanner) {
        System.out.println("Select an option: ");

        String input = scanner.nextLine().trim();

        try {
            //convert the text into an integer
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            //runs when text entered cannot be converted into a whole number
            System.out.println("Invalid input. Please enter a whole number");
            return -1;
        }
    }

    //Display all the users stored in the application
    private static void displayAllUsers(UserService userService) {
        //Delegate userService to fetch all users
        User[] users = userService.getAllUsers();
        //Handle empty  user array
        if (users.length == 0) {
            System.out.println("No users found");
            return;
        }

        System.out.println();
        System.out.println("ALL USERS");
        System.out.println("---------------------------");

        //Display Each user
        for (int i = 0; i < users.length; i++) {

            User currentUser = users[i];
            System.out.println("User number: " + (i + 1));
            System.out.println("ID: " + currentUser.getId());
            System.out.println("Name: " + currentUser.getName());
            System.out.println("--------------------------------");
        }
    }

    //-----Display Cars that currently have no active booking
    private static void displayAvailableCars(CarBookingService carBookingService) {
        //Request booking service to determine availability
        Car[] availableCars = carBookingService.getAvailableCars();

        if (availableCars.length == 0) {
            System.out.println("There are currently no available cars");
            return;
        }
        System.out.println();
        System.out.println("AVAILABLE CARS");
        displayCars(availableCars); //Reuse the common car display method
    }

    //Display cars that are both available abd electric
    private static void displayAvailableElectricCars(CarBookingService carBookingService) {
        Car[] electricCars = carBookingService.getAvailableElectricCars();

        if (electricCars.length == 0) {
            System.out.println("The are currently no available electric cars.");
            return;
        }
        System.out.println();
        System.out.println("AVAILABLE ELECTRIC CARS");

        displayCars(electricCars); //Reuse tge common car display methos instead of writing loop code
    }
    //Display every car in the supplied array
    //both available car menu options use this method

    private static void displayCars(Car[] cars) {
        System.out.println("---------------------------------");

        for (int i = 0; i < cars.length; i++) {
            Car currentCar = cars[i];

            System.out.println("Car number: " + (i + 1));
            System.out.println("ID: " + currentCar.getId());
            System.out.println("Brand: " + currentCar.getBrand());
            System.out.println("Registration: " + currentCar.getRegNumber());
            System.out.println("Daily Price: " + currentCar.getRentalPricePerDay());
            System.out.println("Electric: " + currentCar.isElectric());
            System.out.println("--------------------------------------------------");
        }
    }

    private static void displayAllBookings(CarBookingService carBookingService) {
        //Ask the service for every stored service
        CarBooking[] bookings = carBookingService.getAllBookings();

        System.out.println();
        System.out.println("ALL BOOKINGS");
        //pass the array to a separate method responsible for display booking details.
        displayBookings(bookings);
    }

    private static void displayBookings(CarBooking[] bookings) {
        if (bookings.length == 0) {
            System.out.println("No bookings were found.");
            return;
        }
        System.out.println("------------------------------------------");

        //Loop thorugh every booking in the array
        for (int i = 0; i < bookings.length; i++) {
            //retrieve the booking at the current position
            CarBooking currentBooking = bookings[i];

            //Display the booking details
            System.out.println("Booking number: " + (i + 1));
            System.out.println("Booking ID: " + currentBooking.getId());
            System.out.println("User: " + currentBooking.getUser().getName());
            System.out.println("User ID: " + currentBooking.getUser().getId());
            System.out.println("Car: " + currentBooking.getCar().getBrand());
            System.out.println("Car ID: " + currentBooking.getCar().getId());
            System.out.println("Registration: " + currentBooking.getCar().getRegNumber());
            System.out.println("Start date: " + currentBooking.getStartDate());
            System.out.println("End date: " + currentBooking.getEndDate());
            System.out.println("Total price: £" + currentBooking.getPrice());
            System.out.println("Status: " + currentBooking.getStatus());
            System.out.println("Booked at: " + currentBooking.getBookedAt());
            System.out.println("-----------------------------------------------------");
        }

    }

    //Displays all bookings belonging to one selected user
    private static void displayUserBookings(Scanner scanner, UserService userService, CarBookingService carBookingService) {
        //Show all users first so the operator can copy the UUID of the required user
        displayAllUsers(userService);
        System.out.println("Enter user ID: ");

        String userIdInput = scanner.nextLine().trim(); //Read uuid as text

        try {
            //convert entered text into a uuid object
            UUID userID = UUID.fromString(userIdInput);
            //fetch bookings belonging to the user through carBookingService
            CarBooking[] userBookings = carBookingService.getBookingsByUserId(userID);

            System.out.println();
            System.out.println("USER BOOKINGS");

            displayBookings(userBookings);
            // if(userBookings == null) {
            //   System.out.println("The are no current bookings for user: " + userID);
            //}
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to retrieve bookings: " + e.getMessage());
        }
    }

    //bookCar helper method - handles CLI process for creating a new car booking
    private static void bookCar(Scanner scanner, UserService userService, CarBookingService carBookingService) {
        //First show the users. Operator Needs a userID to create the booking

        displayAllUsers(userService);
        System.out.println();

        //Then show cars that are currently available
        displayAvailableCars(carBookingService);
        System.out.println();

        try {
            //Ask for the user ID
            System.out.println("Enter user ID: ");
            String userIdInput = scanner.nextLine().trim();

            //convert the string into uuid object
            UUID userId = UUID.fromString(userIdInput);

            //Ask for the car ID
            System.out.println("Enter the car ID: ");

            String carIdInput = scanner.nextLine().trim();

            UUID carID = UUID.fromString(carIdInput);

            //Ask user for start date
            System.out.println("Enter the start date: ");

            String startDateInput = scanner.nextLine().trim();

            LocalDate startDate = LocalDate.parse(startDateInput);

            //Ask user for booking end date
            System.out.println("Enter the end date: ");

            String endDateInput = scanner.nextLine().trim();

            LocalDate endDate = LocalDate.parse(endDateInput);

            //Pass all the information to the carbooking service
            CarBooking booking = carBookingService.bookCar(userId, carID, startDate, endDate);
            //if successful the booking will be created
            System.out.println();
            System.out.println("BOOKING SUCCESSFUL");
            System.out.println("---------------------------------");

            System.out.println("Booking ID: " + booking.getId());
            System.out.println("User: " + booking.getUser().getName());
            System.out.println("Car: " + booking.getCar().getBrand());
            System.out.println("Registration: " + booking.getCar().getRegNumber());
            System.out.println("Start date: " + booking.getStartDate());
            System.out.println("End date: " + booking.getEndDate());
            System.out.println("Total price: £" + booking.getPrice());
            System.out.println("Status: " + booking.getStatus());
            System.out.println("----------------------------------------");
        } catch (DateTimeParseException e) {

            System.out.println("Unable to create booking: " + e.getMessage());
            //main is responsible for asking the user for input, convert the input and displying the result
            //CarBookingService remains responsible for - checking user exists, check car exists. check dates, availability, price, create booking and saving bookings
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to create booking; Enter correct details: " + e.getMessage());
        }

    }

    //deleteBooking helper
    private static void deleteBooking(Scanner scanner, CarBookingService carBookingService) {
        //First retrieve all current bookings
        CarBooking[] bookings = carBookingService.getAllBookings();

        if (bookings.length == 0) {
            System.out.println("There are no bookings to delete.");
            return;
        }

        //Display bookings first to allow operator to find ID for the search
        System.out.println();
        System.out.println("CURRENT BOOKINGS");
        displayBookings(bookings);

        //Ask for booking ID
        System.out.println("Enter booking ID you want to delete: ");

        String bookingIdInput = scanner.nextLine().trim();

        try {
            //Convert the entered sting into a UUID

            UUID bookingId = UUID.fromString(bookingIdInput);

            //Command the service layer to delete the booking
            //True or False if booking not found

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
//Program flow : Main() -> Creates scanner and services -> sets running = true -> enters the while loop -> displys the menu -> reads the selected option ->switch executes the matching case -> menue repeats.
//Used 2 helper method for user bookings as the first method retrieves the bookings and the second displays any booking array