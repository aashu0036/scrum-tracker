package com.springboot.scrum_tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.scrum_tracker.dto.LoginDto;
import com.springboot.scrum_tracker.dto.UserDto;
import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name="Public APIs", description = "Register User, login and health-check")
@RestController
@RequestMapping("/api/public")
public class PublicController {
	
//	@Autowired
//	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	@Operation(summary = "Register a User (with User role)")
	public ResponseEntity<User> registerUser(@RequestBody @Valid UserDto userDto){
		log.debug("Entering Register User method");
		User user=userService.addUser(userDto);
		log.info("User with username: {} registered successfully", userDto.getUserName());
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	@Operation(summary = "Login (fetch JWT)")
	public ResponseEntity<String> loginUser(@RequestBody @Valid LoginDto loginDto){
		log.debug("Entering Login method");
		String s=userService.verifyUser(loginDto);
		log.info("Log in successful!");
		return new ResponseEntity<>(s, HttpStatus.OK);
	}
	
	@GetMapping("/health-check")
	@Operation(summary = "Health check")
    public ResponseEntity<String> healthCheck() {
		log.debug("Entering health check method");
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }
}
