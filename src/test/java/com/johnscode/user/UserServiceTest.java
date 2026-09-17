package com.johnscode.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userDao);
    }

    @Test
    void itShouldGetAllUsers() {
        List<User> users = List.of(
                new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "James")
        );
        when(userDao.getUsers()).thenReturn(users);

        List<User> result = underTest.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("James", result.get(0).getName());
        verify(userDao).getUsers();
    }

    @Test
    void itShouldGetUserById() {
        UUID userId = UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3");
        User james = new User(userId, "James");
        when(userDao.findUserById(userId)).thenReturn(james);

        User result = underTest.getUserById(userId);

        assertEquals("James", result.getName());
        verify(userDao).findUserById(userId);
    }

    @Test
    void itShouldThrowWhenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> underTest.getUserById(null));
    }
}
