package com.springboot.scrum_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.springboot.scrum_tracker.model.Team;
import com.springboot.scrum_tracker.repository.ProjectRepository;
import com.springboot.scrum_tracker.repository.TeamRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.springboot.scrum_tracker"})
public class ScrumTrackerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ScrumTrackerApplication.class, args);
		log.info("Application Started!");
		
//		TeamRepository teamRepo=context.getBean(TeamRepository.class);
//		ProjectRepository projectRepo=context.getBean(ProjectRepository.class);
//		
//		System.out.println(teamRepo.findByManagerUserName("aashu.khare"));
//		
//		System.out.println(projectRepo.findByProjectLeadUserName("john.wick"));
	}

}
