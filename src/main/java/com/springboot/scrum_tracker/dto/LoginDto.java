package com.springboot.scrum_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

	@NotBlank
	private String userName;
	
	@NotBlank
	private String password;
}
