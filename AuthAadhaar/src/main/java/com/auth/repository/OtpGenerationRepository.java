package com.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auth.model.otpGeneration;

@Repository
public interface OtpGenerationRepository extends JpaRepository<otpGeneration, Long>{

	public otpGeneration findOneByUnqiqueIdAndVeriFy(@Param("UNIQUE_ID")String unqiqueId,@Param("REQUEST_BY")String veriFy);
	
	public otpGeneration findOneByUnqiqueIdAndVeriFyAndOtpStatusAndStatusAndEnvType(@Param("UNIQUE_ID")String unqiqueId,
			@Param("REQUEST_BY")String veriFy,@Param("OTP_STATUS")String otpStatus,@Param("STATUS")String status
			,@Param("ENV_TYPE")String envType);
	
	public otpGeneration save(otpGeneration optgen);
}
