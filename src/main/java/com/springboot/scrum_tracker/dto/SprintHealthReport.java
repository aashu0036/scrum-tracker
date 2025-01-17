package com.springboot.scrum_tracker.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SprintHealthReport {

	private String name;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private int completionPercentage;
	
	private int totalIssues;
	
	private int blockedIssues;
	
	private int overdueIssues;
	
	private int sprintDaysRemaining;
	
	private int bugRatio;
}
