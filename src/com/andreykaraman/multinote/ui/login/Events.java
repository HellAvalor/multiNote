package com.andreykaraman.multinote.ui.login;

import com.andreykaraman.multinote.events.ResponceComplexEvent;
import com.andreykaraman.multinote.model.User;

public class Events {

    public static class LoginRequest extends User{
	    public LoginRequest(String name, String pass) {
		super(name, pass);
	    }
    }

    public static class LoginResponse extends ResponceComplexEvent {
    }

    public static class RegisterRequest {
	  private String login;
	    private String password;
	    private String repPassword;

	    public RegisterRequest(String name, String pass, String rep) {
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

    public static class RegisterResponse extends ResponceComplexEvent {
    }
}
