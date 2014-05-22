package com.andreykaraman.multinote.data;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class LoginLoader extends AsyncTaskLoader<ResponseLogin> {

    private String mLogin;
    private String mPassword;

    public void setLoginAndPasswrod(String login, String password) {
        mLogin = login;
        mPassword = password;
    }

    public LoginLoader(Context context) {
        super(context);
    }

    public String getmLogin() {
        return mLogin;
    }

    public void setmLogin(String mLogin) {
        this.mLogin = mLogin;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    @Override
    public ResponseLogin loadInBackground() {
        try {
            Singleton.getInstance().login(mLogin, mPassword);
        } catch (UserExceptions e) {
            return new ResponseLogin(false, e.getError());
        }
        return new ResponseLogin(true, null);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mLogin == null || mPassword == null) {
            return;
        } else {
            forceLoad();
        }
    }
}