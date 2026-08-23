package com.johnscode.booking;

import java.util.UUID;

public class CarBookingArrayDataAccessService implements CarBookingDao {

    // Phase 2 Part A - same booking array logic from phase 1, just in its own class now.
    // getBookingsByUserId used to live here but the interface doesn't include it -
    // that filtering moved up to CarBookingService (see getBookingsByUserId there).

    private static CarBooking[] bookings = new CarBooking[0];

    //Returns all current bookings
    @Override
    public CarBooking[] getBookings() {
        return bookings;
    }

    // Searches for one booking by its unique booking ID.
    // Returns the matching booking, or null if not found.
    @Override
    public CarBooking findBookingById(UUID bookingId) {
        for (int i = 0; i < bookings.length; i++) {
            CarBooking currentBooking = bookings[i];
            if (currentBooking.getId().equals(bookingId)) {
                return currentBooking;
            }
        }
        return null;
    }

    //Save a new booking
    //Since arrays have a fixed sixe: We 1/ create a new array that is one position larger 2. Copy the exisitng booking into 3. add the new booking at the end 4. replace the old array
    @Override
    public void saveBooking(CarBooking booking) {
        //Create an array with one additional space
        CarBooking[] updatedBookings = new CarBooking[bookings.length + 1];

        //Copy all existing bookings into the new array
        for (int i = 0; i < bookings.length; i++) {
            updatedBookings[i] = bookings[i];
        }
        //Store the new booking in the final position
        updatedBookings[updatedBookings.length - 1] = booking;

        //Replace the old array with the expanded array
        bookings = updatedBookings;
    }

    //Soft delete a booking by setting its status to CANCELLED.
    @Override
    public void deleteBooking(UUID bookingId) {
        for (int i = 0; i < bookings.length; i++) {
            CarBooking currentBooking = bookings[i];
            if (currentBooking.getId().equals(bookingId)) {
                currentBooking.setStatus(BookingStatus.CANCELLED);
                return;
            }
        }
    }
}


