package com.johnscode.car;

import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarFakerDataAccessService implements CarDao {

    private final List<Car> cars;
    private final Faker faker = new Faker();

    public CarFakerDataAccessService() {
        cars = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            cars.add(new Car(
                    UUID.randomUUID(),                              // id
                    faker.bothify("??## ???").toUpperCase(),      // regNumber (randon mix of letters a-z and numbers 0-9)
                    new BigDecimal(faker.number().numberBetween(40, 150)), // rentalPricePerDay
                    faker.options().option(Brand.class),            // brand (TESLA, AUDI, MERCEDES, TOYOTA)
                    faker.bool().bool()                             // electric
            ));
        }
    }

    @Override
    public List<Car> getCars() {
        return cars;
    }

    @Override
    public Car findCarById(UUID carId) {
        return cars.stream()
                .filter(car -> car.getId().equals(carId))
                .findFirst()
                .orElse(null);
    }
}