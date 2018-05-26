package com.auth.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
import com.auth.bean.CutomExceptionType;
import com.auth.service.BioSaveService;
import com.auth.service.BioServiceImpl;
import com.auth.service.DemoService;
import com.auth.service.UserService;
import com.auth.util.AUAProperties;
import com.auth.util.AUAUtilities;
import com.auth.util.IpassCustomBase64;
import com.auth.util.Util;
import com.auth.util.paramValidation;
import com.ecs.asa.processor.AuthProcessor;
import com.ecs.asa.processor.AuthProcessor.RcType;
import com.ecs.asa.processor.AuthProcessor.TidType;
import com.ecs.asa.utils.HttpConnector;
import com.ecs.exceptions.AsaServerException;
import com.ecs.exceptions.InvalidResponseException;
import com.ecs.exceptions.UidaiSignatureVerificationFailedException;
import com.ecs.exceptions.XMLParsingException;
import com.google.gson.JsonParser;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import in.gov.uidai.authentication.uid_auth_response._1.AuthRes;
import in.gov.uidai.authentication.uid_auth_response._1.AuthResult;


/**
 * Controller for BioApi
 * @author sanjay.negi
 *
 */
@RestController
@PropertySource("classpath:parameter.properties")
public class BioApiController {
	
	
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

	@Autowired
	BioServiceImpl bioservice;
	
