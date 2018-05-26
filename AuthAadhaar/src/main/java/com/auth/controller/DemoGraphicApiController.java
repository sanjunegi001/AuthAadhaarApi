package com.auth.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import com.auth.service.DemoService;
import com.auth.service.UserService;
import com.auth.service.VerificationService;
import com.auth.util.AUAProperties;
import com.auth.util.AUAUtilities;
import com.auth.util.DateValidator;
import com.auth.util.Util;
import com.auth.util.authData;
import com.auth.util.paramValidation;
import com.ecs.asa.processor.AuthProcessor;
import com.ecs.asa.processor.AuthProcessor.RcType;
import com.ecs.asa.processor.AuthProcessor.TidType;
import com.ecs.asa.utils.HttpConnector;
import com.ecs.aua.pidgen.support.DemoAuthData;
import com.ecs.exceptions.AsaServerException;
import com.ecs.exceptions.InvalidResponseException;
import com.ecs.exceptions.UidaiSignatureVerificationFailedException;
import com.ecs.exceptions.XMLParsingException;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import in.gov.uidai.authentication.uid_auth_response._1.AuthRes;
import in.gov.uidai.authentication.uid_auth_response._1.AuthResult;


import com.auth.bean.AadhaarError;
import com.auth.bean.CustomeResponseType;

import com.auth.bean.RequestParameterType;
import com.auth.model.User;
import com.auth.bean.CutomExceptionType;



/**
 * RestController for DemoGraphic Api
 * @author sanjay.negi
 *
 */

@RestController
@PropertySource("classpath:parameter.properties")
public class DemoGraphicApiController {

	
	@Autowired
	UserService userservice;

	@Autowired
	private Environment env;

	@Autowired
	private AadhaarError aadhaarerr;

	@Autowired
	private AUAProperties auaprop;

