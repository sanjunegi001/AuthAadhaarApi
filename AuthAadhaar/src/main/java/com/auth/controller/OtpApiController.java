package com.auth.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth.bean.AadhaarError;
import com.auth.bean.CustomeResponseType;
import com.auth.model.otpGeneration;
import com.auth.service.OtpSaveService;
import com.auth.service.OtpServiceImpl;
import com.auth.service.UserService;
import com.auth.util.AUAProperties;
import com.auth.util.AUAUtilities;
import com.auth.util.ParamNullPointerException;
import com.auth.util.Util;
import com.auth.util.paramValidation;
import com.ecs.asa.processor.AuthProcessor;
import com.ecs.asa.processor.AuthProcessor.RcType;
import com.ecs.asa.processor.AuthProcessor.TidType;
import com.ecs.asa.processor.OtpProcessor;
import com.ecs.asa.processor.OtpProcessor.ChannelType;
import com.ecs.asa.processor.OtpProcessor.MobileEmail;
import com.ecs.asa.processor.OtpProcessor.OtpType;
import com.ecs.asa.utils.HttpConnector;
import com.ecs.exceptions.AsaServerException;
import com.ecs.exceptions.InvalidResponseException;
import com.ecs.exceptions.UidaiSignatureVerificationFailedException;
import com.ecs.exceptions.XMLParsingException;
import com.google.gson.JsonParser;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import in.gov.uidai.authentication.otp_response._1.OtpRes;
import in.gov.uidai.authentication.otp_response._1.OtpResult;
import in.gov.uidai.authentication.uid_auth_response._1.AuthRes;
import in.gov.uidai.authentication.uid_auth_response._1.AuthResult;

/**
* Controller for OtpApi
* @author sanjay.negi
*
*/
@RestController
@PropertySource("classpath:parameter.properties")
public class OtpApiController {

	
	@Autowired
	UserService userservice;

	@Autowired
	private Environment env;

	@Autowired
	private AadhaarError aadhaarerr;

	@Autowired
	private AUAProperties auaprop;
	
	@Autowired
	private OtpServiceImpl otpservice;
	
	@Autowired
	private OtpSaveService otpsaveservice;
	