	@Autowired
	BioSaveService biosaveservice;
	
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
	 * Get Request for BioApi
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bioApi", method = RequestMethod.GET)
	public ResponseEntity<?> bioApiHomeGet(HttpServletRequest request) throws Exception {

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
	@RequestMapping(value = "/bioApi", method = RequestMethod.POST)
	public ResponseEntity<?> bioApiHome(@RequestBody String jsondata, HttpServletRequest request) throws Exception {
		
		
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
		
		try {
			
			JsonParser parser = new JsonParser();
			parser.parse(auth_data);
			JSONObject jsonObj = new JSONObject(auth_data);
			if (StringUtils.isNotEmpty(jsonObj.getString("aadhaarnumber").toString()) && StringUtils.isNotEmpty(jsonObj.getString("data").toString())) {
				IpassCustomBase64 piddecodestring = new IpassCustomBase64();

				String pidwebapidata = piddecodestring.decode(jsonObj.getString("data").toString().trim());
				org.jsoup.nodes.Document doc2 = null;
				try {

					doc2 = Jsoup.parse(pidwebapidata, "", Parser.xmlParser());
					if (StringUtils.isEmpty(doc2.getElementsByTag("DeviceInfo").attr("dc").trim())) {
						
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A115", "Invalid Pid XML.").build(),
								HttpStatus.BAD_REQUEST);
		
					}

				} catch (Exception e) {

					return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A115", "Invalid Pid XML.")
							.excetionTxn(utransactionId).build(), HttpStatus.BAD_REQUEST);
				}

				udc = doc2.getElementsByTag("DeviceInfo").attr("dc").trim();
				
				if (StringUtils.isNotEmpty(udc)) {
					if(!bioservice.findOneByUDC(udc)) {
						aadharcardnumber = new paramValidation().isAaadharValid(jsonObj.getString("aadhaarnumber").toString().trim())
								           ?jsonObj.getString("aadhaarnumber").toString().trim():null;
						if(aadharcardnumber==null)
							return new ResponseEntity<CustomeResponseType>(
									new CustomeResponseType.Builder("A110", "Aadhaar Number Should be 12 Digits.").build(),
									HttpStatus.BAD_REQUEST);
								
						
							
						/**
						 * Prepare Request Xml For ASA	
						 */
							String pidXML = pidwebapidata;
							AuthProcessor pro = null;
							pro = new AuthProcessor(new Util().readAll(auaprop.getUidai_encrypt_cert()),
									new Util().readAll(auaprop.getUidai_signing_cert()));
							
							pro.setUid(aadharcardnumber.trim());
							pro.setAc(auaprop.getUidai_aua_code());
							pro.setSa(auaprop.getUidai_aua_code());
							pro.setRc(RcType.Y);
							pro.setTid(TidType.registered);
							pro.setLk(auaprop.getUidai_license_key());
							pro.setTxn(utransactionId);
							pro.setRDRespone(pidXML, "FMR", false, false, false, false, false, "UDC0001");
							try {
								requestXML = pro.getSignedXml(new Util().readAll(auaprop.getClient_pfx()), auaprop.getClient_password());
								
								responseXML = HttpConnector.postData(auaprop.getAsa_request_url(), requestXML);
								
								try {
									
									AuthRes authres = pro.parse(responseXML);
									if (authres.getRet() == AuthResult.Y) {
										
										/** Set Response Time **/
										Date resdate = new Date();
										response_time = dateFormat.format(resdate);

										biosaveservice.saveBio(authres, udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
										return new ResponseEntity<CustomeResponseType>(new CustomeResponseType.Builder("A200", "Authentication Success.")
												.setUID(aadharcardnumber)
												.setTXN(authres.getTxn()).build(),
												HttpStatus.OK);
									}else {
										/** Set Response Time **/
										Date resdate = new Date();
										response_time = dateFormat.format(resdate);

										biosaveservice.saveBio(authres, udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
										return new ResponseEntity<CustomeResponseType>(
												new CustomeResponseType.Builder("A201",aadhaarerr.getErrorMsg(authres.getErr()))
												.setUID(aadharcardnumber)
												.setTXN(authres.getTxn())
												.setERROR(authres.getErr()).build(),
												HttpStatus.OK);
									}
									
								}catch (XMLParsingException ex) {

									/** Set Response Time **/
									Date resdate = new Date();
									response_time = dateFormat.format(resdate);
									org.jsoup.nodes.Document doc = Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\">" + responseXML, "",
											Parser.xmlParser());
									biosaveservice.saveExceptionDemo(doc.select("Code").text(), ex.getMessage(), udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
									return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A212", ex.getMessage())
											.excetionError(doc.select("Code").text()).excetionTxn(utransactionId).build(),
											HttpStatus.BAD_REQUEST);
								} catch (AsaServerException ex) {

									/** Set Response Time **/
									Date resdate = new Date();
									response_time = dateFormat.format(resdate);
									biosaveservice.saveExceptionDemo(ex.getCode(), ex.getMessage(), udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);

									return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A213", ex.getMessage())
											.excetionError(ex.getCode()).excetionTxn(utransactionId).build(), HttpStatus.BAD_REQUEST);
								} catch (InvalidResponseException ex) {
									/** Set Response Time **/
									Date resdate = new Date();
									response_time = dateFormat.format(resdate);
									org.jsoup.nodes.Document doc = Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\">" + responseXML, "",
											Parser.xmlParser());
									biosaveservice.saveExceptionDemo(doc.select("Code").text(), ex.getMessage(), udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
									return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A214", ex.getMessage())
											.excetionError(doc.select("Code").text()).excetionTxn(utransactionId).build(),
											HttpStatus.BAD_REQUEST);
								} catch (UidaiSignatureVerificationFailedException ex) {

									/** Set Response Time **/
									Date resdate = new Date();
									response_time = dateFormat.format(resdate);
									org.jsoup.nodes.Document doc = Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\">" + responseXML, "",
											Parser.xmlParser());
									biosaveservice.saveExceptionDemo(doc.select("Code").text(), ex.getMessage(), udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
									return new ResponseEntity<CutomExceptionType>(new CutomExceptionType.Builder("A215", ex.getMessage())
											.excetionError(doc.select("Code").text()).excetionTxn(utransactionId).build(),
											HttpStatus.BAD_REQUEST);
								} catch (Exception ex) {

									/** Set Response Time **/
									Date resdate = new Date();
									response_time = dateFormat.format(resdate);
									if (ex.getMessage().contentEquals("Invalid uid")) {
										biosaveservice.saveExceptionDemo("A219", ex.getMessage(), udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
										return new ResponseEntity<CutomExceptionType>(
												new CutomExceptionType.Builder("A219", "Invalid Aadhaar Number.").excetionError("998")
														.excetionTxn(utransactionId).build(),
												HttpStatus.BAD_REQUEST);

									} else {

										biosaveservice.saveExceptionDemo("998", ex.getMessage(), udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
										return new ResponseEntity<CutomExceptionType>(
												new CutomExceptionType.Builder("A217", "Something Went Wrong.").build(),
												HttpStatus.INTERNAL_SERVER_ERROR);
									}
								
							}
							
							
						}catch(Exception ex) {
							/** Set Response Time **/
							Date resdate = new Date();
							response_time = dateFormat.format(resdate);
							if (ex.getMessage().contentEquals("Invalid uid")) {
								biosaveservice.saveExceptionDemo("A219", ex.getMessage(), udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
								return new ResponseEntity<CutomExceptionType>(
										new CutomExceptionType.Builder("A219", "Invalid Aadhaar Number.").excetionError("998")
												.excetionTxn(utransactionId).build(),
										HttpStatus.BAD_REQUEST);

							} else {

								biosaveservice.saveExceptionDemo("998", ex.getMessage(), udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
								return new ResponseEntity<CutomExceptionType>(
										new CutomExceptionType.Builder("A217", "Something Went Wrong.").build(),
										HttpStatus.INTERNAL_SERVER_ERROR);
							}
							
						}
							
						
					}else {
						
						return new ResponseEntity<CustomeResponseType>(
								new CustomeResponseType.Builder("A112", "Device is not Whitelisted").build(),
								HttpStatus.BAD_REQUEST);
					}
				}else {
					
					return new ResponseEntity<CustomeResponseType>(
							new CustomeResponseType.Builder("A112", "Device is not Whitelisted").build(),
							HttpStatus.BAD_REQUEST);
				}

			}else {
				
				return new ResponseEntity<CustomeResponseType>(
						new CustomeResponseType.Builder("A110", "Please check request parameters.").build(),
						HttpStatus.BAD_REQUEST);
			}
			
		}catch(Exception ex) {
			
			/** Set Response Time **/
			Date resdate = new Date();
			response_time = dateFormat.format(resdate);
			if (ex.getMessage().contentEquals("Invalid uid")) {
				biosaveservice.saveExceptionDemo("998", ex.getMessage(), udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
				return new ResponseEntity<CutomExceptionType>(
						new CutomExceptionType.Builder("998", "Invalid Aadhaar Number.").excetionError("998")
								.excetionTxn(utransactionId).build(),
						HttpStatus.BAD_REQUEST);

			} else {

				biosaveservice.saveExceptionDemo("A217", ex.getMessage(), udc, aadharcardnumber.trim(), request_time, response_time, flocation, orgip, fcity, fpostalcode, "", verifyby);
				return new ResponseEntity<CutomExceptionType>(
						new CutomExceptionType.Builder("A217", "Something Went Wrong.").build(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		
		
		
		
		
		
	}

}
