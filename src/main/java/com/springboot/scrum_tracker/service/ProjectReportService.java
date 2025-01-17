package com.springboot.scrum_tracker.service;

import com.springboot.scrum_tracker.dto.ProjectHealthReport;

public interface ProjectReportService {

	ProjectHealthReport getProjectHealthReport(Integer projectId);
	
	void sendProjectReport(Integer projectId, String username);
}
