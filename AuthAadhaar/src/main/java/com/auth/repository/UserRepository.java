package com.auth.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.auth.model.User;


@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>{

	public User findOneById(@Param("id") Long id);
	
	public User findOneByUserName(@Param("username") String userName);
	
	public User findByUserNameAndPassword(@Param("username") String userName,@Param("password") String Password);
	
	
}
