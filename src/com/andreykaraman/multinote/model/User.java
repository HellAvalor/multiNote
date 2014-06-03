package com.andreykaraman.multinote.model;

import java.io.Serializable;

public class User implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
	private String Login;
	private String Password;

	public User(String name, String pass) {
		Login = name;
		Password = pass;
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
}
