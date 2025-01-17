package com.springboot.scrum_tracker.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Team extends BaseEntity {
	
	@Column(name="name")
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference
	@JoinColumn(name="manager_id")
	private User manager;
	
	@OneToMany(mappedBy = "team",fetch = FetchType.EAGER)
	@JsonBackReference
	@JsonIgnore
	private List<User> members;
	
//	@OneToMany(mappedBy = "team")
//	@JsonBackReference
//	@JsonIgnore
//	private List<Issue> issues;
//	
	@OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonBackReference
	private List<Project> projects;

	@Override
	public String toString() {
		return "Team name=" + getName() +"id: "+ getId();
	}
	
	  
	
}
