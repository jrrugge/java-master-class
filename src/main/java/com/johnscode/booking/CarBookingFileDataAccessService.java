package com.johnscode.booking;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CarBookingFileDataAccessService implements CarBookingDao {

    private final String filepath;

    public CarBookingFileDataAccessService(String filepath) {
        this.filepath = filepath;
    }

    private List<CarBooking> readBookingsFromFile() {
        File file = new File(filepath);

        if (!file.exists()) {
            return Collections.emptyList();
        }

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            return (List<CarBooking>) input.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            throw new IllegalStateException("Failed to read bookings from file: " + filepath, exception);
        }
    }

    private void writeBookingsToFile(List<CarBooking> bookings) {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filepath))) {
            output.writeObject(bookings);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write bookings to file: " + filepath, exception);
        }
    }

    @Override
    public List<CarBooking> getBookings() {
        return readBookingsFromFile();
    }

    @Override
    public CarBooking findBookingById(UUID bookingId) {
        List<CarBooking> bookings = readBookingsFromFile();

        for (CarBooking currentBooking : bookings) {
            if (currentBooking.getId().equals(bookingId)) {
                return currentBooking;
            }
        }

        return null;
    }

    @Override
    public void saveBooking(CarBooking booking) {
        List<CarBooking> bookings = new ArrayList<>(readBookingsFromFile());
        bookings.add(booking);
        writeBookingsToFile(bookings);
    }

    @Override
    public void deleteBooking(UUID bookingId) {
        List<CarBooking> bookings = readBookingsFromFile();

        for (int i = 0; i < bookings.size(); i++) {
            CarBooking currentBooking = bookings.get(i);
            if (currentBooking.getId().equals(bookingId)) {
                currentBooking.setStatus(BookingStatus.CANCELLED);
                writeBookingsToFile(bookings);
                return;
            }
        }
    }
}
