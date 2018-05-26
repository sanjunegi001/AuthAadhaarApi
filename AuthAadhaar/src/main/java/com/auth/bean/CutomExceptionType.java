package com.auth.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CutomExceptionType {
	
	@JsonProperty("StatusCode")
    String Statuscode;
	
	@JsonProperty("Error")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String error;
	
	@JsonProperty("TransactionID")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String txn;
	
	@JsonProperty("Message")
	String msg;
	
	
	private CutomExceptionType(Builder builder) {
		super();
		this.Statuscode=builder.statuscode;
		this.error=builder.error;
		this.txn=builder.txn;
		this.msg=builder.msg;
		
	}
	
	public static class Builder{
		
		String statuscode;
		String error;
		String txn;
		String msg;
		
		
		public Builder(String statuscode,String msg){
			this.statuscode=statuscode;
			this.msg=msg;
			
		}
		
		public Builder excetionError(String error){
			this.error=error;
			return this;
			
		}
		
		public Builder excetionTxn(String txn) {
			this.txn=txn;
			return this;
			
		}
		
		public CutomExceptionType build() {
			
			return new CutomExceptionType(this);
			
		}
	}
	
}
