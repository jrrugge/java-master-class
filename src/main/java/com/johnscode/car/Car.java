package com.johnscode.car;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

//Represents one car in the car booking system and holds 5 values which need to be supplied

public record Car(UUID id, String regNumber, BigDecimal rentalPricePerDay, Brand brand,
                  boolean electric) implements Serializable {
    // Phase 2 Part B - needed because CarBooking gets saved to a file and it contains a Car object
    private static final long serialVersionUID = 1L;
    //Constructor, called whenever a new car object is created. All five values must be provided when creating a car in order to create the object
    //store supplied constructor value in the corresponding field

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return electric == car.electric && Objects.equals(id, car.id) && Objects.equals(regNumber, car.regNumber) && Objects.equals(rentalPricePerDay, car.rentalPricePerDay) && brand == car.brand;
    }

}


