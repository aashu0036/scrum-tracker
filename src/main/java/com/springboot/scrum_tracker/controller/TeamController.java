package com.springboot.scrum_tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.service.TeamService;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name="Team APIs", description = "Get team projects and members")
@RestController
@RequestMapping("/api/teams")
public class TeamController {
	
	
	@Autowired
	private TeamService teamService;
	
	@GetMapping("/{teamId}/projects")
	public ResponseEntity<List<Project>> getTeamProjects(@PathVariable Integer teamId){
		log.debug("In getTeamProject method");;
		List<Project> projects=teamService.getTeamProjects(teamId);
		HttpStatus status = projects.isEmpty()?HttpStatus.NO_CONTENT:HttpStatus.OK;
		return new ResponseEntity<>(projects, status);
	}
	
	@GetMapping("/{teamId}/members")
	public ResponseEntity<List<User>> getTeamMembers(@PathVariable Integer teamId, @AuthenticationPrincipal UserDetails ud){
		log.debug("In getTeamMembers method");;
		List<User> members=teamService.getTeamMembers(teamId);
		HttpStatus status = members.isEmpty()?HttpStatus.NO_CONTENT:HttpStatus.OK;
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		System.out.println("FROM  SEc COntext: "+authentication.toString());
		System.out.println("PRINCIPALLLL from anno: " +ud);
		return new ResponseEntity<>(members, status);
	}
}
