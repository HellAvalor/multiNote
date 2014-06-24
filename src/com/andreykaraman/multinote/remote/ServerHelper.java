package com.andreykaraman.multinote.remote;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

import com.andreykaraman.multinote.data.APIStringConstants;
import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.data.UserExceptions.Error;
import com.andreykaraman.multinote.model.Note;

public class ServerHelper {
    static final String LOG_SECTION = ServerHelper.class.getName();
    private static ServerHelper sInstance;

    private Uri.Builder builder = new Uri.Builder().scheme("http")
	    .encodedAuthority("10.0.3.2:8080");

    public static ServerHelper getInstance() {
	if (sInstance == null) {
	    sInstance = new ServerHelper();
	}
	return sInstance;
    }

    public ArrayList<Note> getNotes(int sessionID) {

	ArrayList<Note> notes = new ArrayList<Note>();
	builder.clearQuery();
	builder.encodedPath(APIStringConstants.REQUEST_SERVICE_URI)
		.appendEncodedPath(APIStringConstants.REQUEST_GET_NOTES_LIST)
		.appendQueryParameter(APIStringConstants.CONST_SESSOIN_ID,
			String.valueOf(sessionID));

	String url = builder.build().toString();

	Log.d("getNotes", url);
	String responce = getStringResponce(url);
	Log.d("getNotes", responce);

	try {
	    JSONObject jsonObject = new JSONObject(responce);

	    JSONArray notesItems = new JSONArray(jsonObject.getString("notes"));

	    // ---print out the content of the json feed---
	    for (int i = 0; i < notesItems.length(); i++) {
		JSONObject jsonNote = notesItems.getJSONObject(i);
		Note note = new Note(
			jsonNote.getInt(APIStringConstants.CONST_NOTE_ID),
			jsonNote.getString(APIStringConstants.CONST_NOTE_TITLE),
			jsonNote.getString(APIStringConstants.CONST_SHORT_CONTENT));
		notes.add(note);
	    }

	} catch (JSONException e) {
	    Log.d("getNotes", e.getLocalizedMessage());
	}
	return notes;
    }

    public int addNote(int sessionId, String title, String content)
	    throws UserExceptions {
	
	builder.clearQuery();
	
	builder.encodedPath(APIStringConstants.REQUEST_SERVICE_URI)
		.appendPath(APIStringConstants.REQUEST_ADD_NOTE)
		.appendQueryParameter(APIStringConstants.CONST_SESSOIN_ID,
			String.valueOf(sessionId))
		.appendQueryParameter(APIStringConstants.CONST_NOTE_TITLE,
			title)
		.appendQueryParameter(APIStringConstants.CONST_NOTE_CONTENT,
			content);
	
	String url = builder.build().toString();
	Log.d("addNote", url);

	String responce = getStringResponce(url);

	Log.d("addNote", "responce " + responce);

	try {
	    JSONObject jsonResponce = new JSONObject(responce);

	    if (jsonResponce.getInt(APIStringConstants.CONST_RESULT) == 1) {
		throw new UserExceptions(Error.ERROR_IN_WRITING_TO_EXT_DB);
	    } else {
		return jsonResponce.getInt(APIStringConstants.CONST_NOTE_ID);
	    }
	} catch (JSONException e) {
	    Log.d("addNote", e.getLocalizedMessage());
	}
	throw new UserExceptions(Error.UNKNOWN_ERROR);
    }

    public Note getNote(int sessionId, long noteId) throws UserExceptions {
	builder.clearQuery();
	builder.encodedPath(APIStringConstants.REQUEST_SERVICE_URI)
		.appendEncodedPath(APIStringConstants.REQUEST_GET_NOTE)
		.appendQueryParameter(APIStringConstants.CONST_SESSOIN_ID,
			String.valueOf(sessionId))
		.appendQueryParameter(APIStringConstants.CONST_NOTE_ID,
			String.valueOf(noteId));

	String url = builder.build().toString();

	Log.d("getNote", url);

	String responce = getStringResponce(url);

	Log.d("getNote", "responce " + responce);

	try {
	    JSONObject jsonResponce = new JSONObject(responce);

	    if (jsonResponce.getInt(APIStringConstants.CONST_RESULT) != 0) {
		throw new UserExceptions(Error.ERROR_IN_WRITING_TO_EXT_DB);
	    } else {
		return new Note(
			noteId,
			jsonResponce
				.getString(APIStringConstants.CONST_NOTE_TITLE),
			jsonResponce
				.getString(APIStringConstants.CONST_NOTE_CONTENT));
	    }
	} catch (JSONException e) {
	    Log.d("getNote", e.getLocalizedMessage());
	}
	throw new UserExceptions(Error.UNKNOWN_ERROR);
    }

