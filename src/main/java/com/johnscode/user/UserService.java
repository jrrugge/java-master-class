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

    //UserService functionality: Return all users, find a user by uuid and validate that te supplied uuid is not null
    //The service uses UserDao to access the stored users
    //Private - only userservice can directly use this field.

    private final UserDao userDao; //declares that user service needs a UserDao
    //the service will create its own dao object as we cant use dependency injection just yet

    //this constructor runs when a new UserService is created, the constructor creates the dao object
    public UserService() {
        this.userDao = new UserDao();
    }

    //return every user stored by UserDao
    //USerService is asking UserDao to return users - Delegation (Main asks userserice for users-> userserice asks userdao->userdao returns the user array-> usersevice returns it to main)
    public User[] getAllUsers() {
        return userDao.getAllUsers();
    }

    //find one user using the UUID

    public User getUSerById(UUID userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userDao.getUserById(userId);
    }

}




