package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.model.Verification;

public interface VerificationRepository extends JpaRepository<Verification, Long>{

	public Verification save(Verification veri);
}
