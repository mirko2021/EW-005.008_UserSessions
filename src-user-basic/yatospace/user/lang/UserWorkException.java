package yatospace.user.lang;

import yatospace.user.object.User;
import yatospace.user.type.UserException;

/**
 * Грешке при раду са корисницима. 
 * @author MV
 * @version 1.0
 */
public class UserWorkException extends RuntimeException implements UserException{
	private static final long serialVersionUID = 691641468372442414L;
	private User user = new User(); 
	
	public UserWorkException() {
		super();
	}

	public UserWorkException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UserWorkException(String arg0) {
		super(arg0);
	}

	public UserWorkException(Throwable arg0) {
		super(arg0);
	}

	@Override
	public User getUser() {
		return user;
	}

	public UserWorkException setUser(User user) {
		this.user = user;
		return this; 
	}
}
