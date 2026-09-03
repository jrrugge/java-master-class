package com.johnscode.car;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CarService {

    private final CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public List<Car> getAllCars() {
        return carDao.getCars();
    }

    public Car findCarById(UUID carId) {
        if (carId == null) {
            throw new IllegalArgumentException("Car ID cannot be null");
        }
        return carDao.findCarById(carId);
    }

    // Phase 4 - filter with stream instead of loop
    public List<Car> getElectricCars() {
        return carDao.getCars().stream()
                .filter(Car::isElectric)
                .collect(Collectors.toList());
    }
}
