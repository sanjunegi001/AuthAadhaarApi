package com.auth.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.auth.model.Personal;
import com.auth.model.Residential;
import com.auth.model.Verification;
import com.auth.util.AUAProperties;

import in.gov.uidai.authentication.uid_auth_response._1.AuthRes;



/**
 * @service Method for Database
 * @author sanjay.negi
 *
 */
@Service
@Configurable
public class DemoService {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	@Autowired
	private VerificationService verificationservice;

	@Autowired
	private AUAProperties auaprop;

	public void saveDemoAuth(AuthRes authres, String request_time, String response_time, String flocation, String orgip, String fcity, String fpostalcode, String username, String subAuaCode, Map savemap) throws ParseException {

		String savekey = "";
		Verification veri = new Verification();
		veri.setAPI_NAME("2.0");
		veri.setAUA_CODE(auaprop.getUidai_aua_code());
		veri.setSUB_AUA_CODE(subAuaCode);
		veri.setAUTH_TYPE("DEMOAUTH");

		if (StringUtils.isNotEmpty(authres.getErr())) {
			veri.setMESSAGE("Authentication Failure");
			veri.setSTATUS(0);
		} else {
			veri.setMESSAGE("Authentication Success");
			veri.setSTATUS(1);
		}
		
		veri.setUID(Long.parseLong(savemap.get("aadhaarnumber").toString()));
		veri.setUDC_CODE("AUT123");
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
		

		if (savemap.containsKey("aadhaar_name") || savemap.containsKey("gender") || savemap.containsKey("email") || savemap.containsKey("mobileno") || savemap.containsKey("dob_type") || savemap.containsKey("dob")) {

			Personal pers = null;
			pers = new Personal();
			if (savemap.containsKey("aadhaar_name")) {
				pers.setNAME(savemap.get("aadhaar_name").toString());
			}
			if (savemap.containsKey("gender")) {
				pers.setGENDER(savemap.get("gender").toString());
			}
			if (savemap.containsKey("email")) {
				pers.setEMAIL(savemap.get("email").toString());
			}
			if (savemap.containsKey("mobileno")) {

				pers.setMOBILE_NUMBER(savemap.get("mobileno").toString());
			}
			if (savemap.containsKey("dob_type")) {
				pers.setDOB_TYPE(savemap.get("dob_type").toString());
			}
			if (savemap.containsKey("dob")) {
				if (StringUtils.isNotEmpty(savemap.get("dob").toString())) {
					if (savemap.get("dob").toString().length() != 4) {
						pers.setDOB(savemap.get("dob").toString());
					} else {
						pers.setDOB(savemap.get("dob").toString() + "00" + "00");
					}
				}
			}
			pers.setVerification(veri);
			verificationservice.save(pers);

		}
		
		if (savemap.containsKey("careof") || savemap.containsKey("building") || savemap.containsKey("landmark") || savemap.containsKey("street") || savemap.containsKey("locality") || savemap.containsKey("vtc") || savemap.containsKey("dist") || savemap.containsKey("subdist") || savemap.containsKey("poname") || savemap.containsKey("state") || savemap.containsKey("pincode") || savemap.containsKey("faddress")) {

			Residential resp = null;
			resp = new Residential();
			if (savemap.containsKey("careof")) {
				resp.setCARE_OF(((String) savemap.get("careof")).trim());
			}
			if (savemap.containsKey("building")) {
				resp.setBUILDING(((String) savemap.get("building")).trim());
			}
			if (savemap.containsKey("landmark")) {
				resp.setSTREET(((String) savemap.get("landmark")).trim());
			}
			if (savemap.containsKey("street")) {
				resp.setLANDMARK(((String) savemap.get("street")).trim());
			}
			if (savemap.containsKey("locality")) {
				resp.setLOCALITY(((String) savemap.get("locality")).trim());
			}
			if (savemap.containsKey("vtc")) {
				resp.setVILLAGE_TOWN_CITY(((String) savemap.get("vtc")).trim());
			}
			if (savemap.containsKey("dist")) {
				resp.setDISTRICT(((String) savemap.get("dist")).trim());
			}

			if (savemap.containsKey("subdist")) {
				resp.setSUBDISTRICT(((String) savemap.get("subdist")).trim());
			}
			if (savemap.containsKey("poname")) {
				resp.setPONAME(((String) savemap.get("poname")).trim());
			}
			if (savemap.containsKey("state")) {
				resp.setSTATE(((String) savemap.get("state")).trim());
			}
			if (savemap.containsKey("pincode")) {
				resp.setPINCODE(((String) savemap.get("pincode")).trim());
			}
			if (savemap.containsKey("faddress")) {
				resp.setFULLADDRESS(((String) savemap.get("faddress")).trim());
			}

			resp.setVerification(veri);
			verificationservice.save(resp);
		}


	}

