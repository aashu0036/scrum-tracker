package com.springboot.scrum_tracker.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
public class Project extends BaseEntity{

	@Column(name="name", unique = true)
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="start_date")
	private LocalDate startDate;
	
	@Column(name="sprint_count")
	@Min(value = 0)
	@Max(value = 52)
	private Integer sprintCount;
	
	@ManyToOne
	@JoinColumn(name="team_id")
	@JsonManagedReference
	private Team team;
	
	@ManyToOne
	@JoinColumn(name="project_lead_id")
	@JsonManagedReference
	private User projectLead;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JsonBackReference
	private List<Issue> issues;
	
	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Sprint> sprints;

	@Override
	public String toString() {
		return "Project [name=" + name + ", description=" + description + ", startDate=" + startDate + ", sprintCount="
				+ sprintCount + ", team=" + team + ", projectLead=" + projectLead + "]";
	}
}
