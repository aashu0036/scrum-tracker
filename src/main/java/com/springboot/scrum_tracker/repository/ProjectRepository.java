package com.springboot.scrum_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Team;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer>  {
	
	List<Project> findByTeam(Team team);
	
	Project findByProjectLeadUserName(String username);
	
	Project findByName(String name);
}
