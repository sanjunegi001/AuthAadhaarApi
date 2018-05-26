/*
 * 
 */
package com.auth.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

// TODO: Auto-generated Javadoc
/**
 * The Class Personal.
 */
@Entity
@Table(name = "ab_personal_details")
public class Personal {

	/** The id. */
	@Id
	@SequenceGenerator(name = "seq_verification", sequenceName = "seq_verification")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_verification")
	private int ID;

	/** The name. */
	@Column
	private String NAME;

	/** The dob. */
	@Column
	private String DOB;

	/** The dob type. */
	@Column
	private String DOB_TYPE;

	/** The gender. */
	@Column
	private String GENDER;

	/** The Emial. */
	@Column
	private String EMAIL;

	/** The mobilenumber. */
	@Column
	private String MOBILE_NUMBER;

//	/** The ab verification id. */
//	@Column
//	private int AB_VERIFICATION_ID;

	/** The Personal. */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "AB_VERIFICATION_ID", referencedColumnName = "ID")
    private Verification verification;

	
	
	public Verification getVerification() {
		return verification;
	}

	public void setVerification(Verification verification) {
		this.verification = verification;
	}

	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getNAME() {
		return NAME;
	}

	/**
	 * Sets the name.
	 *
	 * @param nAME
	 *            the new name
	 */
	public void setNAME(String nAME) {
		NAME = nAME;
	}

	/**
	 * Gets the dob.
	 *
	 * @return the dob
	 */
	public String getDOB() {
		return DOB;
	}

	/**
	 * Sets the dob.
	 *
	 * @param dOB
	 *            the new dob
	 */
	public void setDOB(String dOB) {
		DOB = dOB;
	}

	/**
	 * Gets the dob type.
	 *
	 * @return the dob type
	 */
	public String getDOB_TYPE() {
		return DOB_TYPE;
	}

	/**
	 * Sets the dob type.
	 *
	 * @param dOB_TYPE
	 *            the new dob type
	 */
	public void setDOB_TYPE(String dOB_TYPE) {
		DOB_TYPE = dOB_TYPE;
	}

	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public String getGENDER() {
		return GENDER;
	}

	/**
	 * Sets the gender.
	 *
	 * @param gENDER
	 *            the new gender
	 */
	public void setGENDER(String gENDER) {
		GENDER = gENDER;
	}

	

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String eMAIL) {
		
		EMAIL = eMAIL;
	}

	public String getMOBILE_NUMBER() {
		return MOBILE_NUMBER;
	}

	public void setMOBILE_NUMBER(String mOBILE_NUMBER) {
		MOBILE_NUMBER = mOBILE_NUMBER;
	}

}
