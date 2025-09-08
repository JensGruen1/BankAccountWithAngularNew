package com.example.demo.controllerTest;

import BankingApp.controller.UserController;
import BankingApp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @TestConfiguration
    static class TestSecurityUsers {

        @Bean
        @Primary
        PasswordEncoder testPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        @Primary
        UserDetailsService testUserDetailsService(PasswordEncoder encoder) {
            UserDetails user = User.withUsername("testuser")
                    .password(encoder.encode("pass"))  // wichtig: encodiert!
                    .roles("USER")
                    .build();
            return new InMemoryUserDetailsManager(user);
        }
    }



    @Test
    void loginWithValidCredentials_returnsOk() throws Exception {
        String json = "{\"username\":\"testuser\",\"password\":\"pass\"}";

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                //.andExpect(content().string("Login erfolgreich"));
                .andExpect(jsonPath("$.token").isNotEmpty());

    }

    @Test
    void login_401_withWrongPassword () throws Exception{
        String json = "{\"username\":\"testuser\",\"password\":\"wrong\"}";

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Ungültige Zugangsdaten"));
    }

    @Test
    void login_401_withUnknownUser () throws Exception {
        String json = "{\"username\":\"wrongUser\",\"password\":\"pass\"}";

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Ungültige Zugangsdaten"));
    }



}
