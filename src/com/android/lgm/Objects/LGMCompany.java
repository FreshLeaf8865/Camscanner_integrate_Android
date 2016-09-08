package com.android.lgm.Objects;


public class LGMCompany{
	private String ID;
	private String customID;
	private String compName;
	private String logo;
	private String mailSubject;
	private String mailAddress;
	
	public LGMCompany() {}
	
	
	
	public LGMCompany(String iD, String customID, String compName, String logo,
			String mailSubject, String mailAddress) {
		ID = iD;
		this.customID = customID;
		this.compName = compName;
		this.logo = logo;
		this.mailSubject = mailSubject;
		this.mailAddress = mailAddress;
	}



	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}



	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}



	/**
	 * @return the customID
	 */
	public String getCustomID() {
		return customID;
	}



	/**
	 * @param customID the customID to set
	 */
	public void setCustomID(String customID) {
		this.customID = customID;
	}



	/**
	 * @return the compName
	 */
	public String getCompName() {
		return compName;
	}



	/**
	 * @param compName the compName to set
	 */
	public void setCompName(String compName) {
		this.compName = compName;
	}



	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}



	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}



	/**
	 * @return the mailSubject
	 */
	public String getMailSubject() {
		return mailSubject;
	}



	/**
	 * @param mailSubject the mailSubject to set
	 */
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}



	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}



	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}


	@Override
	public String toString() {
		return "Company Name: "+compName;
	}
}
