package com.andreykaraman.multinote.model;

public class User {

    private String login;
    private String password;

    public User(String name, String pass) {
	login = name;
	password = pass;
    }

    public String getLogin() {
	return login;
    }

    public String getPass() {
	return password;
    }

    public void setPass(String pass) {
	password = pass;
    }
}