    public void editNote(int sessionId, long noteId, String content)
	    throws UserExceptions {
	builder.clearQuery();
	builder.encodedPath(APIStringConstants.REQUEST_SERVICE_URI)
		.appendEncodedPath(APIStringConstants.REQUEST_EDIT_NOTE)
		.appendQueryParameter(APIStringConstants.CONST_SESSOIN_ID,
			String.valueOf(sessionId))
		.appendQueryParameter(APIStringConstants.CONST_NOTE_ID,
			String.valueOf(noteId))
		.appendQueryParameter(APIStringConstants.CONST_NOTE_TEXT,
			content);

	String url = builder.build().toString();

	Log.d("editNote", url);

	String responce = getStringResponce(url);

	try {
	    JSONObject loginResponce = new JSONObject(responce);

	    if (loginResponce.getInt(APIStringConstants.CONST_RESULT) != 0) {
		throw new UserExceptions(Error.ERROR_IN_WRITING_TO_EXT_DB);
	    } else {
		return;
	    }
	} catch (JSONException e) {
	    Log.d("editNote", e.getLocalizedMessage());
	}

	throw new UserExceptions(Error.UNKNOWN_ERROR);
    }

    public int checkLogin(String name, String pass) throws UserExceptions {
	builder.clearQuery();
	builder.encodedPath(APIStringConstants.REQUEST_SERVICE_URI)
		.appendEncodedPath(APIStringConstants.REQUEST_LOGIN)
		.appendQueryParameter(APIStringConstants.CONST_LOGIN, name)
		.appendQueryParameter(APIStringConstants.CONST_PASSWORD, pass);

	String url = builder.build().toString();

	Log.d("checkLogin", url);

	String responce = getStringResponce(url);

	try {
	    JSONObject loginResponce = new JSONObject(responce);

	    if (loginResponce.getInt(APIStringConstants.CONST_RESULT) == 1) {
		throw new UserExceptions(Error.USER_NOT_FOUND_OR_WRONG_PASS);
	    } else {
		return loginResponce
			.getInt(APIStringConstants.CONST_SESSOIN_ID);
	    }
	} catch (JSONException e) {
	    Log.d("checkLogin", e.getLocalizedMessage());
	}

	throw new UserExceptions(Error.UNKNOWN_ERROR);
    }

    public int addUser(String login, String pass) throws UserExceptions {
	builder.clearQuery();
	builder.encodedPath(APIStringConstants.REQUEST_SERVICE_URI)
		.appendEncodedPath(APIStringConstants.REQUEST_REGISTER)
		.appendQueryParameter(APIStringConstants.CONST_LOGIN, login)
		.appendQueryParameter(APIStringConstants.CONST_PASSWORD, pass);

	String url = builder.build().toString();

	Log.d("addUser", url);

	String responce = getStringResponce(url);

	try {
	    JSONObject registerResponce = new JSONObject(responce);
	    switch (registerResponce.getInt("result")) {
	    case 1:
		throw new UserExceptions(Error.THIS_LOGIN_NOT_FREE);
	    case 2:
		throw new UserExceptions(Error.WRONG_PASSWORD);
	    case 0:
		return checkLogin(login, pass);
	    default:
		throw new UserExceptions(Error.UNKNOWN_ERROR);
	    }

	} catch (JSONException e) {
	    Log.d("addUser", e.getLocalizedMessage());
	}
	throw new UserExceptions(Error.UNKNOWN_ERROR);
    }

