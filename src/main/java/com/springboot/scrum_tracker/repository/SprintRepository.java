package com.springboot.scrum_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.scrum_tracker.model.Sprint;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

public interface SprintRepository extends JpaRepository<Sprint, Integer> {

	@Transactional
	@Query("SELECT s.id from Sprint s WHERE s.project.id = :projectId AND s.endDate < CURRENT_DATE")
	List<Integer> getPastSprintIds(@Param("projectId") Integer projectId);
	
	@Transactional
	@Query("SELECT s.id AS id, s.name AS name from Sprint s WHERE s.project.id = :projectId AND CURRENT_DATE BETWEEN s.startDate AND s.endDate")
	Tuple getCurrentSprintIdAndName(@Param("projectId") Integer projectId);
}
