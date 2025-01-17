package com.springboot.scrum_tracker.dto;

import com.springboot.scrum_tracker.model.IssueType;
import com.springboot.scrum_tracker.model.Priority;
import com.springboot.scrum_tracker.model.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IssueRequestDto {

	@NotBlank(message = "Issue title cannot be blank")
	private String title;
	
	private String description;
	
	private Integer assigneeUserId;
	
	@NotNull(message = "Priority field is required")
	private Priority priority;
	
	@NotNull(message = "Issue Type field is required")
	private IssueType issueType;
	
	private Status status=Status.TO_DO;
	
	@NotNull
	private Integer sprintId;
}
