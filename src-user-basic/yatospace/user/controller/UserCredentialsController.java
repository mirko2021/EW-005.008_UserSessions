package yatospace.user.controller;

import yatospace.user.object.User;
import yatospace.user.util.SaltGeneratorEngine;
import yatospace.user.util.UserCredentials;

/**
 * Контролне јединице када су у питању креденцијали за корисника. 
 * @author MV
 * @version 1.0
 */
public class UserCredentialsController {
	private UserCredentials credentialsTool = new UserCredentials();
    private SaltGeneratorEngine saltGeneratorTool = new SaltGeneratorEngine();
	
    public UserCredentialsController() {
    	String salt = saltGeneratorTool.generateNewCode(); 
    	credentialsTool.setPasswordSalt(salt); 
    }
    
    public UserCredentials getCredentialsTool() {
		return credentialsTool;
	}
	public UserCredentialsController setCredentialsTool(UserCredentials credentialsTool) {
		if(credentialsTool==null) {
			credentialsTool = new UserCredentials(); 
			String salt = saltGeneratorTool.generateNewCode(); 
	    	credentialsTool.setPasswordSalt(salt); 
		}
		this.credentialsTool = credentialsTool;
		return this; 
	}
	public SaltGeneratorEngine getSaltGeneratorTool() {
		return saltGeneratorTool;
	}
	public UserCredentialsController setSaltGeneratorTool(SaltGeneratorEngine saltGeneratorTool) {
		if(saltGeneratorTool==null) saltGeneratorTool = new SaltGeneratorEngine(); 
		this.saltGeneratorTool = saltGeneratorTool;
		return this; 
	} 
	
	public User getUser() {
		return credentialsTool.getUser();
	}
	
	public UserCredentialsController setUser(User user) {
		if(user==null) user = new User();
		credentialsTool.setUser(user); 
		return this; 
	}
	
	public String setPassword(String password) {
		if(password==null) password = ""; 
		String salt = saltGeneratorTool.generateNewCode(); 
		return credentialsTool.setPasswordPlain(password).setPasswordSalt(salt).hashPasswordToAll().getPasswordRecord(); 
	}
	
	public String setGoodPassword(String password) {
		if(password==null) password = ""; 
		if(password.length()<8) return ""; 
		int upperCount = 0; 
		int lowerCount = 0; 
		int numberCount = 0; 
		for(char ch: password.toCharArray())
			if(Character.isDigit(ch)) numberCount++; 
			else if(Character.isUpperCase(ch)) upperCount++; 
			else if(Character.isLowerCase(ch)) lowerCount++; 
		if(upperCount==0 || lowerCount==0 || numberCount==0) return ""; 
		String salt = saltGeneratorTool.generateNewCode(); 
		return credentialsTool.setPasswordPlain(password).setPasswordSalt(salt).hashPasswordToAll().getPasswordRecord(); 
	}
	
	
	public String setGoodUsername(String username) {
		if(username==null) username = ""; 
		if(username.trim().length()==0) return "";
		char first = username.charAt(0); 
		char last = username.charAt(username.length()-1); 
		if(!Character.isUpperCase(first) && !Character.isLowerCase(first) && first=='_' && first=='$' && first=='#' && first=='~') return ""; 
		if(!Character.isUpperCase(last) && !Character.isLowerCase(last) && last=='_' && last=='$' && last=='#' && first=='~') return ""; 
		for(char ch: username.toCharArray()) {
			if(Character.isUpperCase(ch)) continue;
			if(Character.isLowerCase(ch)) continue;
			if(Character.isDigit(ch)) continue;
			if(ch=='_') continue;
			if(ch=='$') continue;
			if(ch=='#') continue;
			if(ch=='~') continue;
			if(ch=='-') continue;
			if(ch=='.') continue;
			return "";
		}
		credentialsTool.setUser(new User(username)); 
		return username; 
	}
	
	
	public boolean checkPassword(String password) {
		if(password==null) return false; 
		return credentialsTool.setPasswordPlain(password).check(); 
	}
}
