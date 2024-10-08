package com.demo.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.demo.web.repo.UserRepository;
import com.demo.web.vo.Address;
import com.demo.web.vo.User;

@Service
public class UserService {

	private static final Logger logger = LogManager.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private static final String KAFKA_TOPIC = "user_operations";

	/*
	 * getAllUsers() - method Fetch All the Users
	 */
	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<>();
		try {
			userList = userRepository.findAll();
		} catch (Exception e) {
			logger.error("Error in getAllUsers() Method ", e);
		}
		return userList;
	}

	/*
	 * getUserById() - method Fetch User By Input ID
	 */
	public User getUserById(Long id) {
		Optional<User> user = null;
		try {
			user = userRepository.findById(id);
			if (user.isEmpty()) {
				throw new Exception("User not found " + String.valueOf(id));
			}
		} catch (Exception e) {
			logger.error("Error in getUserById() Method ", e);
		}
		return user.get();
	}

	/*
	 * createUser(User user) - method Will Save the User Details
	 */
	public User createUser(User user) {
		User createdUser = null;
		try {
			if (user != null) {
				if (user.getAddress() != null) {
					user.getAddress().forEach(userAddress -> {
						userAddress.setUser(user);
					});
				}
			}
			createdUser = userRepository.save(user);
			kafkaTemplate.send(KAFKA_TOPIC, String.valueOf(createdUser.getId()),
					"Created User: " + createdUser.getId());
		} catch (Exception e) {
			logger.error("Error in createUser() Method ", e);
		}
		return createdUser;
	}

	/*
	 * updateUser(Long id, User user) - method Will Update the User Details
	 */
	public User updateUser(Long id, User user) throws Exception {
		Optional<User> existingUserResult = null;
		User existingUser = null;

		existingUserResult = userRepository.findById(id);
		if (existingUserResult.isEmpty()) {
			throw new Exception("User not found " + String.valueOf(id));
		}
		existingUser = existingUserResult.get();
		existingUser.setName(user.getName());
		existingUser.setEmail(user.getEmail());
		if (user.getAddress() != null) {
			existingUser.getAddress().clear();
			for (Address newAddress : user.getAddress()) {
				newAddress.setUser(existingUser);
				existingUser.getAddress().add(newAddress);
			}
		}
		existingUser = userRepository.save(existingUser);
		kafkaTemplate.send(KAFKA_TOPIC, String.valueOf(existingUser.getId()), "Updated User: " + existingUser.getId());
		return existingUser;
	}

	/*
	 * deleteUser(Long id) - method Will Delete User By Input ID, if user exists
	 */
	public void deleteUser(Long id) throws Exception {
		User user = getUserById(id);
		if (user != null) {
			userRepository.deleteById(id);
			kafkaTemplate.send(KAFKA_TOPIC, String.valueOf(id), "Deleted User: " + id);
		} else {
			throw new Exception("User Not Found " + id);
		}
	}
}