	public static final Logger Log = LoggerFactory.getLogger(DemoGraphicApiController.class);
	public static String auth_data = "";
	public static String aadharcardnumber = "";
	public static String verifyby = "";
	public static String request_time = "";
	public static String response_time = "";
	public static String utransactionId = "";
	public static String requestXML = "";
	public static String responseXML = "";
	public static String flocation="";
	public static String orgip="";
	public static String fcity="";
	public static String fpostalcode="";
	public static String udc="";
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	
	/**
	 * Get Request for OtpApi
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/otpApi", method = RequestMethod.GET)
	public ResponseEntity<?> otpApiHomeGet(HttpServletRequest request) throws Exception {

		Log.info("User Access Api with Get Method");
		return new ResponseEntity<CustomeResponseType>(new CustomeResponseType.Builder("A100", "Invalid Request Mathod").build(),
				HttpStatus.BAD_REQUEST);

	}
	
	
	/**
	 * @Post Request for bioApi
	 * @param jsondata
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/otpApi", method = RequestMethod.POST)
	public ResponseEntity<?> otpApiHome(@RequestBody String jsondata, HttpServletRequest request) throws Exception {
		
		Log.info("User Access Api With Post Method");


		/**
		 * Get Request Header Values
		 */
		Map<String, String> mapheader = new HashMap<String, String>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			mapheader.put(key, value);

		}

		/** Set Request Time **/
		Date reqdate = new Date();
		request_time = dateFormat.format(reqdate);
		
		/**
		 * Captured Ipaddress Of Request
		 */
		 orgip = AUAUtilities.getClientIpAddr(request);
		 String geofile = auaprop.getGeofile();
		 LookupService lookUp = new LookupService(auaprop.getGeofile(), LookupService.GEOIP_MEMORY_CACHE);
		 
			try {
				lookUp = new LookupService(auaprop.getGeofile(), LookupService.GEOIP_MEMORY_CACHE);

				Location location = lookUp.getLocation(orgip);

				if (location != null) {
					flocation = location.countryName;
					fpostalcode = location.postalCode;
					fcity = location.city;
				} else {
					flocation = "India";
					fpostalcode = "122015";
					fcity = "Gurgaon";
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Log.info("Error Message::" + e1);
				e1.printStackTrace();

			}
			
			
		/**
		 * Check User Valid or Not	
		 */
		if (paramValidation.isParamNullorEmpty(mapheader.get("username")) || paramValidation.isParamNullorEmpty(mapheader.get("password"))) {
			Log.info("Empty Username or Password");
			return new ResponseEntity<CustomeResponseType>(
				    new CustomeResponseType.Builder("A101", "Please Check Header Values.").build(),
					HttpStatus.BAD_REQUEST);

		} else if (false == userservice.findByUserNameAndpassword(mapheader.get("username"),
				mapheader.get("password"))) {

			Log.info("Invalid Username or Password");
			return new ResponseEntity<CustomeResponseType>(
					new CustomeResponseType.Builder("A102", "Invalid Username or Password.").build(), 
					HttpStatus.NOT_FOUND);

		} else {

			auth_data = jsondata.trim();
			verifyby = mapheader.get("username").trim();
		}

		/** Generating Unique transactionid **/
		utransactionId = "AUTHBRIDGE-" + AUAUtilities.generateUniqueId();
		
		try {
			
			JsonParser parser = new JsonParser();
			parser.parse(auth_data);

			JSONObject jsonObj = new JSONObject(auth_data);
			
			if(paramValidation.isParamNullorEmpty(jsonObj.getString("authtype"))||
					paramValidation.isParamNullorEmpty(jsonObj.getString("uniqueid"))||
					paramValidation.isParamNullorEmpty(jsonObj.getString("channel"))) {
				     throw new ParamNullPointerException();
				
				
			}else {
				
				if (jsonObj.getString("authtype").trim().contentEquals("1")) {

					/**
					 * OTP
					 * GENERATION
					 **/
					
					String channel;
					aadharcardnumber = new paramValidation().isAaadharValid(jsonObj.getString("aadhaarnumber").toString().trim())
					           ?jsonObj.getString("aadhaarnumber").toString().trim():null;
			     if(aadharcardnumber==null)
			     	return new ResponseEntity<CustomeResponseType>(
						new CustomeResponseType.Builder("A110", "Aadhaar Number Should be 12 Digits.").build(),
						HttpStatus.BAD_REQUEST);
					
					
			     channel=(jsonObj.getString("channel").contentEquals("1")) || (jsonObj.getString("channel").contentEquals("2") || (jsonObj.getString("channel").contentEquals("3")))
			     ?jsonObj.getString("channel").trim():null;
				
			     if(channel==null)
			    	 return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A111", "Invalid Channel Value.").build(),
								HttpStatus.BAD_REQUEST);
			     
			     
			     
			     String uniqueid = "";
					uniqueid = (jsonObj.getString("uniqueid").trim().replaceAll("\\s", "").length()>9&&jsonObj.getString("uniqueid").trim().replaceAll("\\s", "").length()>=31
							?jsonObj.getString("uniqueid").trim().replaceAll("\\s", ""):null);
					if(uniqueid==null)
						 return new ResponseEntity<CustomeResponseType>(
									new CustomeResponseType.Builder("A112", "Unique id is not valid.").build(),
									HttpStatus.BAD_REQUEST);
			     
					if(!otpservice.findOneByUniqueid(uniqueid, verifyby))
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A111", "Duplicate unique id.").build(),
								HttpStatus.BAD_REQUEST);
					
					/**
					 * Prepare Request Xml
					 */
					utransactionId = uniqueid.trim();
					OtpProcessor opro = new OtpProcessor(new Util().readAll(auaprop.getUidai_encrypt_cert()));

					opro.setUid(aadharcardnumber);
					opro.setTxn(utransactionId);
					opro.setAc(auaprop.getUidai_aua_code());
					opro.setSa(auaprop.getUidai_aua_code());
					opro.setLk(auaprop.getUidai_license_key());
					opro.setTid(com.ecs.asa.processor.OtpProcessor.TidType.PUBLIC);
					opro.setType(OtpType.AADHAAR_NUMBER);
					if (channel.contentEquals("1"))
						opro.setCh(ChannelType.SMS_ONLY);
					else if (channel.contentEquals("2"))
						opro.setCh(ChannelType.EMAIL_ONLY);
					else
						opro.setCh(ChannelType.SMS_AND_EMAIL);
					
					
					
					requestXML = opro.getSignedXml(new Util().readAll(auaprop.getClient_pfx()), auaprop.getClient_password());

					responseXML = HttpConnector.postData(auaprop.getAsa_request_url(), requestXML);
					
					OtpRes ores = opro.parse(requestXML);
					
					if (ores.getRet() == OtpResult.Y) {
						
						/** Set Response Time **/
						Date resdate = new Date();
						response_time = dateFormat.format(resdate);
						
						MobileEmail me = opro.getMaskedMobileEmail(ores);
						if (StringUtils.isEmpty(me.getEmail()) && StringUtils.isNotEmpty(me.getMobileNumber())) {
							otpsaveservice.saveOtpGen(ores, String.format("OTP sent to Mobile Number %s", me.getMobileNumber()), aadharcardnumber, utransactionId, request_time, response_time, "", verifyby);
							return new ResponseEntity<CustomeResponseType>(
									new CustomeResponseType.Builder("A200", String.format("OTP sent to Mobile Number %s", me.getMobileNumber()))
									.setUID(aadharcardnumber)
									.setTXN(utransactionId)
									.build(),
									HttpStatus.OK);
							
						}else if (StringUtils.isNotEmpty(me.getEmail()) && StringUtils.isEmpty(me.getMobileNumber())) {
							otpsaveservice.saveOtpGen(ores, String.format("OTP sent to Email Id %s", me.getEmail()), aadharcardnumber, utransactionId, request_time, response_time, "", verifyby);
							return new ResponseEntity<CustomeResponseType>(
									new CustomeResponseType.Builder("A200", String.format("OTP sent to Eamil Id %s", me.getEmail()))
									.setUID(aadharcardnumber)
									.setTXN(utransactionId)
									.build(),
									HttpStatus.OK);
						}else {
							
							otpsaveservice.saveOtpGen(ores, String.format("OTP sent to Mobile Number %s Email Id %s", me.getMobileNumber(), me.getEmail()), aadharcardnumber, utransactionId, request_time, response_time, "", verifyby);
							return new ResponseEntity<CustomeResponseType>(
									new CustomeResponseType.Builder("A200", String.format("OTP sent to Eamil Id %s And Mobile Number %s", me.getEmail(),me.getMobileNumber()))
									.setUID(aadharcardnumber)
									.setTXN(utransactionId)
									.build(),
									HttpStatus.OK);
							
						}
						
					}else if(ores.getErr().contentEquals("952")) {
						otpsaveservice.saveOtpGen(ores, "OTP Flooding - Please avoid trying to generate the OTP multiple times within short time", aadharcardnumber, utransactionId, request_time, response_time, "", verifyby);
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A201", "OTP Flooding. Please avoid trying to generate the OTP multiple times within short time.")
								.setUID(aadharcardnumber)
								.setTXN(utransactionId)
								.setERROR("952")
								.build(),
								HttpStatus.OK);
						
					}else {
						otpsaveservice.saveOtpGen(ores, ores.getErr(), aadharcardnumber, utransactionId, request_time, response_time, "", verifyby);
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A201",aadhaarerr.getErrorMsg(ores.getErr()))
								.setUID(aadharcardnumber)
								.setTXN(utransactionId)
								.setERROR(ores.getErr())
								.build(),
								HttpStatus.OK);
					}
						
				}else if(jsonObj.getString("authtype").trim().contentEquals("2")) {
						
					String uniqueid = "",otp="";
					
					otp=paramValidation.isParamNullorEmpty(jsonObj.getString("otp"))?jsonObj.getString("otp").trim():null;
					
					if(otp==null)
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A113", "Otp Should not be empty or null.").build(),
								HttpStatus.BAD_REQUEST);
						
					uniqueid = (jsonObj.getString("uniqueid").trim().replaceAll("\\s", "").length()>9&&jsonObj.getString("uniqueid").trim().replaceAll("\\s", "").length()>=31
							?jsonObj.getString("uniqueid").trim().replaceAll("\\s", ""):null);
					if(uniqueid==null)
						 return new ResponseEntity<CustomeResponseType>(
									new CustomeResponseType.Builder("A112", "Unique id is not valid.").build(),
									HttpStatus.BAD_REQUEST);
					otpGeneration otpgen=otpservice.findOneByDuplicateId(uniqueid, verifyby, "0", "1", auaprop.getEnv_type());
					if(otpgen==null)
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A111", "Duplicate unique id.").build(),
								HttpStatus.BAD_REQUEST);
					
					AuthProcessor pro = new AuthProcessor(new Util().readAll(auaprop.getUidai_encrypt_cert()), new Util().readAll(auaprop.getUidai_signing_cert()));
					pro.setUid(Long.toString(otpgen.getUID()));
					pro.setAc(auaprop.getUidai_aua_code());
					pro.setSa(auaprop.getUidai_aua_code());
					pro.setRc(RcType.Y);
					pro.setTid(TidType.None);
					pro.setLk(auaprop.getUidai_license_key());
					pro.setTxn(otpgen.getUNIQUE_ID());
					pro.prepareOtpPIDBlock(otp, "AUT122333");
					
					requestXML = pro.getSignedXml(new Util().readAll(auaprop.getClient_pfx()), auaprop.getClient_password());
					
					responseXML = HttpConnector.postData(auaprop.getAsa_request_url(), requestXML);
					
					AuthRes res = pro.parse(responseXML);
					if (res.getRet() == AuthResult.Y) {
						
						/** Set Response Time **/
						Date resdate = new Date();
						response_time = dateFormat.format(resdate);
						otpsaveservice.saveOtpVer(res, Long.toString(otpgen.getUID()), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A200", "Authentication Success.")
								.setUID(aadharcardnumber)
								.setTXN(utransactionId)
								.build(),
								HttpStatus.OK);
						
					}else {
						
						/** Set Response Time **/
						Date resdate = new Date();
						response_time = dateFormat.format(resdate);
						otpsaveservice.saveOtpVer(res, Long.toString(otpgen.getUID()), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A201", "Authentication Success.")
								.setUID(aadharcardnumber)
								.setTXN(utransactionId)
								.setERROR(res.getErr())
								.build(),
								HttpStatus.OK);
					}
					
				}else {
					return new ResponseEntity<CustomeResponseType>(
							new CustomeResponseType.Builder("A119", "Invalid authtype.").build(),
							HttpStatus.BAD_REQUEST);
					
				}
				
			}
			
		}catch (XMLParsingException ex)  {
			
			/** Set Response Time **/
			Date resdate = new Date();
			response_time = dateFormat.format(resdate);
			org.jsoup.nodes.Document doc = Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\">" + responseXML, "", Parser.xmlParser());
			otpsaveservice.saveExceptionOtp(doc.select("Code").text(), ex.getMessage(), aadharcardnumber, request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
			return new ResponseEntity<CustomeResponseType>(
					new CustomeResponseType.Builder("A212", ex.getMessage())
					.setUID(aadharcardnumber)
					.setTXN(utransactionId)
					.setERROR(doc.select("Code").text())
					.build(),
					HttpStatus.BAD_REQUEST);
			
		}catch (AsaServerException ex) {
			
			/** Set Response Time **/
			Date resdate = new Date();
			response_time = dateFormat.format(resdate);
			otpsaveservice.saveExceptionOtp(ex.getCode(), ex.getMessage(), aadharcardnumber, request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
			return new ResponseEntity<CustomeResponseType>(
					new CustomeResponseType.Builder("A213", ex.getMessage())
					.setUID(aadharcardnumber)
					.setTXN(utransactionId)
					.setERROR(ex.getCode())
					.build(),
					HttpStatus.BAD_REQUEST);
			
		}catch (InvalidResponseException ex) {
			
			/** Set Response Time **/
			Date resdate = new Date();
			response_time = dateFormat.format(resdate);
			org.jsoup.nodes.Document doc = Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\">" + responseXML, "", Parser.xmlParser());
			otpsaveservice.saveExceptionOtp(doc.select("Code").text(), ex.getMessage(), aadharcardnumber, request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
			return new ResponseEntity<CustomeResponseType>(
					new CustomeResponseType.Builder("A214", ex.getMessage())
					.setUID(aadharcardnumber)
					.setTXN(utransactionId)
					.setERROR(doc.select("Code").text())
					.build(),
					HttpStatus.BAD_REQUEST);
		}catch (UidaiSignatureVerificationFailedException ex) {
			
			/** Set Response Time **/
			Date resdate = new Date();
			response_time = dateFormat.format(resdate);
			org.jsoup.nodes.Document doc = Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\">" + responseXML, "", Parser.xmlParser());
			otpsaveservice.saveExceptionOtp(doc.select("Code").text(), ex.getMessage(), aadharcardnumber, request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
			return new ResponseEntity<CustomeResponseType>(
					new CustomeResponseType.Builder("A215", ex.getMessage())
					.setUID(aadharcardnumber)
					.setTXN(utransactionId)
					.setERROR(doc.select("Code").text())
					.build(),
					HttpStatus.BAD_REQUEST);
			
		}
		
		
		
		
	}
}
