package com.springboot.scrum_tracker.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {

	private String userName;
	private String newPassword;
}
