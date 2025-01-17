package com.springboot.scrum_tracker.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.scrum_tracker.dto.IssueRequestDto;
import com.springboot.scrum_tracker.exception.CustomIssueException;
import com.springboot.scrum_tracker.exception.ResourceNotFoundException;
import com.springboot.scrum_tracker.model.Issue;
import com.springboot.scrum_tracker.model.IssueType;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Sprint;
import com.springboot.scrum_tracker.model.Status;
import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.repository.IssueRepository;
import com.springboot.scrum_tracker.repository.ProjectRepository;
import com.springboot.scrum_tracker.repository.SprintRepository;
import com.springboot.scrum_tracker.repository.UserRepository;
import com.springboot.scrum_tracker.service.IssueService;
import com.springboot.scrum_tracker.service.SprintService;
import com.springboot.scrum_tracker.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {
	
	@Autowired
	private IssueRepository issueRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private SprintService sprintService;

	@Override
	public Issue addIssue(IssueRequestDto issueDto, Integer projectId) {
		Issue newIssue=new Issue();
		
		Project project=projectRepo.findById(projectId).orElseThrow(()->new ResourceNotFoundException("Project", "Id", projectId));
		
		newIssue.setTitle(issueDto.getTitle());
		newIssue.setDescription(issueDto.getDescription());
		//if no assignee, set Project Lead as assignee by default
		
		if(issueDto.getAssigneeUserId()==null)
			newIssue.setAssignee(project.getProjectLead());
		else {
			User assignee=userService.findResourceById(issueDto.getAssigneeUserId());
			if(assignee.getTeam()!=project.getTeam()) {
				throw new CustomIssueException("Assignee should belong to the same team as the project", HttpStatus.BAD_REQUEST);
			}
			
			newIssue.setAssignee(assignee);
		}
		
		newIssue.setPriority(issueDto.getPriority());
		newIssue.setIssueType(issueDto.getIssueType());
		newIssue.setStatus(issueDto.getStatus());
		newIssue.setProject(project);
		Sprint sprint = sprintService.findResourceById(issueDto.getSprintId());
		
		if (!sprint.getProject().getId().equals(projectId)) {
			throw new CustomIssueException("Sprint does not belong to specified project", HttpStatus.BAD_REQUEST);
		}
		newIssue.setSprint(sprint);
		return issueRepo.save(newIssue);
	}

	@Override
	public List<Issue> getIssuesByAssignee(Integer assigneeUserId) {
		User user=userService.findResourceById(assigneeUserId);
		return issueRepo.findByAssignee(user);
	}

	@Override
	public List<Issue> getIssuesByProject(Integer projectId) {
		Project project=projectRepo.findById(projectId).orElseThrow(()-> new ResourceNotFoundException("Project", "id", projectId));
		return issueRepo.findByProject(project);
	}

	@Override
	public Issue findResourceById(Integer issueId) {
		return issueRepo.findById(issueId).orElseThrow(() -> new ResourceNotFoundException("Issue", "id", issueId));
	}

	@Override
	public Issue editIssueDetails(IssueRequestDto issueRequestDto, Integer issueId, Integer projectId) {
		Issue issue=findResourceById(issueId);
		validateIssueBelongsToProject(issue, projectId);
		issue.setTitle(issueRequestDto.getTitle());
		issue.setDescription(issueRequestDto.getDescription());
		issue.setPriority(issueRequestDto.getPriority());
		issue.setIssueType(issueRequestDto.getIssueType());
		issue.setStatus(issueRequestDto.getStatus());
		Sprint newSprint=sprintService.findResourceById(issueRequestDto.getSprintId());
		if (!newSprint.getProject().equals(issue.getProject())) {
			throw new CustomIssueException("Sprint must belong to the project", HttpStatus.BAD_REQUEST);
		}
		issue.setSprint(newSprint);
		
		User newAssignee=userService.findResourceById(issueRequestDto.getAssigneeUserId());
		if(newAssignee.getTeam()!=issue.getProject().getTeam())
			throw new CustomIssueException("Assignee must belong to the same team as project", HttpStatus.BAD_REQUEST);
		
		issue.setAssignee(newAssignee);
		return issueRepo.save(issue);
	}

	@Override
	public void deleteIssue(Integer issueId, Integer projectId) {
		Issue issue=findResourceById(issueId);
		validateIssueBelongsToProject(issue, projectId);
		issue.getProject().getIssues().remove(issue);
		issue.getSprint().getIssues().remove(issue);
		issueRepo.delete(issue);
		
	}

	@Override
	public Issue updateIssueStatus(Integer issueId, Integer projectId, Status status) {
		Issue issue=findResourceById(issueId);
		validateIssueBelongsToProject(issue, projectId);
		issue.setStatus(status);
		return issueRepo.save(issue);
	}
	
	private void validateIssueBelongsToProject(Issue issue, Integer projectId) {
		if(!issue.getProject().getId().equals(projectId)) {
			String message="Issue id: "+issue.getId()+" not found for project: "+projectId;
			throw new CustomIssueException(message, HttpStatus.BAD_REQUEST);
		}
	}

}
