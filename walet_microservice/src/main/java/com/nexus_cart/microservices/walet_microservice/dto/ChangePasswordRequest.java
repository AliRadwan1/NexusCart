package com.nexus_cart.microservices.walet_microservice.dto;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordRequest {
//	@NotBlank(message = "Id is required")
	private String id;

	@NotBlank(message = "Password is required")
	private String currentPassword;

	@NotBlank(message = "Password is required")
	private String newPassword;

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
	 * @return the currentPassword
	 */
	public String getCurrentPassword() {
		return currentPassword;
	}

	/**
	 * @param currentPassword the currentPassword to set
	 */
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
