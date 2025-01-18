package com.springboot.scrum_tracker.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.scrum_tracker.service.EmailService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;

@Hidden
@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	private EmailService emailService;
	

	@GetMapping("/hello")
	@ResponseBody
	public String test(HttpServletRequest request) {
		System.out.println("in hello!!");
		String s=request.getSession().getId();
		
		return "Hello World!!  "+ s;
	}
	
	@GetMapping("/csrf")
	@ResponseBody
	public CsrfToken getCsrfToken(HttpServletRequest request) {
		return (CsrfToken)request.getAttribute("_csrf");
	}
	
	@GetMapping("/greet")
	@ResponseBody
	public String greet(@RequestParam(defaultValue = "John Doe") String name) {
		System.out.println("Hello "+name);
		return "Hello World!! "+ name;
	}
	
	
	@PostMapping("/testmail")
	@ResponseBody
	public ResponseEntity<String> sendMail(@RequestBody Map<String, Object> request){
		emailService.sendEmail("test@test.com", "TEST email sender API", (String)request.get("body"));
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}
	
}
