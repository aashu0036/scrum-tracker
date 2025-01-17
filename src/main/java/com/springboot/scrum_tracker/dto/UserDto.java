package com.springboot.scrum_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDto {

	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	private Integer teamId;
	
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
	private String email;
	
	@NotBlank
	private String userName;
	
	@NotBlank
	private String password;
	
}
