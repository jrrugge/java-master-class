package com.johnscode.car;


// Phase 2 Part A - split CarDao into an interface + a separate class that holds the array.
// This file is just the contract (what methods any car DAO must have).
// The actual array logic lives in CarArrayDataAccessService.

import java.util.UUID;

public interface CarDao {
    Car[] getCars();

    Car findCarById(UUID carId);
}
