package com.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth.model.Personal;
import com.auth.model.Residential;
import com.auth.model.Verification;
import com.auth.repository.PersonalRepository;
import com.auth.repository.ResidenceRepository;
import com.auth.repository.VerificationRepository;

@Component
public class VerificationServiceimpl implements VerificationService{

	     @Autowired
	     PersonalRepository personalrepo;
	     
	     @Autowired
	     ResidenceRepository residencrepo;
	     
	     @Autowired
	     VerificationRepository verirepo;
	     
	   
	@Override
	public Personal save(Personal personal) {
		// TODO Auto-generated method stub
		return personalrepo.save(personal);
	}


	@Override
	public Residential save(Residential res) {
		// TODO Auto-generated method stub
		return residencrepo.save(res);
	}


	@Override
	public Verification save(Verification veri) {
		// TODO Auto-generated method stub
		return verirepo.save(veri);
	}

}
