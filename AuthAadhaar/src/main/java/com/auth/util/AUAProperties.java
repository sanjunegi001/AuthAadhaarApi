package com.auth.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class AUAProperties {

	

	
	/*The GEO FILE
	 * */
	@Value("${geofile}")
	public  String geofile;
	
	/*The LOG PATH
	 * */
	@Value("${logPath}")
	public  String logPath;
	
	/*The CLIENT ID
	 * */
	@Value("${client_id}")
	public  String client_id;
	
	/*The CLIENT PFX
	 * */
	@Value("${client_pfx}")
	public  String client_pfx;
	
	/*The CLIENT PASS
	 * */
	@Value("${client_password}")
	public  String client_password;
	
	/*The UIDAI SIGNING CERT
	 * */
	@Value("${uidai_signing_cert}")
	public  String uidai_signing_cert;
	
	/*The UIDAI ENCRYPT CERT
	 * */
	@Value("${uidai_encrypt_cert}")
	public  String uidai_encrypt_cert;
	
	/*The UIDAI  AUA CODE
	 * */
	@Value("${uidai_aua_code}")
	public  String uidai_aua_code;
	
	/*The UIDAI SUB AUA CODE
	 * */
	@Value("${uidai_subaua_code}")
	public  String uidai_subaua_code;
	
	/*The UIDAI LICENSE KEY
	 * */
	@Value("${uidai_license_key}")
	public  String uidai_license_key;
	
	@Value("${uidai_bio_license_key}")
	public  String uidai_bio_license_key;
	
	/*The ASA
	 */
	@Value("${asa_name}")
	public  String asa_name;
	
	/*The ENV TYPE
	 */
	@Value("${env_type}")
	public  String env_type;
	
	/*The ASA URL
	 * */
	@Value("${asa_request_url}")
	public  String asa_request_url;

	public  String getGeofile() {
		return geofile;
	}

	public  String getLogPath() {
		return logPath;
	}

	public  String getClient_id() {
		return client_id;
	}

	public  String getClient_pfx() {
		return client_pfx;
	}

	public  String getClient_password() {
		return client_password;
	}

	public  String getUidai_signing_cert() {
		return uidai_signing_cert;
	}

	public  String getUidai_encrypt_cert() {
		return uidai_encrypt_cert;
	}

	public  String getUidai_aua_code() {
		return uidai_aua_code;
	}

	public  String getUidai_subaua_code() {
		return uidai_subaua_code;
	}

	public  String getUidai_license_key() {
		return uidai_license_key;
	}

	public  String getUidai_bio_license_key() {
		return uidai_bio_license_key;
	}

	public  String getAsa_name() {
		return asa_name;
	}

	public  String getEnv_type() {
		return env_type;
	}

	public  String getAsa_request_url() {
		return asa_request_url;
	}

	
	
	
	
}