    public void delNote(int sessionId, int noteId) throws UserExceptions {
	builder.clearQuery();
	builder.encodedPath(APIStringConstants.REQUEST_SERVICE_URI)
		.appendEncodedPath(APIStringConstants.REQUEST_DELETE_NOTE)
		.appendQueryParameter(APIStringConstants.CONST_SESSOIN_ID,
			String.valueOf(sessionId))
		.appendQueryParameter(APIStringConstants.CONST_NOTE_ID,
			String.valueOf(noteId));

	String url = builder.build().toString();

	Log.d("delNote", url);

	String responce = getStringResponce(url);
	Log.d("delNote", "responce " + responce);

	try {
	    JSONObject jsonResponce = new JSONObject(responce);

	    if (jsonResponce.getInt(APIStringConstants.CONST_RESULT) == 0) {
		return;
	    } else {
		throw new UserExceptions(Error.ERROR_IN_WRITING_TO_EXT_DB);
	    }
	} catch (JSONException e) {
	    Log.d("delNote", e.getLocalizedMessage());
	}
	throw new UserExceptions(Error.UNKNOWN_ERROR);
    }

    public int registrationNewUser(String login, String pass, String repeatPass)
	    throws UserExceptions {

	if (pass.equals(repeatPass)) {
	    return addUser(login, repeatPass);
	} else {
	    throw new UserExceptions(Error.PASSWORD_MISSMATCH);
	}
    }

    private String getStringResponce(String query) {

	StringBuilder stringBuilder = new StringBuilder();
	HttpClient httpClient = new DefaultHttpClient();
	HttpGet httpGet = new HttpGet(query);
	try {
	    HttpResponse response = httpClient.execute(httpGet);
	    StatusLine statusLine = response.getStatusLine();

	    int statusCode = statusLine.getStatusCode();
	    Log.d(LOG_SECTION, "statusCode " + statusCode);
	    if (statusCode == 200) {
		HttpEntity entity = response.getEntity();
		InputStream inputStream = entity.getContent();
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(inputStream));
		String line;
		while ((line = reader.readLine()) != null) {
		    stringBuilder.append(line);
		}
		inputStream.close();
	    } else {
		Log.d("JSON", "Failed to download file");
	    }
	} catch (Exception e) {
	    Log.d("readJSONFeed", e.getLocalizedMessage());
	}
	return stringBuilder.toString();
    }

    public void logout(int sessionId) throws UserExceptions {
	builder.clearQuery();
	builder.encodedPath(APIStringConstants.REQUEST_SERVICE_URI)
		.appendEncodedPath(APIStringConstants.REQUEST_LOGOUT)
		.appendQueryParameter(APIStringConstants.CONST_SESSOIN_ID,
			String.valueOf(sessionId));

	String url = builder.build().toString();

	Log.d("logout", url);

	String responce = getStringResponce(url);

	try {
	    JSONObject loginResponce = new JSONObject(responce);

	    if (loginResponce.getInt(APIStringConstants.CONST_RESULT) == 1) {
		throw new UserExceptions(Error.UNKNOWN_ERROR);
	    } else {
		return;
	    }
	} catch (JSONException e) {

	    Log.d("logout", e.getLocalizedMessage());
	}
	throw new UserExceptions(Error.UNKNOWN_ERROR);
    }

    public void changePass(int sessionId, String oldPass, String newPass)
	    throws UserExceptions {
	
	builder.clearQuery(); 
	
	builder.encodedPath(APIStringConstants.REQUEST_SERVICE_URI)
		.appendEncodedPath(APIStringConstants.REQUEST_CHANGE_PASSWORD)
		.appendQueryParameter(APIStringConstants.CONST_SESSOIN_ID,
			String.valueOf(sessionId))
		.appendQueryParameter(APIStringConstants.CONST_OLD_PASS,
			oldPass)
		.appendQueryParameter(APIStringConstants.CONST_NEW_PASS,
			newPass);

	String url = builder.build().toString();

	Log.d("changePass", url);

	String responce = getStringResponce(url);

	Log.d("changePass", "responce " + responce);

	try {
	    JSONObject loginResponce = new JSONObject(responce);
	    if (loginResponce.getInt(APIStringConstants.CONST_RESULT) == 2) {
		throw new UserExceptions(Error.WRONG_OLD_PASSWORD);
	    } else if (loginResponce.getInt(APIStringConstants.CONST_RESULT) == 0) {
		return;
	    }
	} catch (JSONException e) {

	    Log.d("changePass", e.getLocalizedMessage());
	}

	throw new UserExceptions(Error.UNKNOWN_ERROR);
    }
}
