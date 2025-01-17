package com.springboot.scrum_tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.scrum_tracker.dto.ChangePasswordDto;
import com.springboot.scrum_tracker.model.Issue;
import com.springboot.scrum_tracker.model.Role;
import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.service.IssueService;
import com.springboot.scrum_tracker.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Tag(name="User APIs", description = "Get list of issues assigned to user")
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IssueService issueService;
	
	@GetMapping("/{userId}/issues")
	public ResponseEntity<List<Issue>> getUserIssues(@PathVariable Integer userId){
		List<Issue> issues= issueService.getIssuesByAssignee(userId);
		HttpStatus status = issues.isEmpty()?HttpStatus.NO_CONTENT:HttpStatus.OK;
		return new ResponseEntity<>(issues, status);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<User> getUserDetails(@PathVariable Integer userId, @AuthenticationPrincipal UserDetails userDetails){
		//Using a set here so that multiple roles can be included in future
//		Set<String> roles = userDetails.getAuthorities().stream()
//			     .map(r -> r.getAuthority()).collect(Collectors.toSet());
		User user=userService.getUserById(userId, userDetails.getUsername());
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@PatchMapping("/{userId}/password")
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto, @AuthenticationPrincipal UserDetails userDetails){
		//Using a set here so that multiple roles can be included in future
//		Set<String> roles = userDetails.getAuthorities().stream()
//			     .map(r -> r.getAuthority()).collect(Collectors.toSet());
		String authority=userDetails.getAuthorities().stream().map(a -> a.getAuthority()).findFirst().orElse(null);
		System.out.println("authority: "+authority);
		userService.changePassword(changePasswordDto, userDetails.getUsername(), authority);
		String message="Password successfully changed";
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	
}
