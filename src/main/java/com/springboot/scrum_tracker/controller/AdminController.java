package com.springboot.scrum_tracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.scrum_tracker.dto.ChangePasswordDto;
import com.springboot.scrum_tracker.dto.TeamRequestDto;
import com.springboot.scrum_tracker.dto.UserDto;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Team;
import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.service.ProjectService;
import com.springboot.scrum_tracker.service.TeamService;
import com.springboot.scrum_tracker.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name="Admin APIs", description = "Create, Edit & View Teams, Promote and View all Users, View all Projects")
@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private ProjectService projectService;
	
	@GetMapping("/users")
	@Operation(summary = "Get list of all users across all teams")
	public ResponseEntity<List<User>> getAllUsers(){
		log.debug("Entering getAllUser method");
		List<User> users=userService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping("/projects")
	@Operation(summary = "Get list of all projects across all teams")
	public ResponseEntity<List<Project>> getAllProjects(){
		log.debug("Entering getAllProjects method");
		List<Project> projects=projectService.getAllProjects();
		return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}
	
	@DeleteMapping("users/{userId}")
	@Operation(summary = "Delete a user")
	public ResponseEntity<User> deleteUser(@PathVariable Integer userId){
		log.debug("Entering deleteUser method for user with id: {}", userId);
		User deletedUser=userService.deleteUser(userId);
		log.info("User with user id: {} deleted", userId);
		return new ResponseEntity<>(deletedUser, HttpStatus.OK);
	}
	
	@PostMapping("/register")
	@Operation(summary = "Register an Admin user")
	public ResponseEntity<User> registerAdminUser(@RequestBody @Valid UserDto adminUserDto){
		log.debug("Entering registerAdminUser method");
		User adminUser=userService.addAdminUser(adminUserDto);
		log.info("Registered Admin user with username: {}",adminUser.getUserName());
		return new ResponseEntity<>(adminUser, HttpStatus.CREATED);
	}
	
	@PatchMapping("/users/{userId}/promote")
	@Operation(summary = "Promote a User to Admin")
	public ResponseEntity<User> promoteToAdmin(@PathVariable Integer userId){
		log.debug("Entering promoteToAdmin method");
		User promotedUser=userService.promoteToAdmin(userId);
		log.info("User with user id: {} promoted to Admin", userId);
		return new ResponseEntity<>(promotedUser, HttpStatus.OK);
	}
	
	@PatchMapping("/users/password")
	@Operation(summary = "Change users' password")
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto, @AuthenticationPrincipal UserDetails userDetails){
		log.debug("Entering changePassword method");
		String authority=userDetails.getAuthorities().stream().map(a -> a.getAuthority()).findFirst().orElse(null);
		userService.changePassword(changePasswordDto, userDetails.getUsername(), authority);
		log.info("Password changed for user with username: {}", changePasswordDto.getUserName());
		String message="Password changed for user: "+changePasswordDto.getUserName();
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@PostMapping("/teams")
	@Operation(summary = "Create a new team")
	public ResponseEntity<Team> saveTeam(@RequestBody @Valid TeamRequestDto teamRequestDto){
		log.debug("Entering saveTeam method");
		Team team=teamService.addTeam(teamRequestDto);
		log.info("New team: {} added", teamRequestDto.getName());
		return new ResponseEntity<Team>(team, HttpStatus.CREATED);
	}
	
	@PutMapping("/teams/{teamId}")
	@Operation(summary = "Edit team details")
	public ResponseEntity<Team> editTeam(@PathVariable Integer teamId, @RequestBody TeamRequestDto teamRequestDto){
		log.debug("Entering editTeam method");
		Team editedTeam=teamService.editTeam(teamId, teamRequestDto);
		log.info("Team with id: {} edited", teamId);
		return new ResponseEntity<Team>(editedTeam, HttpStatus.OK);
	}
	
	@DeleteMapping("/teams/{teamId}")
	@Operation(summary = "Delete team")
	public ResponseEntity<String> deleteTeam(@PathVariable Integer teamId){
		log.debug("Entering deleteTeam method");
		teamService.deleteTeam(teamId);
		log.info("Team with id: {} deleted", teamId);
		String message="Team with id: "+ teamId+" deleted Successfully";
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}

