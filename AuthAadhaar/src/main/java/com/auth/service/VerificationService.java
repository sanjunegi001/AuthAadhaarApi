package com.auth.service;

import com.auth.model.Personal;
import com.auth.model.Residential;
import com.auth.model.Verification;

public interface VerificationService {

	public Personal save(Personal personal);
	
	public Residential save(Residential res);
	
	public Verification save(Verification veri);
	
}
