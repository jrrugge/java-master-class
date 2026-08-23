package com.johnscode.user;

import java.util.UUID;

import com.johnscode.user.UserDao;

public class UserService {
    //Main.java -> userService -> UserDao -> User[]
    //Each class has a separate responsibility
    //User.java -> Represents one user
    //UserDao.java -> stores and retrieves users
    //UserService.java -> Provides user related operations to the rest of the application
    //Main.java -> interacts with the person using the program

    //UserService functionality: Return all users, find a user by uuid and validate that the supplied uuid is not null
    //The service uses UserDao to access the stored users
    //Private - only UserService can directly use this field.

    private final UserDao userDao; //declares that user service needs a UserDao


    // Phase 2 Part C - Main passes in the UserDao now instead of this class creating its own.
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    // getAllUsers() kept for Main - delegates to userDao.getUsers() on the interface
    public User[] getAllUsers() {
        return userDao.getUsers();
    }

    //find one user using the UUID

    public User getUserById(UUID userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userDao.findUserById(userId);
    }

}




