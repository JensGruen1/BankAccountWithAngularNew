package BankingApp.controller;


import BankingApp.dto.LoginRequest;
import BankingApp.entity.User;
import BankingApp.repository.UserRepository;
import BankingApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // allow Angular-Port
@RequestMapping("/api/users")
public class UserController {


    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final SessionRegistry sessionRegistry;
    private final UserService userService;

    public UserController(AuthenticationManager authenticationManager, SessionRegistry sessionRegistry, UserService userService) {;
        this.authenticationManager = authenticationManager;
        this.sessionRegistry = sessionRegistry;
        this.userService = userService;
    }



    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }
        //Case of too many requests is still to cover!
        userService.registerUser(user);
        return ResponseEntity.ok("User signed up successfully");

    }

/*    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = repository.findByUsername(loginRequest.getUsername());

        if (user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity.ok("Login success");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginRequest loginrequest) {
        try {
            logger.info("Login attempt for user: {}", loginrequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginrequest.getUsername(), loginrequest.getPassword())
            );

            // Login erfolgreich
            logger.info("Login successful for user: {}", loginrequest.getUsername());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
            sessionRegistry.registerNewSession(session.getId(), authentication.getPrincipal());
            return ResponseEntity.ok("Login erfolgreich");
        } catch (BadCredentialsException ex) {
            logger.warn("Login failed for user: {} - Reason: {}", loginrequest.getUsername(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Zugangsdaten");
        }
    }

    @GetMapping("/isUserLoggedIn")
    public ResponseEntity<?> isUserLoggedIn() {
        User user = userService.getLoggedInUser();
        if (user != null ) {
            return ResponseEntity.status(HttpStatus.OK).body("User is logged in");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No logged in user");
        }
    }


}
