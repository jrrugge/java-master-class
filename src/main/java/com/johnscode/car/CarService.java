package com.johnscode.car;

import com.johnscode.car.CarDao;
import java.util.UUID;
import com.johnscode.car.Car;

//return all cars
//find a car by uuid
//return only electric cars
//validate input before calling carDao

public class CarService {

    //CarService needs a CarDao
private final CarDao carDao;


//Constructor to create new CarDao used by this service.
public CarService() {
    this.carDao = new CarDao();
}

//returns all cars stored in CarDao
    //The service delegates retrieval to CarDao
public Car[] getAllCars() {
    return carDao.getAllCars();
}

//Find a car using its unique ID

public Car getCarById(UUID carId) {
    if (carId == null) {
        throw new IllegalArgumentException("Car ID cannot be null");
    }
    return carDao.getCarById(carId);
}

//Returns a new array containing only electric cars
    //loops to count how many electric cars exist
    //and create an array of that exact size and fill it
public Car[] getElectricCars() {
    Car[] allCars = carDao.getAllCars();
    int electricCarCount = 0; //variable to hold count of electric cars

    //first loop - count how many cars are electric
    for (int i = 0; i < allCars.length; i++) {
        Car currentCar = allCars[i];

        if(currentCar.isElectric()) {
            electricCarCount++;
        }
    }

    //Array to hold the number of electric cars found
    Car[] electricCars = new Car[electricCarCount];
    int electricCarIndex = 0;  //position of insert in the array of electric cars found in order

    //second loop. Find each electric car and add it to te new array
    for(int i = 0; i < allCars.length; i++) {
        Car currentCar = allCars[i];

        if (currentCar.isElectric()) {
            electricCars[electricCarIndex] = currentCar; //store the current electric car in te next available position
        electricCarIndex++; //Move to the next position for the next match
        }
    }
    return electricCars;
}

}