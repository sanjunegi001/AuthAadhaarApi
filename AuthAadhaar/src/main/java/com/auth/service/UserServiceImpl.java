package com.auth.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth.model.User;
import com.auth.repository.UserRepository;
import com.auth.service.UserService;

@Component
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	/**
	 * To find the user by user id
	 */
	@Override
	public User findById(Long id) {
		return userRepository.findOneById(id);
	}

	/**
	 * To find the user by username. Will get the username as input
	 */
	@Override
	public User findByUserName(String userName) {
		return userRepository.findOneByUserName(userName);
	}

	/**
	 * To create a new user
	 */
	@Override
	public User createUser(User user) {
		return userRepository.save(user);
	}

	/**
	 * To load the proper user and get the user details using the username
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		
		User user = this.findByUserName(userName);
		
		
		if(user == null){
			throw new UsernameNotFoundException("No user present with username: "+userName);
		}
		System.out.println("sanjnnnn::"+user.getUsername());
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return (List<User>) userRepository.findAll();
	}

	@Override
	public Boolean findOneByUserName(String username) {
		// TODO Auto-generated method stub
		User user = this.findByUserName(username); 
		if(user==null)
			return false;
		else
			return true;
	}

	@Override
	public Boolean findByUserNameAndpassword(String username, String password) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserNameAndPassword(username, password);
		if(user==null)
			return false;
		else
			return true;
		
	}

	
	
	

}
