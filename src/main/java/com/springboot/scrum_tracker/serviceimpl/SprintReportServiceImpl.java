package com.springboot.scrum_tracker.serviceimpl;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.scrum_tracker.dto.SprintHealthReport;
import com.springboot.scrum_tracker.model.IssueType;
import com.springboot.scrum_tracker.model.Sprint;
import com.springboot.scrum_tracker.model.Status;
import com.springboot.scrum_tracker.repository.IssueRepository;
import com.springboot.scrum_tracker.service.SprintReportService;
import com.springboot.scrum_tracker.service.SprintService;

@Service
public class SprintReportServiceImpl implements SprintReportService {

	@Autowired
	private SprintService sprintService;
	
	@Autowired
	private IssueRepository issueRepo;
	
	private Sprint cachedSprint;
	
	@Override
	public SprintHealthReport getSprintHealthReport(Integer sprintId) {
		Sprint sprint=getSprintById(sprintId);
		
		SprintHealthReport sprintReport=new SprintHealthReport();
		LocalDate sprintEndDate=sprint.getEndDate();
		
		sprintReport.setName(sprint.getName());
		sprintReport.setStartDate(sprint.getStartDate());
		sprintReport.setEndDate(sprintEndDate);
		sprintReport.setCompletionPercentage(getSprintCompletionPercentage(sprintId));
		sprintReport.setTotalIssues(getTotalIssueCount(sprintId));
		sprintReport.setBlockedIssues(getBlockedIssuesCount(sprintId));
		sprintReport.setOverdueIssues(getOverdueIssues(sprintId));
		sprintReport.setBugRatio(getBugRatio(sprintId));
		
		int daysRemaining= sprintEndDate.isBefore(LocalDate.now())?0:(Period.between(LocalDate.now(), sprintEndDate)).getDays();
		sprintReport.setSprintDaysRemaining(daysRemaining);
		return sprintReport;
	}
	
	public Sprint getSprintById(Integer sprintId) {
		if (cachedSprint == null || !cachedSprint.getId().equals(sprintId)) {
            cachedSprint = sprintService.findResourceById(sprintId);
        }
		
		return cachedSprint;
	}
	
	public int getSprintCompletionPercentage(Integer sprintId) {
		List<Status> allowedStatuses=null;
		Integer projectId=null;
		int totalStoryCount = issueRepo.countIssueByStatusAndIssueType(projectId, sprintId, IssueType.STORY, allowedStatuses);
		System.out.println("total story count is: "+totalStoryCount);
		int closedStoryCount = issueRepo.countIssueByStatusAndIssueType(projectId, sprintId, IssueType.STORY, List.of(Status.CLOSED));
		
		int sprintCompletionPercentage= totalStoryCount==0?0:(closedStoryCount*100)/totalStoryCount;
		
		return sprintCompletionPercentage;
	}
	
	public int getBlockedIssuesCount(Integer sprintId) {
		Integer projectId=null;
		int blockedStories= issueRepo.countIssueByStatusAndIssueType(projectId, sprintId, IssueType.STORY, List.of(Status.BLOCKED));
		int blockedBugs= issueRepo.countIssueByStatusAndIssueType(projectId, sprintId, IssueType.BUG, List.of(Status.BLOCKED));
		
		System.out.println("total blocked: "+ issueRepo.countIssueByStatusAndIssueType(null, sprintId, null, List.of(Status.BLOCKED)));
		return blockedStories+blockedBugs;
	}
	
	public int getOverdueIssues(Integer sprintId) {
		Sprint sprint=getSprintById(sprintId);
		if(sprint.getEndDate().isAfter(LocalDate.now()))
			return 0;
		
		List<Status> nonClosedStatuses=Arrays.stream(Status.values())
                .filter(status -> status != Status.CLOSED)
                .collect(Collectors.toList());
		
		Integer projectId=null;
		return issueRepo.countIssueByStatusAndIssueType(projectId, sprintId, null, nonClosedStatuses);
	}
	
	public int getTotalIssueCount(Integer sprintId) {
		int totalIssueCount = issueRepo.countIssueByStatusAndIssueType(null, sprintId, null, null);
		System.out.println("total issue count: "+ totalIssueCount);
		return totalIssueCount;
	}
	
	public int getBugRatio(Integer sprintId) {
		int totalBugsCount=issueRepo.countIssueByStatusAndIssueType(null, sprintId, IssueType.BUG, null);
		int totalIssueCount=issueRepo.countIssueByStatusAndIssueType(null, sprintId, null, null);
		
		int bugRatio=totalIssueCount==0?0:(totalBugsCount*100)/totalIssueCount;
		
		return bugRatio;
	}

}
