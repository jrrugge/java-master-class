package com.johnscode.booking;

import java.util.UUID;

// Phase 2 Part A - booking DAO split into interface + implementation classes.
// CarBookingArrayDataAccessService = in memory. CarBookingFileDataAccessService = file (part B).

public interface CarBookingDao {
    CarBooking[] getBookings();

    CarBooking findBookingById(UUID bookingId);

    void saveBooking(CarBooking booking);

    void deleteBooking(UUID bookingId);
}
