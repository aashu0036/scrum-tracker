package com.springboot.scrum_tracker.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.scrum_tracker.config.MyUserPrincipal;
import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=userRepo.findByUserName(username);
		
//		if(user!=null) {
//			System.out.println("USER FOUNDDD!!!!!!" + user.getUserName() + user.getPassword() + user.getRole());
//			return new MyUserPrincipal(user);
//		}
		
		if (user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRole().toString())
                    .build();
        }
		
		throw new UsernameNotFoundException("User not found with login: "+username);
	}

}
