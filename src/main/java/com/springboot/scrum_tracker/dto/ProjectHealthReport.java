package com.springboot.scrum_tracker.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ProjectHealthReport {

	private String projectName;
	
	private String team;
	
	private String projectLead;
	
	private int totalSprints;
	
	private String ongoingSprint;
	
	private LocalDate startDate;
	
	private int overdueIssuesCount;
	
	private int projectCompletionPercentage;
	
	private int totaLoggedBugsCount;
	
	private int totalClosedBugsCount;
	
	private int percentageClosedBugs;
	
	private int blockedIssues;

	@Override
	public String toString() {
		return "ProjectHealthReport [projectName=" + projectName + ", \nteam=" + team + ", \nprojectLead=" + projectLead
				+ ", \ntotalSprints=" + totalSprints + ", \nongoingSprint=" + ongoingSprint + ", \nstartDate=" + startDate
				+ ", \noverdueIssuesCount=" + overdueIssuesCount + ", \nprojectCompletionPercentage="
				+ projectCompletionPercentage + ", \ntotaLoggedBugsCount=" + totaLoggedBugsCount
				+ ", \ntotalClosedBugsCount=" + totalClosedBugsCount + ",\n percentageClosedBugs=" + percentageClosedBugs
				+ ", \nblockedIssues=" + blockedIssues + "]";
	}
	
	
}
