package com.springboot.scrum_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamRequestDto {
	
	@NotBlank(message = "Team name cannot be blank")
	private String name;
	
	@NotNull(message = "Manager Id cannot be null")
	private Integer managerId;
	
}
