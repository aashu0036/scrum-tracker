package com.springboot.scrum_tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.scrum_tracker.dto.SprintHealthReport;
import com.springboot.scrum_tracker.model.Issue;
import com.springboot.scrum_tracker.service.SprintReportService;
import com.springboot.scrum_tracker.service.SprintService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name="Admin APIs", description = "Get sprint issues list and sprint health report")
@RestController
@RequestMapping("/api/sprints")
public class SprintController {
	
	@Autowired
	private SprintService sprintService;
	
	@Autowired
	private SprintReportService sprintReportService;

	@GetMapping("/{sprintId}/issues")
	public ResponseEntity<List<Issue>> getSprintIssues(@PathVariable Integer sprintId) {
		List<Issue> issues= sprintService.getSprintIssues(sprintId);
		HttpStatus status = issues.isEmpty()?HttpStatus.NO_CONTENT:HttpStatus.OK;
		return new ResponseEntity<>(issues, status);
	}
	
	@GetMapping("/{sprintId}/report")
	public ResponseEntity<SprintHealthReport> getSprintReport(@PathVariable Integer sprintId) {
		SprintHealthReport healthReport=sprintReportService.getSprintHealthReport(sprintId);
		return new ResponseEntity<>(healthReport, HttpStatus.OK);
	}
}
