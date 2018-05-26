package com.auth.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ab_otp_generation")
public class otpGeneration {

	@Id
	@SequenceGenerator(name = "seq_verification", sequenceName = "seq_verification")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_verification")

	private int ID;

	@Column(name="TRANSACTION_ID")
	private String TRANSACTION_ID;
	@Column(name="UNIQUE_ID")
	private String unqiqueId;
	@Column(name="STATUS")
	private String status;
	@Column(name="MESSAGE")
	private String MESSAGE;
	@Column(name="UID")
	private Long UID;
	@Column(name="OTP_STATUS")
	private int otpStatus;
	@Column(name="ERRORCODE")
	private String ERRORCODE;
	@Column(name="REQUEST_BY")
	private String veriFy;
	@Column(name="REQUEST_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Date REQUEST_ON;

	/** The response on. */

	@Column(name="RESPONSE_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Date RESPONSE_ON;
	@Column(name="AUA_CODE")
	private String AUA_CODE;
	@Column(name="SUB_AUA_CODE")
	private String SUB_AUA_CODE;
	@Column(name="ENV_TYPE")
	private String envType;
	@Column(name="ASA_NAME")
	private String ASA_NAME;

	/**
	 * Instantiates a new deviceDetails.
	 */
	public otpGeneration() {
	}

	public otpGeneration(int ID, String TRANSACTION_ID, String UNIQUE_ID, String MESSAGE, int UID, String ERRORCODE, int OTP_STATUS, String REQUEST_BY, Date REQUEST_ON, Date RESPONSE_ON, String AUA_CODE, String SUB_AUA_CODE, String ASA_NAME, String ENV_TYPE) {

		super();
		this.ID = ID;
		this.TRANSACTION_ID = TRANSACTION_ID;
		this.unqiqueId = UNIQUE_ID;
		this.MESSAGE = MESSAGE;
		this.ERRORCODE = ERRORCODE;
		this.otpStatus = OTP_STATUS;
		this.veriFy = REQUEST_BY;
		this.REQUEST_ON = REQUEST_ON;
		this.RESPONSE_ON = RESPONSE_ON;
		this.AUA_CODE = AUA_CODE;
		this.SUB_AUA_CODE = SUB_AUA_CODE;
		this.ASA_NAME = ASA_NAME;
		this.envType = ENV_TYPE;
	}

	public String getTRANSACTION_ID() {
		return TRANSACTION_ID;
	}

	public void setTRANSACTION_ID(String tRANSACTION_ID) {
		TRANSACTION_ID = tRANSACTION_ID;
	}

	public String getUNIQUE_ID() {
		return unqiqueId;
	}

	public void setUNIQUE_ID(String uNIQUE_ID) {
		unqiqueId = uNIQUE_ID;
	}

	public String getSTATUS() {
		return status;
	}

	public void setSTATUS(String sTATUS) {
		status = sTATUS;
	}

	public String getMESSAGE() {
		return MESSAGE;
	}

	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}

	public Long getUID() {
		return UID;
	}

	public void setUID(Long uID) {
		UID = uID;
	}

	public String getERRORCODE() {
		return ERRORCODE;
	}

	public void setERRORCODE(String eRRORCODE) {
		ERRORCODE = eRRORCODE;
	}

	public String getREQUEST_BY() {
		return veriFy;
	}

	public void setREQUEST_BY(String rEQUEST_BY) {
		veriFy = rEQUEST_BY;
	}

	public Date getREQUEST_ON() {
		return REQUEST_ON;
	}

	public void setREQUEST_ON(Date rEQUEST_ON) {
		REQUEST_ON = rEQUEST_ON;
	}

	public Date getRESPONSE_ON() {
		return RESPONSE_ON;
	}

	public void setRESPONSE_ON(Date rESPONSE_ON) {
		RESPONSE_ON = rESPONSE_ON;
	}

	public int getOTP_STATUS() {
		return otpStatus;
	}

	public void setOTP_STATUS(int oTP_STATUS) {
		otpStatus = oTP_STATUS;
	}

	public String getAUA_CODE() {
		return AUA_CODE;
	}

	public void setAUA_CODE(String aUA_CODE) {
		AUA_CODE = aUA_CODE;
	}

	public String getSUB_AUA_CODE() {
		return SUB_AUA_CODE;
	}

	public void setSUB_AUA_CODE(String sUB_AUA_CODE) {
		SUB_AUA_CODE = sUB_AUA_CODE;
	}

	public String getENV_TYPE() {
		return envType;
	}

	public void setENV_TYPE(String eNV_TYPE) {
		envType = eNV_TYPE;
	}

	public String getASA_NAME() {
		return ASA_NAME;
	}

	public void setASA_NAME(String aSA_NAME) {
		ASA_NAME = aSA_NAME;
	}

}
