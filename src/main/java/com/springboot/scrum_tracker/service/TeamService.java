package com.springboot.scrum_tracker.service;

import com.springboot.scrum_tracker.dto.TeamRequestDto;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Team;
import com.springboot.scrum_tracker.model.User;

import java.util.List;

public interface TeamService extends BaseInterface<Team> {

	Team addTeam(TeamRequestDto teamRequestDto);
	
	List<Project> getTeamProjects(Integer projectId);
	
	List<User> getTeamMembers(Integer teamId);
	
	Team editTeam(Integer teamId, TeamRequestDto teamRequestDto);
	
	void deleteTeam(Integer teamId);
}
