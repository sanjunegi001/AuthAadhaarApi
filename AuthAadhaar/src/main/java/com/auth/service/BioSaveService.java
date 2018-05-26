package com.auth.service;



import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.auth.model.Verification;
import com.auth.util.AUAProperties;

import in.gov.uidai.authentication.uid_auth_response._1.AuthRes;

@Service
@Configurable
public class BioSaveService {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	@Autowired
	private AUAProperties auaprop;
	
	@Autowired
	private VerificationService veriservice;

	public void saveBio(AuthRes authres, String udc, String aadharcardnumber, String request_time, String response_time, String flocation, String orgip, String fcity, String fpostalcode, String subAuaCode, String username) throws ParseException {

	

		Verification veri = new Verification();
		veri.setAPI_NAME("2.0");
		veri.setAUA_CODE(auaprop.getUidai_aua_code());
		veri.setSUB_AUA_CODE(auaprop.getUidai_aua_code());
		veri.setAUTH_TYPE("BIOAUTH");

		if (StringUtils.isNotEmpty(authres.getErr())) {
			veri.setMESSAGE("Authentication Failure");
			veri.setSTATUS(0);
		} else {
			veri.setMESSAGE("Authentication Success");
			veri.setSTATUS(1);

		}
		veri.setUID(Long.parseLong(aadharcardnumber));
		veri.setUDC_CODE(udc);
		veri.setERROR_CODE(authres.getErr());
		veri.setTRANSACTION_ID(authres.getTxn());
		veri.setSERVER_RESPONSE_ON(authres.getTs());
		veri.setREQUEST_ON(new Timestamp(dateFormat.parse(request_time).getTime()));
		veri.setRESPONSE_ON(new Timestamp(dateFormat.parse(response_time).getTime()));
		veri.setCOUNTRY(flocation);
		veri.setIPADDRESS(orgip);
		veri.setCITY(fcity);
		veri.setPINCODE(Integer.parseInt(fpostalcode.trim()));
		veri.setREFERENCE_NUMBER(authres.getCode());
		veri.setREQUESTED_BY(username.toString());
		veri.setCONSENT(1);
		veri.setENV_TYPE(auaprop.getEnv_type());
		veri.setASA_NAME(auaprop.getAsa_name());
		veriservice.save(veri);

	}

	public void saveExceptionDemo(String errorCode, String errorMessage, String udc, String aadharcardnumber, String request_time, String response_time, String flocation, String orgip, String fcity, String fpostalcode, String subAuaCode, String username) throws ParseException {

		Verification veri = new Verification();

		veri.setAPI_NAME("2.0");
		veri.setAUA_CODE(auaprop.getUidai_aua_code());
		veri.setSUB_AUA_CODE(auaprop.getUidai_aua_code());
		veri.setAUTH_TYPE("BIOAUTH");
		veri.setUID(Long.parseLong(aadharcardnumber));
		veri.setUDC_CODE(udc);
		veri.setERROR_CODE(errorCode);
		veri.setREQUEST_ON(new Timestamp(dateFormat.parse(request_time).getTime()));
		veri.setRESPONSE_ON(new Timestamp(dateFormat.parse(response_time).getTime()));
		veri.setCOUNTRY(flocation);
		veri.setIPADDRESS(orgip);
		veri.setCITY(fcity);
		veri.setPINCODE(Integer.parseInt(fpostalcode.trim()));
		veri.setMESSAGE("Authentication Failure");
		veri.setSTATUS_DESCRIPTION(errorMessage);
		veri.setSTATUS(0);
		veri.setREQUESTED_BY(username.toString());
		veri.setCONSENT(1);
		veri.setENV_TYPE(auaprop.getEnv_type());
		veri.setASA_NAME(auaprop.getAsa_name());
		veriservice.save(veri);
	}

}

