package com.johnscode.booking;

import java.util.UUID;

public class CarBookingDao {
    //CarBookingDao stores and retrieves booking data
    //Unlike users and cars, booking are not fixed seeded data
    //bookings are added and deleted during application run time

    //the app starts with no bookings
    //the array cannot change in size, when saving or deleting a booking, we create a new array
    //and assign it back to this variable

    private static CarBooking[] bookings = new CarBooking[0];

    //Returns all current bookings
    public CarBooking[] getAllBookings() {
        return bookings;
    }

    //Get bookings by user method. Returns all bookings belonging to one user
    //Remember arrays have a fixed sized, so you need a loop for counting how many matching bookings exist and another for that exact size and fill it
    public CarBooking[] getBookingsByUserId(UUID userId) {

        int matchingBookingCount = 0;

        for (int i = 0; i < bookings.length; i++) {

            CarBooking currentBooking = bookings[i];

            //Get the user stored inside the booking, then compare that users iD with userID
            boolean sameUser = currentBooking
                    .getUser()
                    .getId()
                    .equals(userId);

            if(sameUser) {
                matchingBookingCount++;
            }
        }
        //Create an array large enough to hold all the matches. If no bookings match, this creates an empty array (new CarBooking)
        CarBooking[] userBookings = new CarBooking[matchingBookingCount];

        //this tracks the next available position in the new userBookings array
        int userBookingIndex = 0;
        //Loop thorugh the bookings again and copy matching bookings into the new array.

        for (int i = 0; i < bookings.length; i++) {
            CarBooking currentBooking = bookings[i];
            boolean sameUser = currentBooking
                    .getUser()
                    .getId()
                    .equals(userId);

            if (sameUser) {
                userBookings[userBookingIndex] = currentBooking;
                userBookingIndex++;
            }
        }
        //Return only the bookings belonging to this user
        return userBookings;
    }


    //Save a new booking
    //Since arrays have a fixed sixe: We 1/ create a new array that is one position larger 2. Copy the exisitng booking into 3. add the new booking at the end 4. replace the old array
    public void saveBooking(CarBooking booking) {
        //Create an array with one additional space
        CarBooking[] updatedBookings = new CarBooking[bookings.length + 1];

        //Copy all exisitng bookings into the new array
        for (int i = 0; i < bookings.length; i++) {
            updatedBookings[i] = bookings[i];
        }
        //Store the new booking in the final position
        updatedBookings[updatedBookings.length -1] = booking;

        //Replace the old array with the expanded array
        bookings = updatedBookings;
    }
    //Delete a booking using its UUID
    public boolean deleteBookingById(UUID bookingId) {
        //first find the position of the booking you want to delete
        //-1 means that no matching booking has been found yet

        int bookingIndex = -1;
        for(int i = 0; i < bookings.length; i++) {
            CarBooking currentBooking = bookings[i];

            if (currentBooking.getId().equals(bookingId)) {
                bookingIndex = i;
                break;
            }
        }
        //No match was found
        if(bookingIndex == -1) {
            return false;
        }
        //Create an array with 1 fewer position
        CarBooking[] updatedBookings = new CarBooking[bookings.length -1];

        //index tracks where to insert the new array
        int newIndex = 0;

        //Copy every booking except the one being deleted
        for (int i = 0; i < bookings.length; i++) {
            if(i != bookingIndex) {
                updatedBookings[newIndex] = bookings[i];
                newIndex++;
            }
        }
        //Replace the original array
        bookings = updatedBookings;
        return true;
    }

}
