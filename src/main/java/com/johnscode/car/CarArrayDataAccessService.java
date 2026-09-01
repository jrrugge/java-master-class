package com.johnscode.car;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CarArrayDataAccessService implements CarDao {

    private static final List<Car> cars;

    static {
        cars = List.of(
                new Car(
                        UUID.fromString("a40b7081-3c55-4f87-81d5-cd03c02f0021"),
                        "AB12 CDE",
                        new BigDecimal("75.00"),
                        Brand.TESLA,
                        true
                ),
                new Car(
                        UUID.fromString("b5168555-22e0-4d11-8378-8df78af03d10"),
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
                new Car(
                        UUID.fromString("c88d662e-a50f-47e5-9a9b-a86c48d14bf3"),
                        "AU90 XYZ",
                        new BigDecimal("100.00"),
                        Brand.AUDI,
                        false
                )
        );
    }

    @Override
    public List<Car> getCars() {
        return cars;
    }

    @Override
    public Car findCarById(UUID carId) {
        for (Car currentCar : cars) {
            if (currentCar.getId().equals(carId)) {
                return currentCar;
            }
        }
        return null;
    }
}
