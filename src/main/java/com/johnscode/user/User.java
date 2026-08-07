package com.johnscode.user;

//java class/util used to create unique id's.
import java.util.UUID;

//This class represents a user in the car booking system

public class User {

//defines the information every user contains, two people can have the same name but not the same UUID
    private final UUID id;
    private final String name;

    //Constructor - called whenever we create a new user object
    //Values provided are then copied into the user objects field
    public User(UUID id, String name) {
        this.id = id;  //means store the supplied id inside the User objects id field.
        this.name = name;
    }

    //getter returning the users unique id, code outside this class retrieves data via the getter as it is set to private in this class.
    public UUID getId() {
        return id;
    }

    //getter returning te user name provided
    public String getName() {
        return name;
    }
}
