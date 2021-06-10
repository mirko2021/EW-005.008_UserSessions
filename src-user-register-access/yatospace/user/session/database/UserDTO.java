package yatospace.user.session.database;

import java.io.Serializable;

/**
 * Поаци који се могу размјењивати за корисника. 
 * @author MV
 * @version 1.0
 */
public class UserDTO implements Serializable{
	private static final long serialVersionUID = 440992618475655674L;
	private String id = "";
	private String username = ""; 
	private String passwordRecored = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		if(id==null) id = ""; 
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		if(username==null) username = ""; 
		this.username = username;
	}
	public String getPasswordRecored() {
		return passwordRecored;
	}
	public void setPasswordRecored(String passwordRecored) {
		if(passwordRecored==null) passwordRecored = ""; 
		this.passwordRecored = passwordRecored;
	} 
}
