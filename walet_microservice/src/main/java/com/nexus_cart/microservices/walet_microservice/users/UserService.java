package com.nexus_cart.microservices.walet_microservice.users;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexus_cart.microservices.walet_microservice.dto.ChangePasswordRequest;
import com.nexus_cart.microservices.walet_microservice.dto.DeleteAccountRequest;
import com.nexus_cart.microservices.walet_microservice.dto.LoginRequest;
import com.nexus_cart.microservices.walet_microservice.dto.RegisterRequest;
import com.nexus_cart.microservices.walet_microservice.dto.UpdateUserInfoRequest;
import com.nexus_cart.microservices.walet_microservice.exceptions.InvalidArgumentException;
import com.nexus_cart.microservices.walet_microservice.exceptions.InvalidNewPasswordException;
import com.nexus_cart.microservices.walet_microservice.exceptions.UserAlreadyExistsException;
import com.nexus_cart.microservices.walet_microservice.exceptions.UserAuthenticationException;
import com.nexus_cart.microservices.walet_microservice.exceptions.UserNotFoundException;
import com.nexus_cart.microservices.walet_microservice.wallets.WalletService;

/**
 * Service managing user account lifecycles, authentication, profile updates, 
 * and password management[cite: 1].
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WalletService walletService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Registers a new user account, normalizes their email address to lowercase,
	 * encodes their password using BCrypt, and automatically initializes a wallet[cite: 1, 3].
	 *
	 * @param firstName The user's first name.
	 * @param lastName  The user's last name.
	 * @param email     The user's raw email address (will be trimmed and lowercased).
	 * @param password  The user's plain-text password (will be hashed).
	 * @throws UserAlreadyExistsException If a user with the normalized email already exists.
	 */
	@Transactional
	public void registerUser(RegisterRequest request) {
		String normalizedEmail = (request.getEmail() != null) ? request.getEmail().trim().toLowerCase() : null;

		Optional<User> user = userRepository.findByEmail(normalizedEmail);

		if (user.isPresent()) {
			throw new UserAlreadyExistsException("A user already exists with this email");
		}

		User newUser = new User();
		newUser.setFirstName(request.getFirstName());
		newUser.setLastName(request.getLastName());
		newUser.setEmail(normalizedEmail);
		newUser.setPassword(passwordEncoder.encode(request.getPassword()));

		userRepository.save(newUser);

		walletService.createWallet(newUser.getId());

	}

	/**
	 * Authenticates a user against their stored credentials using normalized email lookup
	 * and password hash comparison[cite: 1].
	 *
	 * @param email    The user's email address.
	 * @param password The user's plain-text password attempt.
	 * @return The authenticated {@link User} entity[cite: 1].
	 * @throws UserNotFoundException       If no user account matches the provided email.
	 * @throws UserAuthenticationException If the password does not match or is null.
	 */
	public User loginUser(LoginRequest request) {
		String normalizedEmail = (request.getEmail() != null) ? request.getEmail().trim().toLowerCase() : null;

		User user = userRepository.findByEmail(normalizedEmail)
				.orElseThrow(() -> new UserNotFoundException("No user found with this email"));

		if (request.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new UserAuthenticationException("Invalid email or password");
		}

		return user;
	}

	/**
	 * Updates a user's profile information while enforcing email uniqueness.
	 *
	 * @param request The {@link UpdateUserInfoRequest} containing the updated profile details.
	 * @throws UserNotFoundException      If no user exists with the specified ID.
	 * @throws UserAlreadyExistsException If the new email is already in use by another account.
	 */
	@Transactional
	public void editUserInfo(UpdateUserInfoRequest request) {
		User user = userRepository.findById(request.getId())
				.orElseThrow(() -> new UserNotFoundException("No User found with this id: " + request.getId()));

		String normalizedEmail = (request.getEmail() != null) ? request.getEmail().trim().toLowerCase() : null;

		// Verify email availability if changing addresses
		if (!normalizedEmail.equalsIgnoreCase(user.getEmail())) {
			Optional<User> existingUser = userRepository.findByEmail(normalizedEmail);

			if (existingUser.isPresent()) {
				throw new UserAlreadyExistsException("A user already exists with this email");
			}
		}

		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(normalizedEmail);
	}

	/**
	 * Updates a user's password after confirming the new password differs from the current active password.
	 *
	 * @param request The {@link ChangePasswordRequest} containing the user ID and new plain-text password.
	 * @throws UserNotFoundException       If no user exists with the specified ID.
	 * @throws InvalidNewPasswordException If the new password is identical to the current password.
	 */
	@Transactional
	public void changePassword(ChangePasswordRequest request) {
		User user = userRepository.findById(request.getId())
				.orElseThrow(() -> new UserNotFoundException("No User found with this id: " + request.getId()));

		// 1. Verify that the user provided their correct current password
	    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
	        throw new UserAuthenticationException("Current password is incorrect");
	    }

	    // 2. Ensure new password is not identical to current password
	    if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
	        throw new InvalidNewPasswordException("New password cannot be the same as the old password");
	    }

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));

		userRepository.save(user);
	}
	
	@Transactional
	public void deleteAccount(DeleteAccountRequest request) {

		// 1. Fast plain-text validation: ensure confirmation password matches
		if (!Objects.equals(request.getCurrentPassword(), request.getConfirmPassword())) {
			throw new InvalidArgumentException("Confirmation password doesn't match");
		}
		
		// 2. Fetch user entity
		User user = userRepository.findById(request.getId())
				.orElseThrow(() -> new UserNotFoundException("No User found with this id: " + request.getId()));

		// 3. verify provided password against BCrypt hash in DB
	    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
	        throw new UserAuthenticationException("Current password is incorrect");
	    }
	    
	    // 4. Cleanup user resources
	    walletService.deleteWallet(user.getId());
	    userRepository.deleteById(user.getId()); 
	}

	/**
	 * Retrieves a user entity by its unique identifier.
	 *
	 * @param id The unique identifier of the user.
	 * @return The matching {@link User} entity.
	 * @throws UserNotFoundException If no user exists with the specified ID.
	 */
	@Transactional(readOnly = true)
	public User getUserId(String id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("No User found with this id: " + id));
	}
}