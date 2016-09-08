package com.android.lgm.Objects;

import java.util.ArrayList;

public class Login {
	private boolean status;
	private String userName;
	private String password;
	private String email;
	private String name;
	private String message;
	private ArrayList<LGMCompany> lstCompany;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<LGMCompany> getLstCompany() {
		return lstCompany;
	}
	public void setLstCompany(ArrayList<LGMCompany> lstCompany) {
		this.lstCompany = lstCompany;
	}
	
}
