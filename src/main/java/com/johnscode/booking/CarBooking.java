package com.johnscode.booking;

import com.johnscode.car.Car;
import com.johnscode.user.User;

import java.math.BigDecimal;
import java.util.Objects;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.io.Serializable;

//imports allos CarBooking to use the classes located in other packages
//represents booking in the cr in the car booking system

//Setting the CarBooking class and the fields
public class CarBooking implements Serializable {
    // Phase 2 Part B - this is the object we actually write to bookings.dat
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final User user; //The user who made the booking
    private final Car car; // The car being booked
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal price;
    private BookingStatus status;
    private final LocalDateTime bookedAt;


//Adding the constructor : creates and initialises the new carbooking object.
//Note the order of the argument matters when the constructor is called.

    public CarBooking(UUID id, User user, Car car, LocalDate startDate, LocalDate endDate, BigDecimal price, BookingStatus status, LocalDateTime bookedAt) {

        //store each supplied value inside this booking object
        this.id = id;
        this.user = user;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.status = status;
        this.bookedAt = bookedAt;
    }

    //Getters, returns value of each attribute provided
    //Each getters return type matches its field
    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Car getCar() {
        return car;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BookingStatus getStatus() {
        return status;
    }

    // Allows the booking status to change, for example when cancelling a booking.
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    // Compare bookings by ID only, because the ID uniquely identifies each booking.
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CarBooking carBooking)) {
            return false;
        }
        return Objects.equals(id, carBooking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
