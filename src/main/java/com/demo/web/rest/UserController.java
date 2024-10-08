package com.demo.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.web.service.UserService;
import com.demo.web.vo.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	/*
	 * Rest Call Method to Get All Users
	 */
	@GetMapping("/GetAllUsers")
	public ResponseEntity<?> getAllUsers() {
		long startTime = System.currentTimeMillis();
		List<User> users = new ArrayList<>();
		try {
			logger.info("Get All Users Rest Method Call Start");
			users = userService.getAllUsers();
			logger.info("Get All Users Rest Method Call Done,Time Taken:{}ms",
					(System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			logger.error("Error In Get All User Rest Method Call", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	/*
	 * Rest Call Method to Get User By ID
	 */
	@GetMapping("/GetUserByID/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		long startTime = System.currentTimeMillis();
		User user = null;
		try {
			logger.info("Get User By ID Rest Method Call Start");
			user = userService.getUserById(id);
			logger.info("Get User By ID Rest Method Call Done,Time Taken:{}ms",
					(System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			logger.error("Error In Get All User Rest Method Call", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	/*
	 * Rest Call Method to Create User
	 */
	@PostMapping("/CreateUser")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		long startTime = System.currentTimeMillis();
		User resultUser = null;
		try {
			logger.info("Create User Rest Method Call Start");
			resultUser = userService.createUser(user);
			logger.info("Create User ID Rest Method Call Done,Time Taken:{}ms",
					(System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			logger.error("Error In Create User Rest Method Call", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(resultUser, HttpStatus.OK);
	}
	
	/*
	 * Rest Call Method to Update User By ID
	 */
	@PutMapping("/UpdateUser/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
		long startTime = System.currentTimeMillis();
		User resultUser = null;
		try {
			logger.info("Update User Rest Method Call Start");
			resultUser = userService.updateUser(id, user);
			logger.info("Update User  Rest Method Call Done,Time Taken:{}ms",
					(System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			logger.error("Error In Update User Rest Method Call", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(resultUser, HttpStatus.OK);
	}

	/*
	 * Rest Call Method to Delete User By ID
	 */
	@DeleteMapping("/Delete/{id}")
	public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
		long startTime = System.currentTimeMillis();
		try {
			logger.info("Delete User Rest Method Call Start");
			userService.deleteUser(id);
			logger.info("Delete User  Rest Method Call Done,Time Taken:{}ms",
					(System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			logger.error("Error In Delete User Rest Method Call", e);
			return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
}
