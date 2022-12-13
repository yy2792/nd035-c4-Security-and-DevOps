package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private BCryptPasswordEncoder encoder;

    @Before
    public void setUp() {
    }

    @Test
    public void createUser() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("IsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(u.getUsername(), "test");
        assertEquals(u.getPassword(), "IsHashed");

    }

    @Test
    public void createUserWithInvalidLength() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("test");
        r.setConfirmPassword("test");

        final ResponseEntity<User> response = userController.createUser(r);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUserWithPasswordNotMatch() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("test1");
        r.setConfirmPassword("test");

        final ResponseEntity<User> response = userController.createUser(r);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findByUserName() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("testpassword");

        when(userRepository.findByUsername("test")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("test");
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(u.getUsername(), "test");
    }

    @Test
    public void findById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("testpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(u.getUsername(), "test");
    }
}
