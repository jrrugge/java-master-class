package com.johnscode.car;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String regNumber;
    private final BigDecimal rentalPricePerDay;
    private final Brand brand;
    private final boolean electric;

    public Car(UUID id, String regNumber, BigDecimal rentalPricePerDay, Brand brand, boolean electric) {
        this.id = id;
        this.regNumber = regNumber;
        this.rentalPricePerDay = rentalPricePerDay;
        this.brand = brand;
        this.electric = electric;
    }

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

    public boolean isElectric() {
        return electric;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return electric == car.electric
                && Objects.equals(id, car.id)
                && Objects.equals(regNumber, car.regNumber)
                && Objects.equals(rentalPricePerDay, car.rentalPricePerDay)
                && brand == car.brand;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, regNumber, rentalPricePerDay, brand, electric);
    }
}
