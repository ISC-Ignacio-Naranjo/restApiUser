package com.restapiuser.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.restapiuser.entities.User;
import com.restapiuser.model.LoginRequest;
import com.restapiuser.model.UserRequest;
import com.restapiuser.model.UserResponse;
import com.restapiuser.service.UserService;
import com.restapiuser.util.UserRegistrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private User mockUser;
    private String email = "test@example.com";
    private String password = "password123";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword("encodedPassword"); // encoded pass simulated

    }


    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User(
                UUID.randomUUID(),
                "Alice Johnson",
                "alice@example.com",
                "hashed_password_1",
                Collections.emptyList(),
                new Date(),
                new Date(),
                new Date(),
                "token 1",
                true

        );
        User user2 = new User(
                UUID.randomUUID(),
                "Bob Smith",
                "bob@example.com",
                "hashed_password_2",
                Collections.emptyList(),
                new Date(),
                new Date(),
                new Date(),
                "token 2",
                true
        );
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);
        mockMvc.perform(get("/user/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Alice Johnson"))
                .andExpect(jsonPath("$[1].name").value("Bob Smith"));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
        when(userService.login(any(LoginRequest.class)))
                .thenReturn(ResponseEntity.ok("fake-jwt-token"));
        ResponseEntity<?> response = userController.login(loginRequest);
        assertEquals(200, response.getStatusCodeValue()); // Verifica el código de estado
        assertEquals("fake-jwt-token", response.getBody()); // Verifica el token en el cuerpo de la respuesta
    }

    @Test
    public void testLogin_InvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");
        when(userService.login(any(LoginRequest.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("WRONG_CREDENTIALS"));
        ResponseEntity<String> response = userController.login(loginRequest);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("WRONG_CREDENTIALS", response.getBody());
    }

    @Test
    public void testSignup_Success() {
        UserRequest signupRequest = new UserRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setName("New User");
        UserResponse userResponse = new UserResponse();
        userResponse.setName("testname");
        userResponse.setActive(true);
        userResponse.setId(UUID.randomUUID());
        when(userService.signup(any(UserRequest.class)))
                .thenReturn(ResponseEntity.ok(userResponse));
        ResponseEntity<UserResponse> response = userController.signup(signupRequest);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getId());

    }

    @Test
    public void testSignup_MailAlreadyRegistered() {
        UserRequest signupRequest = new UserRequest();
        signupRequest.setEmail("existinguser@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setName("Existing User");
        when(userService.signup(any(UserRequest.class)))
                .thenThrow(new UserRegistrationException("MAIL ALREADY REGISTERED"));
        ResponseEntity<UserResponse> response = userController.signup(signupRequest);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("MAIL ALREADY REGISTERED", response.getBody().getToken());

    }

}
