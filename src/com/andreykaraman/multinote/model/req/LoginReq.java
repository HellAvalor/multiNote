package com.andreykaraman.multinote.model.req;

import com.andreykaraman.multinote.model.User;

public class LoginReq extends User {

    private static final long serialVersionUID = 1L;

    public LoginReq(String name, String pass) {
	super(name, pass);
    }
}
