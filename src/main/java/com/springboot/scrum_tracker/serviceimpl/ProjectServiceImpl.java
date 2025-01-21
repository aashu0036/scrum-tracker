package com.springboot.scrum_tracker.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.scrum_tracker.dto.ProjectRequestDto;
import com.springboot.scrum_tracker.exception.CustomProjectException;
import com.springboot.scrum_tracker.exception.ResourceNotFoundException;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Role;
import com.springboot.scrum_tracker.model.Sprint;
import com.springboot.scrum_tracker.model.Team;
import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.repository.ProjectRepository;
import com.springboot.scrum_tracker.repository.SprintRepository;
import com.springboot.scrum_tracker.service.ProjectService;
import com.springboot.scrum_tracker.service.SprintService;
import com.springboot.scrum_tracker.service.TeamService;
import com.springboot.scrum_tracker.service.UserService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {
	
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private SprintService sprintService;
	
	@Autowired
	private SprintRepository sprintRepo;
	
	
	@Override
	@Transactional
	@CacheEvict(cacheNames = "teamProjects",  key="#projectRequestDto.teamId")
	public Project addProjectAndCreateSprints(ProjectRequestDto projectRequestDto, String username) {
		User requestUser=userService.findByUsername(username);
		Team projectTeam=teamService.findResourceById(projectRequestDto.getTeamId());
		
		//use equals
		if(projectTeam.getManager()!=requestUser && requestUser.getRole()!=Role.ADMIN)
			throw new CustomProjectException("You are not authorised to add project", HttpStatus.UNAUTHORIZED);
		
		Project existingProject=projectRepo.findByName(projectRequestDto.getName());
		if(existingProject!=null) {
			throw new CustomProjectException("Project with name: "+ projectRequestDto.getName()+" already exists", HttpStatus.BAD_REQUEST);
		}
		Project newProject=new Project();
		newProject.setName(projectRequestDto.getName());
		newProject.setDescription(projectRequestDto.getDescription());
		newProject.setStartDate(projectRequestDto.getStartDate());
		
		User projectLead=userService.findResourceById(projectRequestDto.getProjectLeadId());
		
		if(projectLead.getTeam()!=projectTeam) {
			throw new CustomProjectException("User should belong to the project team to be added as Project lead", HttpStatus.BAD_REQUEST);
		}
		
		newProject.setProjectLead(projectLead);
		newProject.setSprintCount(projectRequestDto.getSprintCount());
		newProject.setTeam(projectTeam);
		Project savedProject=projectRepo.save(newProject);
		sprintService.createSprintForProject(savedProject);
		return savedProject;
	}

	@Override
	@Cacheable(value="projects", key="'all'")
	public List<Project> getAllProjects() {
		log.info("Cache miss for projects::all");
		return projectRepo.findAll();
	}

	@Override
	@Transactional
	@Caching(
		evict = {@CacheEvict(cacheNames = "teamProjects",  key="#projectRequestDto.teamId")},
		put = {@CachePut(cacheNames = "project",  key="#projectId")}
	)
	public Project editProjectDetails(ProjectRequestDto projectRequestDto, Integer projectId, String username) {
		User requestUser=userService.findByUsername(username);
		Project project=findResourceById(projectId);
		
		if(requestUser.getRole()!=Role.ADMIN && project.getProjectLead()!=requestUser && project.getTeam().getManager()!= requestUser) {
			throw new CustomProjectException("You are not authorised to edit this project", HttpStatus.UNAUTHORIZED);
		}
		
		project.setName(projectRequestDto.getName());
		
		if(projectRequestDto.getDescription()!=null) //since description is not mandatory in the DTO
			project.setDescription(projectRequestDto.getDescription());
		
		User newProjectLead=userService.findResourceById(projectRequestDto.getProjectLeadId());
		if(newProjectLead.getTeam()!=project.getTeam()) {
			throw new CustomProjectException("User should belong to the project team to be added as Project lead", HttpStatus.UNAUTHORIZED);
		}
		project.setProjectLead(newProjectLead);
		
		Team newTeam=teamService.findResourceById(projectRequestDto.getTeamId());
		project.setTeam(newTeam);
		
		int currentSprintCount=project.getSprintCount();
		int newSprintCount=projectRequestDto.getSprintCount();
		if(newSprintCount<currentSprintCount)
			throw new CustomProjectException("Cannot decrease the sprint count. You need to delete sprints individually", HttpStatus.BAD_REQUEST);
		else {
			LocalDate newSprintStartDate=project.getStartDate().plusWeeks(2*currentSprintCount);
			for(int i=0; i<(newSprintCount-currentSprintCount); i++) {
				Sprint sprint=new Sprint();
				sprint.setName("Sprint "+(currentSprintCount+i+1));
				sprint.setProject(project);
				LocalDate sprintStartDate=newSprintStartDate.plusWeeks(i*2);
				sprint.setStartDate(sprintStartDate);
				sprint.setEndDate(sprintStartDate.plusDays(13));
				sprintRepo.save(sprint);
			}
			project.setSprintCount(newSprintCount);
		}
		return projectRepo.save(project);
	}

	@Override
	@Cacheable(cacheNames = "project", key="#projectId")
	public Project findResourceById(Integer projectId) {
		log.info("Cache miss for project::{}", projectId);
		return projectRepo.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
	}

	@Override
	
	@Caching(
			evict= {@CacheEvict(cacheNames = "teamProjects",  allEntries = true),
					@CacheEvict(cacheNames="project", key="#projectId")}
	)
	public void deleteProject(Integer projectId, String username) {
		User requestUser=userService.findByUsername(username);
		Project project=findResourceById(projectId);
		
		if(requestUser.getRole()!=Role.ADMIN && project.getProjectLead()!=requestUser && project.getTeam().getManager()!= requestUser) {
			throw new CustomProjectException("You are not authorised to edit this project", HttpStatus.UNAUTHORIZED);
		}
		
		projectRepo.delete(project);
	}
} 
