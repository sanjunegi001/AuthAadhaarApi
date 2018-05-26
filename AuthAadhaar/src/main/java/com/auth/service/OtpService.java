package com.auth.service;



import com.auth.model.otpGeneration;

public interface OtpService {

	public boolean findOneByUniqueid(String uniqueid,String verify);
	
	public otpGeneration findOneByDuplicateId(String Unqiqueid,String Verify,String Otpstatus,String Status
			,String Envtype);
	
	public otpGeneration save(otpGeneration otpgen);
}
