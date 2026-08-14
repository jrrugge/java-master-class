package com.johnscode.car;

import java.math.BigDecimal;
import java.util.UUID;

import com.johnscode.car.Brand;
import com.johnscode.user.User;


public class CarDao {


    private static final Car[] cars;

    public Car[] getAllCars() {
        return cars;
    }

    public Car getCarById(UUID carId) {
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
