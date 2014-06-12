package com.andreykaraman.multinote.data;

public class UserExceptions extends Exception {

    private static final long serialVersionUID = 1L;

    private final Error error;

    public UserExceptions(Error error) {
	this.error = error;
    }

    public enum Error {
	OK, USER_NOT_FOUND_OR_WRONG_PASS, WRONG_PASSWORD, NEW_PASS_SAME_AS_PREVIOUS, 
	WRONG_OLD_PASSWORD, THIS_LOGIN_NOT_FREE, PASSWORD_MISSMATCH,
	ERROR_IN_WRITING_TO_EXT_DB, UNKNOWN_ERROR
    }

    public Error getError() {
	return error;
    }
}