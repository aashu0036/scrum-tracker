package com.springboot.scrum_tracker.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Sprint extends BaseEntity{

	@Column(name="name")
	private String name;
	
	@Column(name="start_date")
	private LocalDate startDate;
	
	@Column(name="end_date")
	private LocalDate endDate;
	
	@ManyToOne
	@JoinColumn(name="project_id")
	@JsonBackReference
	private Project project;
	
	@OneToMany(mappedBy = "sprint")
	@JsonManagedReference
	private List<Issue> issues;

	@Override
	public String toString() {
		return "Sprint [name=" + name + ", startDate=" + startDate + ", endDate=" + endDate + ", project=" + project.getName()
				+ "]";
	}
}
