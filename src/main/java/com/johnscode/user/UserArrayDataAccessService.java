package com.johnscode.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserArrayDataAccessService implements UserDao {

    private static final List<User> users;

    static {
        users = loadUsers();
    }

    private static List<User> loadUsers() {
        List<User> loadedUsers = new ArrayList<>();
        File file = new File(
                UserArrayDataAccessService.class.getClassLoader().getResource("users.csv").getPath()
        );

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                loadedUsers.add(new User(UUID.fromString(parts[0]), parts[1]));
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load users from users.csv", exception);
        }

        return List.copyOf(loadedUsers);
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User findUserById(UUID userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}
