package com.springboot.scrum_tracker.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.springboot.scrum_tracker.dto.ProjectRequestDto;
import com.springboot.scrum_tracker.exception.ResourceNotFoundException;
import com.springboot.scrum_tracker.model.Issue;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Sprint;
import com.springboot.scrum_tracker.repository.SprintRepository;
import com.springboot.scrum_tracker.service.SprintService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SprintServiceImpl implements SprintService {
	
	@Autowired
	private SprintRepository sprintRepo;

	@Override
	public void createSprintForProject(Project project) {
		int sprintCount=project.getSprintCount();
		LocalDate projectStartDate=project.getStartDate();
		for(int i=0;i<sprintCount;i++) {
			LocalDate sprintStartDate = projectStartDate.plusWeeks(i * 2); // 2-week intervals
            LocalDate sprintEndDate = sprintStartDate.plusDays(13); // 14-day duration
            
            Sprint sprint=new Sprint();
            sprint.setName("Sprint " + (i + 1));
            sprint.setStartDate(sprintStartDate);
            sprint.setEndDate(sprintEndDate);
            sprint.setProject(project);
            
            sprintRepo.save(sprint);
		}
		
	}

	@Override
	public List<Issue> getSprintIssues(Integer sprintId) {
		Sprint sprint=findResourceById(sprintId);
		return sprint.getIssues();
	}

	@Override
	public Sprint findResourceById(Integer sprintId) {
		return sprintRepo.findById(sprintId).orElseThrow(()-> new ResourceNotFoundException("Sprint", "id", sprintId));
	}

}
