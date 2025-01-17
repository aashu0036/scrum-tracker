package com.springboot.scrum_tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Issue extends BaseEntity{

	@Column(name="title")
	private String title;
	
	@Column(name="decription")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="assignee_user_id")
	@JsonManagedReference
	@JsonIgnore
	private User assignee;
	
	@Column(name="priority")
	@Enumerated(EnumType.STRING)
	private Priority priority;
	
	@Column(name="issue_type")
	@Enumerated(EnumType.STRING)
	private IssueType issueType;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private Status status;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project_id")
	@JsonManagedReference
	@JsonIgnore
	private Project project;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sprint_id")
	@JsonManagedReference
	@JsonIgnore
	private Sprint sprint;
}
