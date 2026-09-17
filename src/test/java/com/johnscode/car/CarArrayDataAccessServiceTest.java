package com.johnscode.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CarArrayDataAccessServiceTest {

    private CarArrayDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CarArrayDataAccessService();
    }

    @Test
    void itShouldReturnAllCars() {
        List<Car> cars = underTest.getCars();

        assertEquals(4, cars.size());
    }

    @Test
    void itShouldFindCarById() {
        UUID toyotaId = UUID.fromString("df63c985-4e76-48af-9c8f-a539de9269c4");

        Car car = underTest.findCarById(toyotaId);

        assertEquals(Brand.TOYOTA, car.getBrand());
    }

    @Test
    void itShouldReturnNullWhenCarNotFound() {
        UUID unknownId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        Car car = underTest.findCarById(unknownId);

        assertNull(car);
    }
}
