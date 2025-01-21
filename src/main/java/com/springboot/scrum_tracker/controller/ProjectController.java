package com.springboot.scrum_tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.scrum_tracker.dto.ProjectHealthReport;
import com.springboot.scrum_tracker.dto.ProjectRequestDto;
import com.springboot.scrum_tracker.model.Issue;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.repository.UserRepository;
import com.springboot.scrum_tracker.service.IssueService;
import com.springboot.scrum_tracker.service.ProjectReportService;
import com.springboot.scrum_tracker.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/projects")
@Tag(name="Project APIs", description = "Add, Edit & Delete Project, Get Issues in a Project")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private IssueService issueService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProjectReportService projectReportService;
	
	
	@GetMapping("/{projectId}/issues")
	@Operation(summary = "Get list of all issues in project")
	public ResponseEntity<List<Issue>> getProjectIssues(@PathVariable Integer projectId) {
		log.debug("Entering getProjectIssues method");
		List<Issue> issues=issueService.getIssuesByProject(projectId);
		HttpStatus status = issues.isEmpty()?HttpStatus.NO_CONTENT:HttpStatus.OK;
		return new ResponseEntity<>(issues, status);
	}

	@PostMapping
	@Operation(summary = "Create a New Project (for Team Manager and Admin)")
	public ResponseEntity<Project> saveProject(@RequestBody @Valid ProjectRequestDto projectRequestDto,
			@AuthenticationPrincipal UserDetails userDetails){
		log.debug("Entering saveProject method");
//		System.out.println(userDetails.getId());
		System.out.println(userDetails.getAuthorities());
		Project project =projectService.addProjectAndCreateSprints(projectRequestDto, userDetails.getUsername());
		log.info("New Project created with id: {}", project.getId());
		return new ResponseEntity<>(project, HttpStatus.CREATED);
	}
	
	@GetMapping("/{projectId}")
	@Operation(summary = "View Project Details")
	public ResponseEntity<Project> getProjectDetails(@PathVariable Integer projectId){
		log.debug("Entering getProjectDetails method");
		Project project=projectService.findResourceById(projectId);
		return new ResponseEntity<>(project, HttpStatus.OK);
	}
	
	@PutMapping("/{projectId}")
	@Operation(summary = "Edit a Project (for Team Manager, Project Lead and Admin)")
	public ResponseEntity<Project> editProjectDetails(@RequestBody @Valid ProjectRequestDto projectRequestDto, @PathVariable Integer projectId,
			@AuthenticationPrincipal UserDetails userDetails){
		log.debug("Entering editProjectDetails method");
		Project editedProject=projectService.editProjectDetails(projectRequestDto, projectId, userDetails.getUsername());
		log.info("Project with id: {} successully edited", projectId);
		return new ResponseEntity<>(editedProject, HttpStatus.OK);
	}
	
	@DeleteMapping("/{projectId}")
	@Operation(summary = "Edit a Project (for Team Manager, Project Lead and Admin)")
	public ResponseEntity<String> deleteProject(@PathVariable Integer projectId,
			@AuthenticationPrincipal UserDetails userDetails){
		log.debug("Entering deleteProject method");
		projectService.deleteProject(projectId, userDetails.getUsername());
		String message="Project with id: "+projectId+" successfully deleted";
		log.info(message);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@GetMapping("/{projectId}/report")
	@Operation(summary = "Get Health report for Project)")
	public ResponseEntity<ProjectHealthReport> getProjectReport(@PathVariable Integer projectId){
		log.debug("Entering getProjectReport method");
		ProjectHealthReport report= projectReportService.getProjectHealthReport(projectId);
		log.info("Project report fetched");
		return new ResponseEntity<>(report, HttpStatus.OK);
	}
	
	@GetMapping("/{projectId}/get")
	@Operation(summary = "Get email with Project Health Status")
	public ResponseEntity<String> sendReport(@PathVariable Integer projectId,
			@AuthenticationPrincipal UserDetails userDetails){
		log.debug("Entering sendReport method");
		projectReportService.sendProjectReport(projectId, userDetails.getUsername());
		log.info("Project Report sent");
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}
}
