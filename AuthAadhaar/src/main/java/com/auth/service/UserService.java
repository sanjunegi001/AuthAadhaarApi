package com.auth.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;


import com.auth.model.User;

public interface UserService extends UserDetailsService {

	public User findById( Long id);
	
	public User findByUserName(String userName);
	
	public User createUser(User user);
	
    public List<User>findAll();
    
    Boolean findOneByUserName(String username);
    
    public Boolean findByUserNameAndpassword(String username,String password);
	
}
