package com.example.demo;

import BankingApp.entity.User;
import BankingApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldSaveUserToDatabase() throws Exception {
		// Arrange: user erstellen
		User user = new User();
		user.setUsername("testuser");
		user.setPassword("testpass");

		// Act: POST-Request absenden
		mockMvc.perform(post("/api/users/signup")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk());

		// Assert: prüfen, ob gespeichert
		Optional<User> savedUser = userRepository.findByUsername("testuser");
		assertThat(savedUser).isPresent().get().extracting(User::getPassword).isEqualTo("testpass");
		//assertThat(savedUser).isNotNull();
		//assertThat(savedUser.get().getPassword()).isEqualTo("testpass");
	}
}
