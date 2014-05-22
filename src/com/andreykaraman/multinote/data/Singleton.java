package com.andreykaraman.multinote.data;

import java.util.ArrayList;
import java.util.HashSet;

import com.andreykaraman.multinote.model.Note;
import com.andreykaraman.multinote.model.User;
import com.andreykaraman.multinote.data.UserExceptions.Error;


public class Singleton {

    private static Singleton sInstance;
    private HashSet<User> Users;
    private User UserInSystem;

    private ArrayList<Note> Notes;// add

    public static Singleton getInstance() {
	if (sInstance == null) {
	    sInstance = new Singleton();
	}
	return sInstance;
    }

    private Singleton() {
	Users = new HashSet<User>();
	Notes = new ArrayList<Note>();// add

    }

    public void Init() {
	//Notes.add(new Note("Firsttt", "ttttt"));
	//Notes.add(new Note("Seconddd", "dddd"));
    }

    public void InitUser() {
	Users.add(newUser("test", "123"));
	Users.add(newUser("test2", "123"));
    }

    public ArrayList<Note> getNotes() {
	return Notes;
    }

    public void addNote(String noteName, String noteText) {
	//Notes.add(new Note(noteName, noteText));

    }

    public boolean setPassword(User LogInUser, String Old, String NewPass) {
	for (User user : Users) {
	    if (user.getLogin().equals(LogInUser.getLogin()) == true) {
		if (LogInUser.getPass().equals(Old)) {
		    user.setPass(NewPass);
		    return true;
		}
	    }
	}
	return false;
    }

    public void login(String Name, String Pass) throws UserExceptions {
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
	boolean nameFree = true;
	for (User user : Users) {
	    if (user.getLogin() == Login) {
		nameFree = false;
	    }
	}
	if (nameFree) {
	    if (Pass.equals(repeatPass)) {
		Singleton.getInstance().addUser(Login, repeatPass);
	    } else {
		throw new UserExceptions(Error.PASSWORD_MISSMATCH);
	    }
	} else {
	    throw new UserExceptions(Error.THIS_LOGIN_NOT_FREE);
	}

    }

}
