package com.springboot.scrum_tracker.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.scrum_tracker.dto.ChangePasswordDto;
import com.springboot.scrum_tracker.dto.LoginDto;
import com.springboot.scrum_tracker.dto.UserDto;
import com.springboot.scrum_tracker.exception.CustomException;
import com.springboot.scrum_tracker.exception.InvalidCredentialsException;
import com.springboot.scrum_tracker.exception.ResourceNotFoundException;
import com.springboot.scrum_tracker.model.Issue;
import com.springboot.scrum_tracker.model.Project;
import com.springboot.scrum_tracker.model.Role;
import com.springboot.scrum_tracker.model.Team;
import com.springboot.scrum_tracker.model.User;
import com.springboot.scrum_tracker.repository.IssueRepository;
import com.springboot.scrum_tracker.repository.ProjectRepository;
import com.springboot.scrum_tracker.repository.TeamRepository;
import com.springboot.scrum_tracker.repository.UserRepository;
import com.springboot.scrum_tracker.service.UserService;
import com.springboot.scrum_tracker.utils.JwtUtils;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TeamRepository teamRepo;
	
	@Autowired
	private ProjectRepository projectRepo;
	
	@Autowired
	private IssueRepository issueRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public User addUser(UserDto userDto) {
		User newUser=new User();
		User existingUser=userRepo.findByUserName(userDto.getUserName());
		if(existingUser!=null) {
			String message="User with username: "+ userDto.getUserName()+" already exists";
			throw new CustomException(message, HttpStatus.BAD_REQUEST);
		}
		Team team=null;
		if(userDto.getTeamId()!=null) {
			 team = teamRepo.findById(userDto.getTeamId())
			        .orElseThrow(() -> new ResourceNotFoundException("Team", "id", userDto.getTeamId()));
		}
		
		newUser.setFirstName(userDto.getFirstName());
		newUser.setLastName(userDto.getLastName());
		newUser.setUserName(userDto.getUserName());
		newUser.setRole(Role.USER);
		newUser.setTeam(team);
		newUser.setEmail(userDto.getEmail());
		
		String hashedPassword=passwordEncoder.encode(userDto.getPassword());
//		String hashedPassword="abcd";
		newUser.setPassword(hashedPassword);
		return userRepo.save(newUser);
	}

	@Override
	public String verifyUser(LoginDto loginDto){
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));
			UserDetails userDetails= userDetailsService.loadUserByUsername(loginDto.getUserName());
			String jwt=jwtUtil.generateToken(userDetails);
			return jwt;
		}
		catch(Exception e) {
//			throw new InvalidCredentialsException(userDto.getLogin());
//			System.out.println("ERRORRRRRRRRRRR"+ e.getMessage());
			return e.getMessage();
		}
	}


	@Override
	public User deleteUser(Integer userId) {
		User user=findResourceById(userId);
		Team userManagedTeam=teamRepo.findByManagerUserName(user.getUserName());
		if(userManagedTeam!=null) {
			String message="Cannot delete user id:"+ userId+" as long as they are manager for team: "+userManagedTeam.getName();
			throw new CustomException(message, HttpStatus.BAD_REQUEST);
		}
		
		Project userLeadProject= projectRepo.findByProjectLeadUserName(user.getUserName());
		if(userLeadProject!=null) {
			String message="Cannot delete user id:"+ userId+" as long as they are lead for project: "+userLeadProject.getName();
			throw new CustomException(message, HttpStatus.BAD_REQUEST);
		}
		
		List<Issue> issues=issueRepo.findByAssignee(user);
		for(Issue issue: issues) {
			issue.setAssignee(issue.getProject().getProjectLead());
			issueRepo.save(issue);
		}
		userRepo.delete(user);
		System.out.println(user);
		return user;
	}


	@Override
	public User addAdminUser(UserDto userDto) {
		User newAdminUser=addUser(userDto);
		newAdminUser.setRole(Role.ADMIN);
		return userRepo.save(newAdminUser);
	}


	
	
	@Override
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}


	@Override
	public User promoteToAdmin(Integer userId) {
		User user=findResourceById(userId);
		user.setRole(Role.ADMIN);
		return userRepo.save(user);
		
	}


	@Override
	@Transactional
	public void changePassword(ChangePasswordDto changePasswordDto, String requestUserName, String authority) {
		if(!authority.equals("ROLE_ADMIN")) {
			if(!requestUserName.equals(changePasswordDto.getUserName())) {
				throw new CustomException("You are not authorised for this action", HttpStatus.BAD_REQUEST);
			}
		}
		User user=userRepo.findByUserName(changePasswordDto.getUserName());
		if(user==null) {
			throw new ResourceNotFoundException("User","username", changePasswordDto.getUserName());
		}
		user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
		return;
	}


	@Override
	public User findResourceById(Integer userId) {
		return userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id", userId));
	}

	@Override
	public User getUserById(Integer userId, String requestUserUsername) {
		User requestUser=userRepo.findByUserName(requestUserUsername);
		if(userId!=requestUser.getId() && requestUser.getRole()!=Role.ADMIN)
			throw new CustomException("You are not authrorised for this action", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		
		return findResourceById(userId);
	}

	@Override
	public User findByUsername(String username) {
		User user=userRepo.findByUserName(username);
		if(user==null)
			throw new ResourceNotFoundException("User", "username", username);
		
		return user;
	}
}
