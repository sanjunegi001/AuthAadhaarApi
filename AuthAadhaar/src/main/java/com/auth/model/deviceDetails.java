package com.auth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ab_device_details")
public class deviceDetails {

	@Id
	@SequenceGenerator(name = "seq_verification", sequenceName = "seq_verification")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_verification")
	private int ID;

	@Column
	private String SERIALNUMBER;

	@Column(name="UDC")
	private String uDC;
	
	@Column
	private String MCNAME;
	@Column
	private String CREATEDON;
	@Column
	private String STATUS;
	@Column
	private String CLIENTID;

	/**
	 * Instantiates a new deviceDetails.
	 */
	public deviceDetails() {
	}

	public deviceDetails(int ID, String SERIALNUMBER, String UDC, String MCNAME, String CREATEDON, String STATUS, String CLIENTID) {

		super();
		this.ID = ID;
		this.SERIALNUMBER = SERIALNUMBER;
		this.uDC = UDC;
		this.MCNAME = MCNAME;
		this.CREATEDON = CREATEDON;
		this.STATUS = STATUS;
		this.CLIENTID = CLIENTID;

	}

	public String getSERIALNUMBER() {
		return SERIALNUMBER;
	}

	public void setSERIALNUMBER(String sERIALNUMBER) {
		SERIALNUMBER = sERIALNUMBER;
	}

	public String getUDC() {
		return uDC;
	}

	public void setUDC(String uDC) {
		uDC = uDC;
	}

	public String getMCNAME() {
		return MCNAME;
	}

	public void setMCNAME(String mCNAME) {
		MCNAME = mCNAME;
	}

	public String getCREATEDON() {
		return CREATEDON;
	}

	public void setCREATEDON(String cREATEDON) {
		CREATEDON = cREATEDON;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getCLIENTID() {
		return CLIENTID;
	}

	public void setCLIENTID(String cLIENTID) {
		CLIENTID = cLIENTID;
	}

}
