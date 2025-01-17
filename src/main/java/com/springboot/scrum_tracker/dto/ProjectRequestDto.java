package com.springboot.scrum_tracker.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProjectRequestDto {

	@NotBlank(message = "Project name cannot be blank")
	private String name;
	
	private String description;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Start Date is required")
	private LocalDate startDate;
	
	@NotNull(message = "Project lead user id cannot be null")
	private Integer projectLeadId;
	
	
	@Min(value = 0)
	@Max(value = 52)
	private Integer sprintCount=0;
	
	@NotNull(message = "Team id cannot be null")
	private Integer teamId;
	
}
