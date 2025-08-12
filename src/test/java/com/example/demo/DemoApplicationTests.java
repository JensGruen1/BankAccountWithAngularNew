package com.example.demo;

import BankingApp.config.SecurityConfig;
import BankingApp.entity.User;
import BankingApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private User user;



	@TestConfiguration
	public class TestSecurityConfig {

		@Bean
		public InMemoryUserDetailsManager userDetailsService() {
			UserDetails user = org.springframework.security.core.userdetails.User.withUsername("user")
					.password(passwordEncoder().encode("password"))
					.roles("USER")
					.build();
			return new InMemoryUserDetailsManager(user);
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
	}


	@BeforeEach
	void setup() {

		userRepository.deleteAll();
		user = new User();
		user.setUsername("user");
		user.setPassword(passwordEncoder.encode("password")); // Passwort hashen!
		userRepository.save(user);
	}



	@Test
	void shouldSaveUserToDatabase() throws Exception {
		// Arrange: user erstellen
		 userRepository.deleteAll();
		 User user = new User();
		 user.setUsername("user");
		 user.setPassword("password");

		// Act: POST-Request absenden
		mockMvc.perform(post("/api/users/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());

		// Assert: prüfen, ob gespeichert
		Optional<User> savedUser = userRepository.findByUsername("user");
		assertThat(savedUser).isPresent();

		String encodedPassword = savedUser.get().getPassword();
		assertThat(encodedPassword).isNotEqualTo("password");
		assertThat(passwordEncoder.matches("password",encodedPassword)).isTrue();
	}

	@Test
	void testIfFieldIsEmpty() throws Exception {
		userRepository.deleteAll();
		User user = new User();
		user.setUsername("user");
		user.setPassword("");

		mockMvc.perform(post("/api/users/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isBadRequest());
	}


	@Test
	void testLoginSuccess() throws Exception {

		mockMvc.perform(post("/api/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"user\",\"password\":\"password\"}"))
		        .andExpect(status().isOk())
				.andExpect(content().string("Login erfolgreich"));

	}

	@Test
	void testWrongPassword() throws Exception {

		mockMvc.perform(post("/api/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\":\"user\",\"password\":\"wrongPassword\"}"))
				.andExpect(status().isUnauthorized())
				.andExpect(content().string("Ungültige Zugangsdaten"));
	}

	@Test
	void testWrongUsername() throws Exception {

		mockMvc.perform(post("/api/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"wrongUser\",\"password\":\"password\"}"))
				.andExpect(status().isUnauthorized())
				.andExpect(content().string("Ungültige Zugangsdaten"));
	}

	@Test
	void TestLogoutSuccess() throws Exception {
		MvcResult loginResult = mockMvc.perform(post("/api/users/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"user\", \"password\":\"password\"}"))
				.andExpect(status().isOk())
				.andReturn();

		MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
		assertNotNull(session);

		mockMvc.perform(post("/api/users/logout")
						.session(session))
				.andExpect(status().isOk());

	}




}
