package com.auth.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;


/**
 * Component for aadhaarErrorCode
 * @author sanjay.negi
 *
 */
@Component
@PropertySource("classpath:aadhaarErrorCode.properties")
public class AadhaarError {

	
	
	
	@Autowired
    Environment env;
	
	
	
	public String getErrorMsg(String code){
		
		return env.getProperty(code);
	}


	 
	 
	
}
