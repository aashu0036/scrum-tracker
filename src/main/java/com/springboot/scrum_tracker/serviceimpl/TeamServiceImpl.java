package com.springboot.scrum_tracker.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.scrum_tracker.dto.TeamRequestDto;
import com.springboot.scrum_tracker.exception.CustomException;
import com.springboot.scrum_tracker.exception.ResourceNotFoundException;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Team;
import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.repository.ProjectRepository;
import com.springboot.scrum_tracker.repository.TeamRepository;
import com.springboot.scrum_tracker.repository.UserRepository;
import com.springboot.scrum_tracker.service.TeamService;
import com.springboot.scrum_tracker.service.UserService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TeamServiceImpl implements TeamService {
		
	@Autowired
	private TeamRepository teamRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	

	@Override
	public Team addTeam(TeamRequestDto teamRequestDto) {
		Team newTeam=new Team();
		User teamManager=userService.findResourceById(teamRequestDto.getManagerId());
		if(teamManager.getTeam()!=null) {
			throw new CustomException("User belongs to a different team", HttpStatus.BAD_REQUEST);
		}
		newTeam.setName(teamRequestDto.getName());
		newTeam.setManager(teamManager);
		teamManager.setTeam(newTeam);
		userRepo.save(teamManager);
		return teamRepo.save(newTeam);
	}


	@Override
	@Cacheable(value="teamProjects", key="#teamId",  unless = "#result==null or #result.size==0")
	public List<Project> getTeamProjects(Integer teamId) {
		log.info("Cache miss for teamProjects::{}", teamId);
		Team team = findResourceById(teamId);
		return projectRepo.findByTeam(team);
	}


	@Override
//	Not caching teamMembers since users may/may not have teamId at time of creation and
//  adding/deletion of 1 user will require eviction of all teamMember caches, although an argument can be made on
//  frequency and eventual consistency of this particular operation
//  As they say, there are 2 hard things in programming..
	public List<User> getTeamMembers(Integer teamId) {
		Team team=findResourceById(teamId);
		return team.getMembers();
	}


	@Override
	public Team editTeam(Integer teamId, TeamRequestDto teamRequestDto) {
		Team team=findResourceById(teamId);
		String newTeamName=teamRequestDto.getName();
		
		if(teamRequestDto.getManagerId()!=null) {
			User newManager=userService.findResourceById(teamRequestDto.getManagerId());
			if(newManager.getTeam()!=team) {
				throw new CustomException("New Manager should belong to the team", HttpStatus.BAD_REQUEST);
			}
			team.setManager(newManager);
		}
		
		if(newTeamName!=null)
				team.setName(newTeamName);
		return teamRepo.save(team);
	}


	@Override
	@CacheEvict(cacheNames = "teamProjects", key="#teamId")
	public void deleteTeam(Integer teamId) {
		Team team=findResourceById(teamId);
		teamRepo.unassignManagerFromTeam(teamId);
		userRepo.unassignTeamFromUsers(teamId);
		teamRepo.delete(team);
	}


	@Override
	public Team findResourceById(Integer teamId) {
		return teamRepo.findById(teamId).orElseThrow(()-> new ResourceNotFoundException("Team", "id:", teamId));
	}

}
