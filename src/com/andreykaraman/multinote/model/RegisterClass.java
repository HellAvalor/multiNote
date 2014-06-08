package com.andreykaraman.multinote.model;

import java.io.Serializable;

public class RegisterClass implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
	private String Login;
	private String Password;
	private String RepPassword;
	
	
	public RegisterClass(String name, String pass, String rep) {
		Login = name;
		Password = pass;
		setRepPassword(rep);
	}

	public String getLogin() {
		return Login;
	}

	public String getPass() {
		return Password;
	}

	public void setPass(String NewPass) {
		Password = NewPass;
	}

	public String getRepPassword() {
	    return RepPassword;
	}

	public void setRepPassword(String repPassword) {
	    RepPassword = repPassword;
	}
}
