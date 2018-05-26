package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.model.Personal;
import com.auth.model.Verification;
/**
 * Repository for Personal table
 * @author sanjay.negi
 *
 */
@Repository
public interface PersonalRepository extends JpaRepository<Personal, Long>{

	public Personal save(Personal personal);
}
