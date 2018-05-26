package com.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth.model.otpGeneration;
import com.auth.repository.OtpGenerationRepository;

@Component
public class OtpServiceImpl implements OtpService{

	@Autowired
	OtpGenerationRepository otprepo;
	
	@Override
	public boolean findOneByUniqueid(String uniqueid,String verify) {
		// TODO Auto-generated method stub
		return otprepo.findOneByUnqiqueIdAndVeriFy(uniqueid, verify)==null?false:true;
	}

	@Override
	public otpGeneration save(otpGeneration otpgen) {
		// TODO Auto-generated method stub
		return otprepo.save(otpgen);
	}

	@Override
	public otpGeneration findOneByDuplicateId(String Unqiqueid, String Verify, String Otpstatus, String Status,
			String Envtype) {
		// TODO Auto-generated method stub
		otpGeneration otpgen=otprepo.findOneByUnqiqueIdAndVeriFyAndOtpStatusAndStatusAndEnvType(Unqiqueid, Verify, Otpstatus, Status, Envtype);
		
		return otpgen;
	}

}
