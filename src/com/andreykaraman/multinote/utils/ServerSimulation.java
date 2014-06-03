package com.andreykaraman.multinote.utils;

import java.util.ArrayList;
import java.util.HashSet;

import android.util.Log;

import com.andreykaraman.multinote.model.Note;
import com.andreykaraman.multinote.model.User;
import com.andreykaraman.multinote.ui.login.MainActivity;
import com.andreykaraman.multinote.data.UserExceptions;
import com.andreykaraman.multinote.data.UserExceptions.Error;

public class ServerSimulation {
    static final String LOG_SECTION = MainActivity.class.getName();
    private static ServerSimulation sInstance;
    private HashSet<User> Users;
    private User UserInSystem;

    private ArrayList<Note> Notes;// add

    public static ServerSimulation getInstance() {
	if (sInstance == null) {
	    sInstance = new ServerSimulation();
	}
	return sInstance;
    }

    private ServerSimulation() {
	Users = new HashSet<User>();
	Notes = new ArrayList<Note>();// add

    }

    public void Init() {
	Log.d(LOG_SECTION, "initNotes");
	Notes.add(new Note("Firsttt", "ttttt"));
	Notes.add(new Note("Seconddd", "dddd"));
    }

    public void InitUser() {
	Users.add(newUser("test", "123"));
	Users.add(newUser("test2", "123"));
    }

    public ArrayList<Note> getNotes() {
	Log.d(LOG_SECTION, "getNotes");
	return Notes;
    }

    public void addNote(String noteName, String noteText) {
	Notes.add(new Note(noteName, noteText));

    }

    public boolean setPassword(User LogInUser, String Old, String NewPass)
	    throws UserExceptions {
	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	for (User user : Users) {
	    if (user.getLogin().equals(LogInUser.getLogin()) == true) {
		if (LogInUser.getPass().equals(Old)) {
		    if (!LogInUser.getPass().equals(NewPass)) {
			user.setPass(NewPass);
			return true;
		    } else {
			throw new UserExceptions(
				Error.NEW_PASS_SAME_AS_PREVIOUS);
		    }
		} else {
		    throw new UserExceptions(Error.WRONG_OLD_PASSWORD);
		}
	    }
	}
	throw new UserExceptions(Error.USER_NOT_FOUND);
    }

    public void checkLogin(String Name, String Pass) throws UserExceptions {
	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	for (User user : Users) {
	    if (user.getLogin().equals(Name)) {
		if (user.getPass().equals(Pass)) {
		    setUserInSystem(user);
		    return;
		} else {
		    throw new UserExceptions(Error.WRONG_PASSWORD);
		}
	    }
	}
	throw new UserExceptions(Error.USER_NOT_FOUND);
    }

    public void checkLogin(User requestUser) throws UserExceptions {
	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	for (User user : Users) {
	    if (user.getLogin().equals(requestUser.getLogin())) {
		if (user.getPass().equals(requestUser.getPass())) {
		    setUserInSystem(user);
		    return;
		} else {
		    throw new UserExceptions(Error.WRONG_PASSWORD);
		}
	    }
	}
	throw new UserExceptions(Error.USER_NOT_FOUND);
    }
    
    public User getUserInSystem() {
	return UserInSystem;
    }

    private void setUserInSystem(User userInSystem) {
	UserInSystem = userInSystem;
    }

    private User newUser(String Login, String Pass) {
	return new User(Login, Pass);
    }

    public void addUser(String Login, String Pass) {
	Users.add(newUser(Login, Pass));
    }

    public void editNote(int position, String NewTitle, String NewDescription) {
	Notes.get(position).setNoteTitle(NewTitle);
	Notes.get(position).setNoteContent(NewDescription);

    }

    public void removeNote(int position) {
	Notes.remove(position);
    }

    public void registrationNewUser(String Login, String Pass, String repeatPass)
	    throws UserExceptions {

	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	boolean nameFree = true;
	for (User user : Users) {
	    if (user.getLogin().equals(Login)) {
		nameFree = false;
	    }
	}
	if (nameFree) {
	    if (Pass.equals(repeatPass)) {
		ServerSimulation.getInstance().addUser(Login, repeatPass);
	    } else {
		throw new UserExceptions(Error.PASSWORD_MISSMATCH);
	    }
	} else {
	    throw new UserExceptions(Error.THIS_LOGIN_NOT_FREE);
	}

    }

}
