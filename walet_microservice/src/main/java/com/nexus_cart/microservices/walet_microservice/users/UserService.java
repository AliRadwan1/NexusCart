package com.nexus_cart.microservices.walet_microservice.users;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexus_cart.microservices.walet_microservice.exceptions.*;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	// Register User
	public void registerUser(String firstName, String lastName, String email, String password) {
		Optional<User> user = userRepository.findByEmail(email);

		if (user.isPresent()) {
			throw new UserAlreadyExistsException("A user already exists with this email");
		}
		
		String newUserID = UUID.randomUUID().toString();
		User newUser = new User(newUserID, firstName, lastName, email, password);

		userRepository.save(newUser);
	}

	// Login
	public User loginUser(String email, String password){
		Optional<User> user = userRepository.findByEmail(email);

		if (user.isEmpty()) {
			throw new UserNotFoundException("No user found with this email");
		}

		if (!password.equals(user.get().getPassword())) {
			throw new UserAuthenticationException("Password is incorrect");
		}

		return user.get();
	}

	// Retrieve user by id
	public User getUserId(String id) {
		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty()) {
			throw new UserNotFoundException("No user found with this id");
		}

		return user.get();
	}
}
