package com.johnscode.user;

import java.util.UUID;

// Phase 2 Part A - old UserDao class renamed and moved here.
// Still uses the static users array from phase 1, just implements the UserDao interface now.

public class UserArrayDataAccessService implements UserDao {


    private static final User[] users;


    static {
        users = new User[]{
                new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "James"), //user stored at index 0 and so on
                new User(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), "John"),
                new User(UUID.fromString("7e8b2f7c-dcb4-4b18-8d74-f0766363a11c"), "Alex")
        };
    }
//Java loads userArrayDataAccessService-> static block runs-> three user objects are created->they are stored in the users array
//Returns the array containing all users. The return type is User[] because this method returns an array of user objects.

    @Override
    public User[] getUsers() {
        return users;
    }


    //Searches for a user using the UUID. The method received a UUID called userID. Return matching users or null when not found.
    @Override
    public User findUserById(UUID userId) {
        //Loops through every position in the users array
        for (int i = 0; i < users.length; i++) {
            User currentUser = users[i];

            if (currentUser.getId().equals(userId)) {
                return currentUser;
            }
        }
        return null;
    }


}
