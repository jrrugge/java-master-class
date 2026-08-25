package com.johnscode.booking;

import java.io.*;
import java.util.UUID;

public class CarBookingFileDataAccessService implements CarBookingDao {

    private final String filepath;

    public CarBookingFileDataAccessService(String filepath) {
        this.filepath = filepath;
    }

    private CarBooking[] readBookingsFromFile() {
        File file = new File(filepath);

        if (!file.exists()) {
            return new CarBooking[0];
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            return (CarBooking[]) input.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            throw new IllegalStateException("Failed to read bookings from file: " + filepath, exception);
        }
    }

    private void writeBookingsToFile(CarBooking[] bookings) {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filepath))) {
            output.writeObject(bookings);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write bookings to file: " + filepath, exception);
        }
    }

    @Override
    public CarBooking[] getBookings() {
        return readBookingsFromFile();
    }

    @Override
    public CarBooking findBookingById(UUID bookingId) {
        CarBooking[] bookings = readBookingsFromFile();

        for (int i = 0; i < bookings.length; i++) {
            CarBooking currentBooking = bookings[i];
            if (currentBooking.getId().equals(bookingId)) {
                return currentBooking;
            }
        }

        return null;
    }

    @Override
    public void saveBooking(CarBooking booking) {
        CarBooking[] bookings = readBookingsFromFile();
        CarBooking[] updatedBookings = new CarBooking[bookings.length + 1];

        System.arraycopy(bookings, 0, updatedBookings, 0, bookings.length);

        updatedBookings[updatedBookings.length - 1] = booking;
        writeBookingsToFile(updatedBookings);
    }

    @Override
    public void deleteBooking(UUID bookingId) {
        CarBooking[] bookings = readBookingsFromFile();

        for (int i = 0; i < bookings.length; i++) {
            CarBooking currentBooking = bookings[i];
            if (currentBooking.getId().equals(bookingId)) {
                currentBooking.setStatus(BookingStatus.CANCELLED);
                writeBookingsToFile(bookings);
                return;
            }
        }
    }
}
