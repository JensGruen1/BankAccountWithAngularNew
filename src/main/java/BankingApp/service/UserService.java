package BankingApp.service;

import BankingApp.entity.User;
import BankingApp.repository.UserRepository;
import BankingApp.util.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;



    public void registerUser(User user) {
            userRepository.findByUsername(user.getUsername()).ifPresent(u -> {throw new UserAlreadyExistsException("User already exists");});

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


    public User getLoggedInUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return userRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + auth.getName()));
        } else {

            throw new AccessDeniedException("No authenticated user found");
        }
    }


}
