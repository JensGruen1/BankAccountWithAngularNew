package com.example.demo.controllerTest;

import BankingApp.controller.UserController;
import BankingApp.dto.LoginRequest;
import BankingApp.entity.User;
import BankingApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    SessionRegistry sessionRegistry;

    @InjectMocks
    private UserController userController;


    @Test
    void signupUser_shouldReturnOk_whenRegisterUserIsSuccessful () {
        User user = new User();
        user.setUsername("testUser");
        user.setUsername(passwordEncoder.encode("password"));
        when(bindingResult.hasErrors()).thenReturn(false);

       ResponseEntity<String> result = userController.signupUser(user,bindingResult);

       verify(userService).registerUser(user);
       assertEquals(HttpStatus.OK, result.getStatusCode());
       assertEquals("User signed up successfully", result.getBody());
    }

    @Test
    void signupUser_shouldThrowRequest_whenBindingResultHasErrors () {
       User user = new User();
        user.setUsername("testUser");
        user.setUsername(passwordEncoder.encode("password"));
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<String> result =  userController.signupUser(user,bindingResult);

        verifyNoInteractions(userService);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid input", result.getBody());

    }

    @Test
    void isUserLoggedIn_shouldReturnHttpStatusOk_whenThereIsUserInDb () {
        User user = new User();
        user.setUsername("testUser");
        user.setUsername(passwordEncoder.encode("password"));

        when(userService.getLoggedInUser()).thenReturn(user);
        ResponseEntity<?>  response = userController.isUserLoggedIn();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User is logged in", response.getBody());

    }

    @Test
    void isUserLoggedIn_shouldReturnForbidden_whenNoUserFoundInDb () {
        when(userService.getLoggedInUser()).thenReturn(null);
        ResponseEntity<?>  response = userController.isUserLoggedIn();

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("No logged in user", response.getBody());
    }


    @Test
    void login_shouldReturnHttpStatusOk_when () {

    }




}


