package com.example.demo.serviceTest;

import BankingApp.entity.User;
import BankingApp.repository.UserRepository;
import BankingApp.service.UserService;
import BankingApp.util.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLoggedInUser_shouldReturnUser_whenAuthenticated () {
        //given
        User user = new User();
        user.setUsername("testUser");
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("testUser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        //when
        User resultUser = userService.getLoggedInUser();

        //then
        assertThat(resultUser).isEqualTo(user);

    }

    @Test
    void getLoggedInUser_shouldThrowAccessDeniedException_whenAuthIsNull () {
        //given
       SecurityContext securityContext = mock(SecurityContext.class);
       when(securityContext.getAuthentication()).thenReturn(null);
       SecurityContextHolder.setContext(securityContext);

       //when
        //then
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> userService.getLoggedInUser());


    }

    @Test
    void getLoggedInUser_shouldThrowAccessDeniedException_whenAuthenticationIsInstanceOfAnonymousAuthenticationToken () {
        Authentication auth = mock(AnonymousAuthenticationToken.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(AccessDeniedException.class, () -> userService.getLoggedInUser());
    }

    @Test
    void getLoggedInUser_shouldThrowUsernameNotFoundException_whenUserNotFound () {

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("UnknownUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(auth.getName())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.getLoggedInUser());
    }

    public void registerUser(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {throw new UserAlreadyExistsException("User already exists");});

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    @Test
    void registerUser_shouldThrowUserAlreadyExistException_whenUserAlreadyInDb  () {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
    }

    @Test
    void registerUser_shouldSaveUserInDb_whenUserIsNotInDb () {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        userService.registerUser(user);

        verify(userRepository,times(1)).save(user);


    }



}
