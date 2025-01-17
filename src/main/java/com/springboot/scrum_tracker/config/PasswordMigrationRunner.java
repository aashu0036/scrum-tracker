package com.springboot.scrum_tracker.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.repository.UserRepository;

//@Component
public class PasswordMigrationRunner implements CommandLineRunner {

	@Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		List<User> users=userRepo.findAll();
		
		for(User user: users) {
			String password=user.getPassword();
			if(!password.startsWith("$2a$")) {
				String encodedPassword = passwordEncoder.encode(password);
				user.setPassword(encodedPassword);
				
				userRepo.save(user);
			}
		}
	}  
}
