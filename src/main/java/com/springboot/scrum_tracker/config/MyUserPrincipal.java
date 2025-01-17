package com.springboot.scrum_tracker.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.springboot.scrum_tracker.model.Role;
import com.springboot.scrum_tracker.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyUserPrincipal implements UserDetails {
	
	private User user;
	
	public MyUserPrincipal(User user, List<?> authorities) {
		super();
		this.user = user;
	}
	
	public Integer getId() {
		return user.getId();
	}
	
	public Role getRole() {
		return user.getRole();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		log.info("In MyUserPrincipal for username: {}", user.getUserName());
		return user.getUserName();
	}
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Map Role enum to GrantedAuthority
        return List.of(user.getRole()).stream()
                    .map(role -> new SimpleGrantedAuthority(role.toString()))
                    .collect(Collectors.toList());
    }
	
}
