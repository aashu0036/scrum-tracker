package com.springboot.scrum_tracker.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.scrum_tracker.dto.IssueRequestDto;
import com.springboot.scrum_tracker.model.Issue;
import com.springboot.scrum_tracker.model.Status;
import com.springboot.scrum_tracker.service.IssueService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name="Issue APIs", description = "Add, Edit, Update Status & Delete Issue")
@RestController
@RequestMapping("/api/project/{projectId}/issues")
public class IssueController {
	
	@Autowired
	private IssueService issueService;
	
	@PostMapping
	@Operation(summary = "Create a new Issue")
	public ResponseEntity<Issue> addIssue(@PathVariable Integer projectId, @RequestBody @Valid IssueRequestDto issueRequestDto){
		log.debug("Entering add Issue method");
		Issue issue=issueService.addIssue(issueRequestDto, projectId);
		log.info("New Issue created successfully with id: {}", issue.getId());
		return new ResponseEntity<>(issue, HttpStatus.CREATED);
	}
	
	@PutMapping("/{issueId}")
	@Operation(summary = "Edit Issue")
	public ResponseEntity<String> editIssue(@RequestBody @Valid IssueRequestDto issueRequestDto,
			@PathVariable Integer issueId, @PathVariable Integer projectId){
		log.debug("Entering edit Issue method");
		Issue issue=issueService.editIssueDetails(issueRequestDto, issueId, projectId);
		String s="Issue with id: "+issueId+" edited";
		log.info("Issue with id: {} edited successully", issueId);
		return new ResponseEntity<>(s, HttpStatus.OK);
	}
	
	@DeleteMapping("/{issueId}")
	@Operation(summary = "Delete Issue")
	public ResponseEntity<String> deleteIssue(@PathVariable Integer issueId,
			@PathVariable Integer projectId){
		log.debug("Entering delete Issue method");
		issueService.deleteIssue(issueId, projectId);
		String s="Issue with id: "+issueId+" deleted";
		log.info("Issue with id: {} deleted successully", issueId);
		return new ResponseEntity<>(s, HttpStatus.OK);
	}
	
	@PatchMapping("/{issueId}/status")
	@Operation(summary = "Update Issue Status")
	public ResponseEntity<Issue> updateStatus(@PathVariable Integer issueId, @PathVariable Integer projectId,
			@RequestBody Map<String, String> statusUpdateRequest) {
		log.debug("Entering update Issue status method");
		String status=statusUpdateRequest.get("newStatus");
//		System.out.println(s);
//		System.err.println(Status.valueOf(s));
	    Issue issue=issueService.updateIssueStatus(issueId, projectId, Status.valueOf(status));
	    log.info("Issue with id: {} upated with status {}", issueId, status);
	    return new ResponseEntity<>(issue, HttpStatus.OK);
	}

}
