package com.springboot.scrum_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.scrum_tracker.model.Issue;
import com.springboot.scrum_tracker.model.IssueType;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Status;
import com.springboot.scrum_tracker.model.User;

import jakarta.transaction.Transactional;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Integer> {
	List<Issue> findByAssignee(User assignee);
	
	List<Issue> findByProject(Project project);
	
	@Transactional
	@Query("SELECT COUNT(i) FROM Issue i WHERE i.sprint.id IN :sprintIds AND i.status != 'CLOSED' ")
	Integer countOverdueIssuesBySprint(@Param("sprintIds") List<Integer> sprintIds);
	
//	@Query("SELECT COUNT(i) FROM Issue i WHERE i.project.id = :projectId AND i.issueType = :issueType AND (:statuses IS NULL OR i.status IN :statuses)")
//	Integer countIssueByStatusAndIssueType(@Param("projectId") Integer projectId, @Param("issueType") IssueType issueType, @Param("statuses") List<Status> statuses);
	
	@Transactional
	@Query("SELECT COUNT(i) FROM Issue i WHERE "
			+ "(:projectId IS NULL OR i.project.id = :projectId)"
			+ "AND (:sprintId IS NULL OR i.sprint.id = :sprintId)"
			+ " AND (:issueType IS NULL OR i.issueType = :issueType)"
			+ " AND (:statuses IS NULL OR i.status IN :statuses)")
	Integer countIssueByStatusAndIssueType(@Param("projectId") Integer projectId, @Param("sprintId") Integer sprintId, @Param("issueType") IssueType issueType, @Param("statuses") List<Status> statuses);

	
}
