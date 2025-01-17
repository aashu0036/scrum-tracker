package com.springboot.scrum_tracker.service;

import com.springboot.scrum_tracker.dto.IssueRequestDto;
import com.springboot.scrum_tracker.dto.ProjectRequestDto;
import com.springboot.scrum_tracker.model.Issue;
import com.springboot.scrum_tracker.model.IssueType;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Status;

import java.util.List;
import java.util.Optional;

public interface IssueService extends BaseInterface<Issue> {

	Issue addIssue(IssueRequestDto issueDto, Integer projectId);
	
	List<Issue> getIssuesByAssignee(Integer assigneeUserId);
	
	List<Issue> getIssuesByProject(Integer projectId);
	
	Issue editIssueDetails(IssueRequestDto issueRequestDto, Integer issueId, Integer projectId);
	
	void deleteIssue(Integer issueId, Integer projectId);
	
	Issue updateIssueStatus(Integer issueId, Integer projectId, Status status);
	
}
