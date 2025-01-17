package com.springboot.scrum_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.scrum_tracker.model.Team;

import jakarta.transaction.Transactional;

public interface TeamRepository extends JpaRepository<Team, Integer> {
	
	Team findByManagerUserName(String username);
	
	@Modifying
    @Transactional
    @Query("UPDATE Team t SET t.manager = null WHERE t.id = :teamId")
    void unassignManagerFromTeam(@Param("teamId") Integer teamId);
}
