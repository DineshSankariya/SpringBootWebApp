package com.demo.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.demo.web.repo.UserRepository;
import com.demo.web.vo.Address;
import com.demo.web.vo.User;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WebApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	private User mockUser;

	private int invalidUserID;

	@BeforeEach
	void setUp() {
		invalidUserID = 9;
		mockUser = new User();
		mockUser.setName("John Harris");
		mockUser.setEmail("johnhrris@example.com");
		List<Address> address = new ArrayList<>();
		address.add(new Address("Time Square", "New York", mockUser));
		address.add(new Address("William Street", "New Zealand", mockUser));
		address.add(new Address("Church Road", "Bangalore", mockUser));
		mockUser.setAddress(address);
		// Save mock user in the database before each test
		userRepository.save(mockUser);
	}


	// Perform GET request to /GetAllUsers and verify the response
	@Test
	void testGetAllUsers() throws Exception {
		mockMvc.perform(get("/api/users/GetAllUsers")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("John Harris"))
				.andExpect(jsonPath("$[0].email").value("johnhrris@example.com"));
	}

	// Perform GET request to /GetUserByID/{id} and verify the response
	@Test
	void testGetUserById() throws Exception {
		mockMvc.perform(get("/api/users/GetUserByID/" + mockUser.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("John Harris"))
				.andExpect(jsonPath("$.email").value("johnhrris@example.com"));
	}

	// Perform GET request to /GetUserByID/{id} and for failure test case by passing
	// invalid ID
	@Test
	void failureTestGetUserById() throws Exception {
		mockMvc.perform(get("/api/users/GetUserByID/" + invalidUserID)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("John Harris"))
				.andExpect(jsonPath("$.email").value("johnhrris@example.com"));
	}

	// Perform POST request to /CreateUser and verify the response
	@Test
	void testCreateUser() throws Exception {
		String newUser = "{\"name\": \"New User\", \"email\": \"newuser@example.com\"}";
		mockMvc.perform(post("/api/users/CreateUser").contentType(MediaType.APPLICATION_JSON).content(newUser))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("New User"))
				.andExpect(jsonPath("$.email").value("newuser@example.com"));
	}

	// Perform POST request to /CreateUser and For Failure Test Case by Passing Null
	// as newUser Value
	@Test
	void failureTestCreateUser() throws Exception {
		String newUser = null;
		mockMvc.perform(post("/api/users/CreateUser").contentType(MediaType.APPLICATION_JSON).content(newUser))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("New User"))
				.andExpect(jsonPath("$.email").value("newuser@example.com"));
	}

	// Perform PUT request to /UpdateUser/{id} and verify the response
	@Test
	void testUpdateUser() throws Exception {

		String updatedUser = "{\"name\": \"Updated Name\", \"email\": \"updatedemail@example.com\"}";
		mockMvc.perform(put("/api/users/UpdateUser/" + mockUser.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(updatedUser)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Updated Name"))
				.andExpect(jsonPath("$.email").value("updatedemail@example.com"));
	}

	// Perform PUT request to /UpdateUser/{id} and For Failure Test Case by Passing
	// Invalid userID
	@Test
	void failureTestUpdateUser() throws Exception {
		String updatedUser = "{\"name\": \"Updated Name\", \"email\": \"updatedemail@example.com\"}";
		mockMvc.perform(put("/api/users/UpdateUser/" + invalidUserID).contentType(MediaType.APPLICATION_JSON)
				.content(updatedUser)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Updated Name"))
				.andExpect(jsonPath("$.email").value("updatedemail@example.com"));
	}

	// Perform DELETE request to /Delete/{id} and verify the response
	@Test
	void testDeleteUser() throws Exception {
		mockMvc.perform(delete("/api/users/Delete/" + mockUser.getId())).andExpect(status().isOk());
	}

	// Perform DELETE request to /Delete/{id} and verify the response
	@Test
	void failureTestDeleteUser() throws Exception {
		mockMvc.perform(delete("/api/users/Delete/" + invalidUserID)).andExpect(status().isOk());
	}

}
