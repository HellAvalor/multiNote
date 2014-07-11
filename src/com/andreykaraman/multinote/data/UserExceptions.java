package com.andreykaraman.multinote.data;

import com.andreykaraman.multinote.R;

import android.content.Context;

public class UserExceptions extends Exception {

    private static final long serialVersionUID = 1L;

    private final Error error;

    public UserExceptions(Error error) {
	this.error = error;
    }

    public enum Error {

	OK(R.string.ok), USER_NOT_FOUND_OR_WRONG_PASS(R.string.wrong_login_pass), WRONG_PASSWORD(
		R.string.wrong_password), NEW_PASS_SAME_AS_PREVIOUS(
		R.string.same_password), WRONG_OLD_PASSWORD(
		R.string.wrong_old_password), THIS_LOGIN_NOT_FREE(
		R.string.login_not_free), PASSWORD_MISSMATCH(
		R.string.passwords_not_match), ERROR_IN_WRITING_TO_EXT_DB(
		R.string.error_db), UNKNOWN_ERROR(R.string.unknown_error);

	private int resId;

	Error(int resId) {
	    this.resId = resId;
	}

	public String resource(Context ctx) {
	    return ctx.getString(resId);
	}
    }

    public Error getError() {
	return error;
    }
}