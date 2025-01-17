package com.springboot.scrum_tracker.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.springboot.scrum_tracker.dto.ProjectHealthReport;
import com.springboot.scrum_tracker.exception.CustomProjectException;
import com.springboot.scrum_tracker.model.IssueType;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Role;
import com.springboot.scrum_tracker.model.Status;
import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.repository.IssueRepository;
import com.springboot.scrum_tracker.repository.SprintRepository;
import com.springboot.scrum_tracker.service.EmailService;
import com.springboot.scrum_tracker.service.IssueService;
import com.springboot.scrum_tracker.service.ProjectReportService;
import com.springboot.scrum_tracker.service.ProjectService;
import com.springboot.scrum_tracker.service.UserService;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProjectReportServiceImpl implements ProjectReportService {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private IssueRepository issueRepo;
	
	@Autowired
	private SprintRepository sprintRepo;

	@Autowired
	private IssueService issueService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public ProjectHealthReport getProjectHealthReport(Integer projectId) {
		Project project= projectService.findResourceById(projectId); 
		ProjectHealthReport healthReport=new ProjectHealthReport();
		User projectLead=project.getProjectLead();
		healthReport.setProjectName(project.getName());
		healthReport.setTeam(project.getTeam().getName());
		healthReport.setProjectLead(projectLead.getFirstName() +" "+projectLead.getLastName());
		healthReport.setTotalSprints(project.getSprintCount());
		Tuple sprint = sprintRepo.getCurrentSprintIdAndName(projectId);
		String sprintDetails = "id: "+sprint.get("id", Integer.class)+"  "+sprint.get("name", String.class);
		
		healthReport.setOngoingSprint(sprintDetails);
		healthReport.setStartDate(project.getStartDate());
		healthReport.setOverdueIssuesCount(getProjectOverdueIssuesCount(projectId));
		healthReport.setProjectCompletionPercentage(getProjectCompletionPercentage(projectId));
		
		int bugsLogged=getLoggedBugsCount(projectId);
		int bugsClosed=getClosedBugsCount(projectId);
		healthReport.setTotaLoggedBugsCount(bugsLogged);
		healthReport.setTotalClosedBugsCount(bugsClosed);
		
		int percentageClosedBugs= bugsLogged==0?0:((bugsClosed*100)/bugsLogged);
		healthReport.setPercentageClosedBugs(percentageClosedBugs);
		
		healthReport.setBlockedIssues(getBlockedIssuesCount(projectId));
		return healthReport;
	}
	
	private int getProjectOverdueIssuesCount(Integer projectId) {
		List<Integer> overdueSprintIds=sprintRepo.getPastSprintIds(projectId);
		return issueRepo.countOverdueIssuesBySprint(overdueSprintIds);
		
	}
	
	private int getProjectCompletionPercentage(Integer projectId) {
		List<Status> closedStatus=List.of(Status.CLOSED); 
		Integer closedStories=issueRepo.countIssueByStatusAndIssueType(projectId,null, IssueType.STORY, closedStatus);
		Integer totalStories=issueRepo.countIssueByStatusAndIssueType(projectId,null, IssueType.STORY, null);
		
		System.out.println("closed: "+ closedStories);
		System.out.println("total: "+ totalStories);
		if(totalStories==0)
			return 0;
		
		int percentage=(closedStories*100)/totalStories;
		System.out.println(percentage);
		return percentage;
	}
	
	private int getLoggedBugsCount(Integer projectId) {
		return issueRepo.countIssueByStatusAndIssueType(projectId, null, IssueType.BUG, null);
	}
	
	private int getClosedBugsCount(Integer projectId) {
		List<Status> closedStatus=List.of(Status.CLOSED); 
		return issueRepo.countIssueByStatusAndIssueType(projectId, null, IssueType.BUG, closedStatus);
	}
	
	private int getBlockedIssuesCount(Integer projectId) {
		List<Status> blockedStatus=List.of(Status.BLOCKED);
		return issueRepo.countIssueByStatusAndIssueType(projectId, null, IssueType.STORY, blockedStatus);
	}
	
	
	@Override
	public void sendProjectReport(Integer projectId, String username) {
		User requestUser=userService.findByUsername(username);
		Project project=projectService.findResourceById(projectId);
		if(requestUser.getRole()!=Role.ADMIN && project.getProjectLead()!=requestUser && project.getTeam().getManager()!= requestUser) {
			throw new CustomProjectException("You are not authorised for this action", HttpStatus.UNAUTHORIZED);
		}
		if(requestUser.getEmail()!=null) {
			ProjectHealthReport report= getProjectHealthReport(projectId);
			String subject="Project report for Project: "+project.getName();
			emailService.sendEmail(requestUser.getEmail(), subject, report.toString());
		}
		
	}


}
