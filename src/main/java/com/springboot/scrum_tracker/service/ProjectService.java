package com.springboot.scrum_tracker.service;

import java.util.List;

import com.springboot.scrum_tracker.dto.ProjectRequestDto;
import com.springboot.scrum_tracker.model.Project;

public interface ProjectService extends BaseInterface<Project> {

	Project addProjectAndCreateSprints(ProjectRequestDto projectRequestDto, String username);
	
	List<Project> getAllProjects();
	
	Project editProjectDetails(ProjectRequestDto projectRequestDto, Integer projectId, String username);
	
	void deleteProject(Integer projectId, String username);
	
//	void sendProjectReport(Integer projectId, String username);
}
