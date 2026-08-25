package com.johnscode.user;

//java class/util used to create unique id's.

import java.io.Serializable;
import java.util.UUID;

//This class represents a user in the car booking system

/**
 * @param id defines the information every user contains, two people can have the same name but not the same UUID
 */
public record User(UUID id, String name) implements Serializable {

    private static final long serialVersionUID = 1L; // part B - User sits inside CarBooking when we save to file
    //Constructor - called whenever we create a new user object
    //Values provided are then copied into the user objects field
    //means store the supplied id inside the User objects id field.

}
