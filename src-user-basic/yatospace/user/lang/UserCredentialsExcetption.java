package yatospace.user.lang;

import yatospace.user.object.User;
import yatospace.user.type.UserException;
import yatospace.user.util.UserCredentials;

/**
 * Изузетци при раду са креденцијалима корисника.
 * @author MV
 * @version 1.0
 */
public class UserCredentialsExcetption extends RuntimeException implements UserException{
	private static final long serialVersionUID = 5261128796620954240L;
	private UserCredentials credentials = new UserCredentials(); 
	
	public UserCredentials getCredentials() {
		return credentials;
	}

	public UserCredentialsExcetption() {
		super();
	}



	public UserCredentialsExcetption(String message, Throwable cause) {
		super(message, cause);
	}

	public UserCredentialsExcetption(String message) {
		super(message); 
	}

	public UserCredentialsExcetption(Throwable cause) {
		super(cause);
	}



	public UserCredentialsExcetption setCredentials(UserCredentials credentials) {
		if(credentials==null) credentials = new UserCredentials(); 
		this.credentials = credentials;
		return this; 
	}

	@Override
	public User getUser() {
		return credentials.getUser();
	}
}
