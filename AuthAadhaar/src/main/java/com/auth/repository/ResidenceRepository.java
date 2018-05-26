package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.model.Residential;

/**
 * Repository for Residence details table
 * @author sanjay.negi
 *
 */
@Repository
public interface ResidenceRepository extends JpaRepository<Residential, Long>{

	public Residential save(Residential res);
}
