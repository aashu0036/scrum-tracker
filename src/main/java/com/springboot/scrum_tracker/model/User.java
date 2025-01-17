package com.springboot.scrum_tracker.model;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class User extends BaseEntity {
	
	@Column(name="first_name", length=255)
	@NotBlank
	private String firstName;
	
	
	@Column(name="last_name")
	@NotBlank
	private String lastName;

	@Column(name="username", unique = true)
	@NotBlank
	private String userName;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private Role role;
	
	@Column(name="password")
	@NotBlank
	@JsonIgnore
	private String password;
	
	@ManyToOne
	@JoinColumn(name="team_id")
	@JsonIgnore
	private Team team;	
	
	@Column(name="email")
	private String email;
	
	
	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", team=" + team + ", lastName=" + lastName + ", login=" + userName
				+ ", role=" + role+"]";
	}
	
}
