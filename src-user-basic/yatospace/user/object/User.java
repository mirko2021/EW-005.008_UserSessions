package yatospace.user.object;

import java.io.Serializable;

import yatospace.user.model.UserDescription;

/**
 * Објекат којим су односвни подаци о кориснику. 
 * @author MV
 * @version 1.0
 */
public class User implements Serializable, Cloneable, Comparable<User>, UserDescription{
	private static final long serialVersionUID = 3212094005285198170L;
	private String username = ""; 
	
	public String getUsername() {
		return username;
	}

	public User() {}
	public User(String username) {setUsername(username);}
	public User(User user) {if(user!=null) username = user.username;}
	
	public void setUsername(String username) {
		if(username==null) username = ""; 
		this.username = username;
	}

	@Override
	public String toString() {
		return "USER >> "+ username; 
	}
	
	@Override 
	public int hashCode() {
		return username.hashCode(); 
	}
	
	@Override 
	public boolean equals(Object object) {
		if(object instanceof User) {
			User u = (User) object; 
			if(!username.contentEquals(u.username)) return false; 
			return true; 
		}
		return false; 
	}
	
	@Override
	public int compareTo(User user) {
		return username.compareTo(user.username); 
	}
	
	@Override
	public User clone() {
		return new User(this);
	}

	@Override
	public User getUser() {
		return this;
	}
}
