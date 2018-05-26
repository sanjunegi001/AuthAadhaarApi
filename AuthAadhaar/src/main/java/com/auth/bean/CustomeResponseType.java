package com.auth.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class For CustomeResponseType
 * @author sanjay.negi
 *
 */
public class CustomeResponseType {

	@JsonProperty("UID")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String uid;
	
	@JsonProperty("StatusCode")
	String statuscode;
	
	@JsonProperty("TransactionID")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String txn;
	
	@JsonProperty("Message")
	String message;
	
	@JsonProperty("Error")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String error;
	
	
	private CustomeResponseType(Builder builder) {
		super();
		this.statuscode=builder.statuscode;
		this.error=builder.error;
		this.txn=builder.txn;
		this.message=builder.message;
		this.uid=builder.uid;
		
	}
	
	
	
	
	
	public static class Builder{
		
		/** Madatory fields**/
		String statuscode;
		String message;
		
		/**
		 * Optional fields
		 */
		String uid;
		String txn;
		String error;
		
		public Builder(String statuscode,String message) {
			this.statuscode=statuscode;
			this.message=message;
		}
		
		public Builder setUID(String uid) {
			this.uid=uid;
			return this;
			
		}
		
		public Builder setTXN(String txn) {
			this.txn=txn;
			return this;
			
		}
		public Builder setERROR(String error) {
			this.error=error;
			return this;
			
		}
		
		public CustomeResponseType build() {
			return new CustomeResponseType(this);
			
		}
		
	}
	
	
	
}
