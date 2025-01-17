package com.springboot.scrum_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.scrum_tracker.model.User;

import jakarta.transaction.Transactional;

@Repository //not required since JpaRepository provides it internally
public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByUserName(String userName);
	
	User findByFirstName(String firstName);
	
	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.team = null WHERE u.team.id = :teamId")
	void unassignTeamFromUsers(@Param("teamId") Integer teamId);
	
}
