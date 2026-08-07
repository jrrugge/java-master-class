package com.johnscode.user;

import java.util.UUID;
import com.johnscode.user.User;
import com.johnscode.user.UserService;

public class UserDao {
//DAO - means data caccess object
// UserDAO is responsible for storing and retrieving User Objects
    //This app will use arrays, a real app will use a database instead

private static final User[] users;

//The array stores all the users in the application
    //static means there is one shared users array for the whole application
    //final means the users variable cant be assigned to a different array

    //User[]: is an array containing user objects

    //A static block runs once when java first loads the userdao class
    //used to add sample data when the application starts
    //This process is called seeding data

static {
    users = new User[] {
            new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "James"), //user stored at index 0 and so on
            new User(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), "John"),
            new User(UUID.fromString("7e8b2f7c-dcb4-4b18-8d74-f0766363a11c"), "Alex")
    };
}
//Java loads userdao-> static block runs-> three user objects are created->they are stored in the users array
//Returns the array containing all users. The return type is User[] because this method returns an array of user objects.

public User[] getAllUsers() {
    return users;
}


//Searches for a user using the UUID. The method received a UUID called userID. Return matching users or null when not found.
public User getUserById(UUID userId) {
    //Loops through every position in the users aray
    for(int i = 0; i < users.length; i++) {
        User currentUser = users[i];

        if(currentUser.getId().equals(userId)) {
            return currentUser;
        }
    }
    return null;
}


}
