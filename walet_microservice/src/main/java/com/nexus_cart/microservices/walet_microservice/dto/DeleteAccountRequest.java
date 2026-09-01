package com.nexus_cart.microservices.walet_microservice.dto;

import jakarta.validation.constraints.NotBlank;

public class DeleteAccountRequest {
//	@NotBlank(message = "Id is required")
	private String id;

	@NotBlank(message = "Password is required")
	private String currentPassword;

	@NotBlank(message = "Confirm Password is required")
	private String confirmPassword;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the password
	 */
	public String getCurrentPassword() {
		return currentPassword;
	}

	/**
	 * @param password the password to set
	 */
	public void setCurrentPassword(String password) {
		this.currentPassword = password;
	}

	/**
	 * @return the confirmPassowrd
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * @param confirmPassowrd the confirmPassowrd to set
	 */
	public void setConfirmPassword(String confirmPassowrd) {
		this.confirmPassword = confirmPassowrd;
	}

}
