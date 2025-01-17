package com.springboot.scrum_tracker.service;

import java.util.List;

import com.springboot.scrum_tracker.dto.ChangePasswordDto;
import com.springboot.scrum_tracker.dto.LoginDto;
import com.springboot.scrum_tracker.dto.UserDto;
import com.springboot.scrum_tracker.model.Role;
import com.springboot.scrum_tracker.model.User;

public interface UserService extends BaseInterface<User> {

	User addUser(UserDto userdto);
	
	User addAdminUser(UserDto userDto);
	
	List<User> getAllUsers();
	
	User promoteToAdmin(Integer userId);
	
	String verifyUser(LoginDto loginDto);
	
	User deleteUser(Integer userId);
	
	void changePassword(ChangePasswordDto changePasswordDto, String requestUserName, String authority);
	
	User getUserById(Integer userId, String requestUserUsername);
	
	User findByUsername(String username);
}
