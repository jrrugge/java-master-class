package com.johnscode.car;

import com.johnscode.user.User;

import java.math.BigDecimal;
import java.util.UUID;

//Represents one car in the car booking system and holds 5 values which need to be supplied

public class Car {
    private final UUID id;
    private final String regNumber;
    private final BigDecimal rentalPricePerDay;
    private final Brand brand;
    private final boolean electric;

//Constructor, called whenever a new car object is created. All five values must be provided when creating a car in order to create the object
public Car (UUID id, String regNumber, BigDecimal rentalPricePerDay, Brand brand, boolean electric) {
    //store supplied constructor value in the corresponding field
    this.id = id;
    this.regNumber = regNumber;
    this.rentalPricePerDay = rentalPricePerDay;
    this.brand = brand;
    this.electric = electric;
}
//getter returns the cars unique ID
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

}