	@Autowired
	DemoService demoserivce;

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
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	
	/**
	 * Get Request for demoGraphicApi
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/demographicApi", method = RequestMethod.GET)
	public ResponseEntity<?> demoApiHomeGet(HttpServletRequest request) throws Exception {

		Log.info("User Access Api with Get Method");
		return new ResponseEntity<CustomeResponseType>(new CustomeResponseType.Builder("A100", "Invalid Request Mathod").build(),
				HttpStatus.BAD_REQUEST);

	}

	
	/**
	 * @Post Request for demographicApi
	 * @param jsondata
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/demographicApi", method = RequestMethod.POST)
	public ResponseEntity<?> demoApiHome(@RequestBody String jsondata, HttpServletRequest request) throws Exception {

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
		if (StringUtils.isEmpty(mapheader.get("username")) || StringUtils.isEmpty(mapheader.get("password"))) {
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

		authData auth = new authData();
		DemoAuthData authdata = new DemoAuthData();
		RequestParameterType requetparam = new RequestParameterType();
		String fkey = "";
		Boolean isAadhaar;
		Map map = new HashMap();
		JSONObject resobj = new JSONObject(auth_data);
		Iterator<?> keys = resobj.keys();
		while (keys.hasNext()) {
			fkey = (String) keys.next();
			map.put(fkey, resobj.get(fkey).toString());

			if (env.getProperty(fkey) != null) {
				
				/** Checking valid aadhaar number **/
				paramValidation pval = new paramValidation();
				if (fkey.equalsIgnoreCase("aadhaarnumber")) {
					if (pval.isAaadharValid(resobj.get("aadhaarnumber").toString()))
						requetparam.setAadhaar_name(resobj.get("aadhaarnumber").toString().trim());
					else
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A110", "Aadhaar Number should be of 12 digits.").build(),
								HttpStatus.BAD_REQUEST);

				}

				if (fkey.equalsIgnoreCase("dob")) {
					if (new DateValidator().validate(resobj.get("dob").toString()))
						requetparam.setDob(resobj.get("dob").toString().trim());
					else
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A111", "dob type format is not correct.").build(),
								HttpStatus.BAD_REQUEST);

				}
				if (fkey.equalsIgnoreCase("pincode")) {

					if (new Util().isValidPin(resobj.get("pincode").toString()))
						requetparam.setPincode(resobj.get("pincode").toString().trim());
					else
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A112", "Invalid Pincode.").build(),
								HttpStatus.BAD_REQUEST);

				}

				/** Checking valid dobtype **/
				if (fkey.equalsIgnoreCase("dob_type")) {
					if (pval.isDobTypeValid(resobj.get("dob_type").toString()))
						requetparam.setDob_type(resobj.get("dob_type").toString().trim());
					else
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A113", "Invalid Dob_Type").build(),
								HttpStatus.BAD_REQUEST);
				}

				/** Checking valid gender **/
				if (fkey.equalsIgnoreCase("gender")) {
					if (pval.isgenderValid(resobj.get("gender").toString()))
						requetparam.setGender(resobj.get("gender").toString().trim());
					else
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A114", "Invalid Gender Value.").build(),
								HttpStatus.BAD_REQUEST);

				}

				/** Checking valid mobile number **/
				if (fkey.equalsIgnoreCase("mobileno")) {
					if (pval.ismobileValid(resobj.get("mobileno").toString()))
						requetparam.setMobileno(resobj.get("mobileno").toString().trim());
					else
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A115", "Invalid Mobile.").build(),
								HttpStatus.BAD_REQUEST);
				}
				/** Checking valid email **/
				if (fkey.equalsIgnoreCase("email")) {
					if (pval.isemailValid(resobj.get("email").toString()))
						requetparam.setEmail(resobj.get("email").toString().trim());
					else
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A116", "Invalid Email.").build(),
								HttpStatus.BAD_REQUEST);
				}
				authdata = auth.setValueAt(fkey.trim(), (resobj.get(fkey).toString()).trim());

			} else {
				return new ResponseEntity<CustomeResponseType>(
						new CustomeResponseType.Builder("A117", "Please check the parameter.").build(),
						HttpStatus.BAD_REQUEST);

			}

		}

		/**
		 * Prepare Request XML for ASA
		 */
		try {

			AuthProcessor pro = null;
			pro = new AuthProcessor(new Util().readAll(auaprop.getUidai_encrypt_cert()),
					new Util().readAll(auaprop.getUidai_signing_cert()));

			pro.setUid(requetparam.getAadhaar_name().toString().trim());
			pro.setAc(auaprop.getUidai_aua_code());
			pro.setSa(auaprop.getUidai_aua_code());
			pro.setRc(RcType.Y);
			pro.setTid(TidType.None);
			pro.setLk(auaprop.getUidai_license_key());
			pro.setTxn(utransactionId);

			pro.prepareDemographicPidBlock(authdata, "AUT122333");

			requestXML = pro.getSignedXml(new Util().readAll(auaprop.getClient_pfx()), auaprop.getClient_password());

			responseXML = HttpConnector.postData(auaprop.getAsa_request_url(), requestXML);

			AuthRes authres = pro.parse(responseXML);
			/** success response checked **/
			if (authres.getRet() == AuthResult.Y) {

				/** Set Response Time **/
				Date resdate = new Date();
				response_time = dateFormat.format(resdate);

				demoserivce.saveDemoAuth(authres, request_time, response_time, flocation, orgip, fcity,fpostalcode, verifyby, "", map);
				return new ResponseEntity<CustomeResponseType>(new CustomeResponseType.Builder("A200", "Authentication Success.")
						.setUID(requetparam.getAadhaar_name().toString().trim())
						.setTXN(authres.getTxn()).build(),
						HttpStatus.OK);

			} else {

				/** Set Response Time **/
				Date resdate = new Date();
				response_time = dateFormat.format(resdate);

				demoserivce.saveDemoAuth(authres, request_time, response_time, flocation, orgip, flocation, fcity, verifyby, "", map);
				return new ResponseEntity<CustomeResponseType>(
						new CustomeResponseType.Builder("A201",aadhaarerr.getErrorMsg(authres.getErr()))
						.setUID(requetparam.getAadhaar_name().toString().trim())
						.setTXN(authres.getTxn())
						.setERROR(authres.getErr()).build(),
						HttpStatus.OK);
			}

		} catch (XMLParsingException ex) {

			/** Set Response Time **/
			Date resdate = new Date();
			response_time = dateFormat.format(resdate);
			org.jsoup.nodes.Document doc = Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\">" + responseXML, "",
					Parser.xmlParser());
			demoserivce.saveExceptionDemo(doc.select("Code").text(), ex.getMessage(), utransactionId, request_time,
					response_time,  flocation, orgip, flocation, fcity, verifyby, "", map);
			return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A212", ex.getMessage())
					.excetionError(doc.select("Code").text()).excetionTxn(utransactionId).build(),
					HttpStatus.BAD_REQUEST);
		} catch (AsaServerException ex) {

			/** Set Response Time **/
			Date resdate = new Date();
			response_time = dateFormat.format(resdate);
			demoserivce.saveExceptionDemo(ex.getCode(), ex.getMessage(), utransactionId, request_time, response_time,
					 flocation, orgip, flocation, fcity, verifyby, "", map);

			return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A213", ex.getMessage())
					.excetionError(ex.getCode()).excetionTxn(utransactionId).build(), HttpStatus.BAD_REQUEST);
		} catch (InvalidResponseException ex) {
			/** Set Response Time **/
			Date resdate = new Date();
			response_time = dateFormat.format(resdate);
			org.jsoup.nodes.Document doc = Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\">" + responseXML, "",
					Parser.xmlParser());
			demoserivce.saveExceptionDemo(doc.select("Code").text(), ex.getMessage(), utransactionId, request_time,
					response_time,  flocation, orgip, flocation, fcity, verifyby, "", map);
			return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A214", ex.getMessage())
					.excetionError(doc.select("Code").text()).excetionTxn(utransactionId).build(),
					HttpStatus.BAD_REQUEST);
		} catch (UidaiSignatureVerificationFailedException ex) {

			/** Set Response Time **/
			Date resdate = new Date();
			response_time = dateFormat.format(resdate);
			org.jsoup.nodes.Document doc = Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\">" + responseXML, "",
					Parser.xmlParser());
			demoserivce.saveExceptionDemo(doc.select("Code").text(), ex.getMessage(), utransactionId, request_time,
					response_time,  flocation, orgip, flocation, fcity, verifyby, "", map);
			return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A215", ex.getMessage())
					.excetionError(doc.select("Code").text()).excetionTxn(utransactionId).build(),
					HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {

			/** Set Response Time **/
			Date resdate = new Date();
			response_time = dateFormat.format(resdate);
			if (ex.getMessage().contentEquals("Invalid uid")) {
				demoserivce.saveExceptionDemo("998", ex.getMessage(), utransactionId, request_time, response_time, 
						 flocation, orgip, flocation, fcity, verifyby, "", map);
				return new ResponseEntity<CutomExceptionType>(
						new CutomExceptionType.Builder("A219", "Invalid Aadhaar Number.").excetionError("998")
								.excetionTxn(utransactionId).build(),
						HttpStatus.BAD_REQUEST);

			} else {

				demoserivce.saveExceptionDemo("998", ex.getMessage(), utransactionId, request_time, response_time,
						 flocation, orgip, flocation, fcity, verifyby, "", map);
				return new ResponseEntity<CutomExceptionType>(
						new CutomExceptionType.Builder("A217", "Something Went Wrong.").build(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
		

	}

}
