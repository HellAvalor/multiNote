package com.andreykaraman.multinote.model;

import java.io.Serializable;

public class ChangePassClass implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
	private int sessionId;
	private String oldPassword;
	private String newPassword;
	
	
	public ChangePassClass(int sessionId, String oldPassword, String newPassword) {
	    this.setSessionId(sessionId);
	    this.setOldPassword(oldPassword);
	    this.setNewPassword(newPassword);
	}


	public int getSessionId() {
	    return sessionId;
	}


	public void setSessionId(int sessionId) {
	    this.sessionId = sessionId;
	}


	public String getOldPassword() {
	    return oldPassword;
	}


	public void setOldPassword(String oldPassword) {
	    this.oldPassword = oldPassword;
	}


	public String getNewPassword() {
	    return newPassword;
	}


	public void setNewPassword(String newPassword) {
	    this.newPassword = newPassword;
	}

	
}
