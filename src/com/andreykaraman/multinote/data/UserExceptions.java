package com.andreykaraman.multinote.data;

public class UserExceptions extends Exception {

    private static final long serialVersionUID = 1L;

    private final Error error;

    public UserExceptions(Error error) {
	this.error = error;
    }

    public enum Error {
	OK, USER_NOT_FOUND, WRONG_PASSWORD, NEW_PASS_SAME_AS_PREVIOUS, WRONG_OLD_PASSWORD, THIS_LOGIN_NOT_FREE, PASSWORD_MISSMATCH
    }

    public Error getError() {
	return error;
    }

}
