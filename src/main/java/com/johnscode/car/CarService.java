package com.johnscode.car;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public List<Car> getElectricCars() {
        List<Car> allCars = carDao.getCars();
        List<Car> electricCars = new ArrayList<>();

        for (int i = 0; i < allCars.size(); i++) {
            Car currentCar = allCars.get(i);
            if (currentCar.electric()) {
                electricCars.add(currentCar);
            }
        }

        return electricCars;
    }
}
