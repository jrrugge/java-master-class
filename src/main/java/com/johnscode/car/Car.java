package com.johnscode.car;

import com.johnscode.user.User;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;
import java.io.Serializable;

//Represents one car in the car booking system and holds 5 values which need to be supplied

public class Car implements Serializable {
    // Phase 2 Part B - needed because CarBooking gets saved to a file and it contains a Car object
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final String regNumber;
    private final BigDecimal rentalPricePerDay;
    private final Brand brand;
    private final boolean electric;

    //Constructor, called whenever a new car object is created. All five values must be provided when creating a car in order to create the object
    public Car(UUID id, String regNumber, BigDecimal rentalPricePerDay, Brand brand, boolean electric) {
        //store supplied constructor value in the corresponding field
        this.id = id;
        this.regNumber = regNumber;
        this.rentalPricePerDay = rentalPricePerDay;
        this.brand = brand;
        this.electric = electric;
    }

    //getter returns the cars details
    public UUID getId() {
        return id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public BigDecimal getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public Brand getBrand() {
        return brand;
    }

    //boolean getters begin with is instead of get
    public boolean isElectric() {
        return electric;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return electric == car.electric && Objects.equals(id, car.id) && Objects.equals(regNumber, car.regNumber) && Objects.equals(rentalPricePerDay, car.rentalPricePerDay) && brand == car.brand;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, regNumber, rentalPricePerDay, brand, electric);
    }
}


