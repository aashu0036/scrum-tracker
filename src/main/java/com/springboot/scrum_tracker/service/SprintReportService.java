package com.springboot.scrum_tracker.service;

import com.springboot.scrum_tracker.dto.SprintHealthReport;

public interface SprintReportService {

	SprintHealthReport getSprintHealthReport(Integer sprintId);
}