	public void saveExceptionDemo(String errorCode, String errorMessage,String txn, String request_time, String response_time, String flocation, String orgip, String fcity, String fpostalcode, String username, String subAuaCode, Map map) throws ParseException {

		String savekey = "";
		Map savemap = new HashMap();
		Iterator it = map.keySet().iterator();

		
		while (it.hasNext()) {
			savekey = (String) it.next();
			savemap.put(savekey, map.get(savekey));
		}

		Verification veri = new Verification();
		veri.setAPI_NAME("2.0");
		veri.setAUA_CODE(auaprop.getUidai_aua_code());
		veri.setSUB_AUA_CODE(subAuaCode);
		veri.setAUTH_TYPE("DEMOAUTH");
		veri.setUID(Long.parseLong(savemap.get("aadhaarnumber").toString()));
		veri.setUDC_CODE("AUT123");
		veri.setERROR_CODE(errorCode);
		veri.setTRANSACTION_ID(txn);
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
		veri.setASA_NAME(auaprop.asa_name);
		

		if (savemap.containsKey("aadhaar_name") || savemap.containsKey("gender") || savemap.containsKey("email") || savemap.containsKey("mobileno") || savemap.containsKey("dob_type") || savemap.containsKey("dob")) {
			Personal pers = null;
			pers = new Personal();
			if (savemap.containsKey("aadhaar_name")) {
				pers.setNAME(savemap.get("aadhaar_name").toString());
			}
			if (savemap.containsKey("gender")) {
				pers.setGENDER(savemap.get("gender").toString());
			}
			if (savemap.containsKey("email")) {
				pers.setEMAIL(savemap.get("email").toString());
			}
			if (savemap.containsKey("mobileno")) {
				pers.setMOBILE_NUMBER(savemap.get("mobileno").toString());
			}
			if (savemap.containsKey("dob_type")) {
				pers.setDOB_TYPE(savemap.get("dob_type").toString());
			}
			if (savemap.containsKey("dob")) {
				if (StringUtils.isNotEmpty(savemap.get("dob").toString())) {
					if (savemap.get("dob").toString().length() != 4) {
						pers.setDOB(savemap.get("dob").toString());
					} else {
						pers.setDOB(savemap.get("dob").toString() + "00" + "00");
					}
				}
			}
			pers.setVerification(veri);
			verificationservice.save(pers);

		}

		if (savemap.containsKey("careof") || savemap.containsKey("building") || savemap.containsKey("landmark") || savemap.containsKey("street") || savemap.containsKey("locality") || savemap.containsKey("vtc") || savemap.containsKey("dist") || savemap.containsKey("subdist") || savemap.containsKey("poname") || savemap.containsKey("state") || savemap.containsKey("pincode") || savemap.containsKey("faddress")) {

			Residential resp = null;
			resp = new Residential();
			if (savemap.containsKey("careof")) {
				resp.setCARE_OF(((String) savemap.get("careof")).trim());
			}
			if (savemap.containsKey("building")) {
				resp.setBUILDING(((String) savemap.get("building")).trim());
			}
			if (savemap.containsKey("landmark")) {
				resp.setSTREET(((String) savemap.get("landmark")).trim());
			}
			if (savemap.containsKey("street")) {
				resp.setLANDMARK(((String) savemap.get("street")).trim());
			}
			if (savemap.containsKey("locality")) {
				resp.setLOCALITY(((String) savemap.get("locality")).trim());
			}
			if (savemap.containsKey("vtc")) {
				resp.setVILLAGE_TOWN_CITY(((String) savemap.get("vtc")).trim());
			}
			if (savemap.containsKey("dist")) {
				resp.setDISTRICT(((String) savemap.get("dist")).trim());
			}

			if (savemap.containsKey("subdist")) {
				resp.setSUBDISTRICT(((String) savemap.get("subdist")).trim());
			}
			if (savemap.containsKey("poname")) {
				resp.setPONAME(((String) savemap.get("poname")).trim());
			}
			if (savemap.containsKey("state")) {
				resp.setSTATE(((String) savemap.get("state")).trim());
			}
			if (savemap.containsKey("pincode")) {
				resp.setPINCODE(((String) savemap.get("pincode")).trim());
			}
			if (savemap.containsKey("faddress")) {
				resp.setFULLADDRESS(((String) savemap.get("faddress")).trim());
			}
			resp.setVerification(veri);
			verificationservice.save(resp);
		}

	}

}
