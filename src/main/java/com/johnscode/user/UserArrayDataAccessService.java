package com.johnscode.user;

import java.util.List;
import java.util.UUID;

public class UserArrayDataAccessService implements UserDao {

    private static final List<User> users;

    static {
        users = List.of(
                new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "James"),
                new User(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), "John"),
                new User(UUID.fromString("7e8b2f7c-dcb4-4b18-8d74-f0766363a11c"), "Alex")
        );
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User findUserById(UUID userId) {
        for (int i = 0; i < users.size(); i++) {
            User currentUser = users.get(i);
            if (currentUser.id().equals(userId)) {
                return currentUser;
            }
        }
        return null;
    }
}
