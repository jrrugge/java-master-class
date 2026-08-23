package com.johnscode.user;

import java.util.UUID;

// Phase 2 Part A - same idea as CarDao: interface only, no array in this file anymore.
// UserArrayDataAccessService holds the users array and implements these methods.

public interface UserDao {
    User[] getUsers();

    User findUserById(UUID userId);
}
