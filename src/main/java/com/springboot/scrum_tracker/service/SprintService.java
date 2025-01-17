package com.springboot.scrum_tracker.service;

import java.util.List;

import com.springboot.scrum_tracker.model.Issue;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Sprint;

public interface SprintService extends BaseInterface<Sprint> {

	void createSprintForProject(Project project);
	
	List<Issue> getSprintIssues(Integer sprintId);
}
