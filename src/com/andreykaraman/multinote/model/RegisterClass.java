package com.andreykaraman.multinote.model;

import java.io.Serializable;

public class RegisterClass implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
	private String login;
	private String password;
	private String repPassword;
	
	
	public RegisterClass(String name, String pass, String rep) {
		this.login = name;
		this.password = pass;
		this.repPassword = rep;
	}

	public String getLogin() {
		return login;
	}

	public String getPass() {
		return password;
	}

	public void setPass(String newPass) {
		password = newPass;
	}

	public String getRepPassword() {
	    return repPassword;
	}

	public void setRepPassword(String repPassword) {
	    this.repPassword = repPassword;
	}
}
