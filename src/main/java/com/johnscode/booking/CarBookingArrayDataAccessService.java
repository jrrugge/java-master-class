package com.johnscode.booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarBookingArrayDataAccessService implements CarBookingDao {

    private static final List<CarBooking> bookings = new ArrayList<>();

    @Override
    public List<CarBooking> getBookings() {
        return bookings;
    }

    @Override
    public CarBooking findBookingById(UUID bookingId) {
        for (int i = 0; i < bookings.size(); i++) {
            CarBooking currentBooking = bookings.get(i);
            if (currentBooking.getId().equals(bookingId)) {
                return currentBooking;
            }
        }
        return null;
    }

    @Override
    public void saveBooking(CarBooking booking) {
        bookings.add(booking);
    }

    @Override
    public void deleteBooking(UUID bookingId) {
        for (int i = 0; i < bookings.size(); i++) {
            CarBooking currentBooking = bookings.get(i);
            if (currentBooking.getId().equals(bookingId)) {
                currentBooking.setStatus(BookingStatus.CANCELLED);
                return;
            }
        }
    }
}
