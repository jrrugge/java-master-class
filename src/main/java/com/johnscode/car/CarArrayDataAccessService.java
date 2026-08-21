package com.johnscode.car;

import java.util.UUID;
import java.math.BigDecimal;

// Phase 2 Part A - moved all the car array code out of CarDao into here.
// CarDao is now an interface; this class implements it using the same seed data from phase 1.
// Renamed methods to match the interface: getCars() and findCarById().

public class CarArrayDataAccessService implements CarDao {

    private static final Car[] cars;

    @Override
    public Car[] getCars() {
        return cars;
    }

    @Override
    public Car findCarById(UUID carId) {
        //Check for a car in the array
        for (int i = 0; i < cars.length; i++) {
            Car currentCar = cars[i];
            //compare uuid values using equals
            if (currentCar.getId().equals(carId)) {
                return currentCar;
            }
        }
        return null;
    }

    static {
        cars = new Car[]{
                new Car(
                        UUID.fromString("a40b7081-3c55-4f87-81d5-cd03c02f0021"),
                        "AB12 CDE",
                        new BigDecimal("75.00"),
                        Brand.TESLA,
                        true
                ),

                new Car(UUID.fromString("b5168555-22e0-4d11-8378-8df78af03d10"),
                        "MN56 OPQ",
                        new BigDecimal("80.00"),
                        Brand.MERCEDES,
                        false
                ),

                new Car(
                        UUID.fromString("df63c985-4e76-48af-9c8f-a539de9269c4"),
                        "EF78 GHI",
                        new BigDecimal("55.00"),
                        Brand.TOYOTA,
                        false
                ),

                new Car(UUID.fromString("c88d662e-a50f-47e5-9a9b-a86c48d14bf3"),
                        "MN56 OPQ",
                        new BigDecimal("100.00"),
                        Brand.AUDI,
                        false
                )
        };

    }

}
