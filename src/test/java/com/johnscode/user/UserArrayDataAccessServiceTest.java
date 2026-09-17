package com.johnscode.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserArrayDataAccessServiceTest {

    private UserArrayDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserArrayDataAccessService();
    }

    @Test
    void itShouldReturnAllUsers() {
        List<User> users = underTest.getUsers();

        assertEquals(3, users.size());
    }

    @Test
    void itShouldFindUserById() {
        UUID jamesId = UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3");

        User user = underTest.findUserById(jamesId);

        assertEquals("James", user.getName());
    }

    @Test
    void itShouldReturnNullWhenUserNotFound() {
        UUID unknownId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        User user = underTest.findUserById(unknownId);

        assertNull(user);
    }
}