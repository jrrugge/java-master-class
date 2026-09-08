package com.johnscode.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarDao carDao;

    private CarService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CarService(carDao);
    }

    @Test
    void itShouldGetAllCars() {
        UUID carId = UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3");
        Car tesla = new Car(carId, "AB12 CDE", new BigDecimal("50.00"), Brand.TESLA, true);
        when(carDao.getCars()).thenReturn(List.of(tesla));

        List<Car> result = underTest.getAllCars();

        assertEquals(1, result.size());
        assertEquals("AB12 CDE", result.get(0).getRegNumber());
        verify(carDao).getCars();
    }

    @Test
    void itShouldFindCarById() {
        UUID carId = UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3");
        Car tesla = new Car(carId, "AB12 CDE", new BigDecimal("50.00"), Brand.TESLA, true);
        when(carDao.findCarById(carId)).thenReturn(tesla);

        Car result = underTest.findCarById(carId);

        assertEquals("AB12 CDE", result.getRegNumber());
        verify(carDao).findCarById(carId);
    }
    
    @Test
    void itShouldThrowWhenCarIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> underTest.findCarById(null));
    }
}