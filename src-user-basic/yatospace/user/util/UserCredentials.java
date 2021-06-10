package yatospace.user.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Base64;

import yatospace.user.constants.UserExceptionMessageCodebook;
import yatospace.user.lang.UserCredentialsExcetption;
import yatospace.user.model.UserDescription;
import yatospace.user.object.User;

/**
 * Баратање са креденцијалима за кориснике. 
 * @author MV
 * @version 1.0
 */
public class UserCredentials implements Serializable, UserDescription{
	private static final long serialVersionUID = -2350982691065382917L;
	private User user = new User();
	private String passwordPlain = "";
	private String passwordRecord = "";
	private String passwordHash = "";
	private String passwordSalt = ""; 
	
	@Override
	public User getUser() {
		return user;
	}
	public UserCredentials setUser(User user) {
		if(user==null) user = new User(); 
		this.user = user;
		return this; 
	}
	public String getPasswordPlain() {
		return passwordPlain;
	}
	public UserCredentials setPasswordPlain(String passwordPlain) {
		if(passwordPlain==null) passwordPlain = ""; 
		this.passwordPlain = passwordPlain;
		return this; 
	}
	public String getPasswordRecord() {
		return passwordRecord;
	}
	public UserCredentials setPasswordRecord(String passwordRecord) {
		if(passwordRecord==null) passwordRecord = ""; 
		this.passwordRecord = passwordRecord;
		return this; 
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public  UserCredentials setPasswordHash(String passwordHash) {
		if(passwordHash==null) passwordHash = "";
		this.passwordHash = passwordHash;
		return this; 
	}
	public String getPasswordSalt() {
		return passwordSalt;
	}
	public  UserCredentials setPasswordSalt(String passwordSalt) {
		if(passwordSalt==null) passwordSalt = ""; 
		this.passwordSalt = passwordSalt;
		return this; 
	}
	
	public UserCredentials resetPasswordPlain() {
		passwordPlain = "";
		return this; 
	}
	
	public UserCredentials resetPasswordRecord() {
		passwordRecord = ""; 
		return this; 
	}
	
	public UserCredentials resetPasswordHash() {
		passwordHash = "";
		return this;
	}
	
	public UserCredentials resetPasswordSalt() {
		passwordSalt = "";
		return this; 
	}
	
	public UserCredentials resetPasswordHashSaltCombination() {
		passwordHash = "";
		passwordSalt = "";
		return this; 
	}
	
	public boolean hasHashSaltCombination() {
		return passwordHash.length()!=0 && passwordSalt.length()!=0;
	}
	
	public boolean hasPasswordHash() {
		return passwordHash.length()!=0; 
	}
	
	public boolean hasPasswordSalt() {
		return passwordSalt.length()!=0;
	}
	
	public boolean hasPasswordRecord() {
		return passwordRecord.length()!=0; 
	}
	
	public boolean hasPasswordPlain() {
		return passwordPlain.length()!=0; 
	}
	
	public UserCredentials convertHashSaltCombinationToRecord() {
		if(!hasHashSaltCombination()) return this; 
		passwordRecord = passwordSalt+"$"+passwordHash;  
		return this;
	}
	
	public UserCredentials convertRecordToHashSaltCombination() throws UserCredentialsExcetption{
		if(passwordRecord.length()==0) return this; 
		String parts[] = passwordRecord.split("\\$"); 
		if(parts.length!=2) throw  new UserCredentialsExcetption(UserExceptionMessageCodebook.WRONG_PASSWORD_RECORD)
									   .setCredentials(this); 
		passwordSalt = parts[0]; 
		passwordHash = parts[1]; 
		return this; 
	}
	
	public UserCredentials hashPasswordToHashSaltCombination() throws UserCredentialsExcetption{
		try {
			if(passwordSalt.length()==0)   throw new UserCredentialsExcetption(UserExceptionMessageCodebook.NO_SALT).setCredentials(this); 
			String base64 = Base64.getEncoder().encodeToString(passwordPlain.getBytes("UTF-8")); 
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] digest = base64.getBytes("UTF-8"); 
			for(int i=0; i<1000; i++) 
				digest = messageDigest.digest(digest);
			passwordHash = Base64.getEncoder().encodeToString(digest);  
			return this; 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	public UserCredentials hashPasswordToRecord() throws UserCredentialsExcetption{
		try {
			if(passwordSalt.length()==0)   throw new UserCredentialsExcetption(UserExceptionMessageCodebook.NO_SALT).setCredentials(this); 
			String base64 = Base64.getEncoder().encodeToString(passwordPlain.getBytes("UTF-8")); 
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] digest = base64.getBytes("UTF-8"); 
			for(int i=0; i<1000; i++) 
				digest = messageDigest.digest(digest);
			passwordRecord = passwordSalt+"$"+Base64.getEncoder().encodeToString(digest);  
			return this; 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	public UserCredentials hashPasswordToAll() throws UserCredentialsExcetption{
		try {
			if(passwordSalt.length()==0)   throw new UserCredentialsExcetption(UserExceptionMessageCodebook.NO_SALT).setCredentials(this); 
			String base64 = Base64.getEncoder().encodeToString(passwordPlain.getBytes("UTF-8")); 
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] digest = base64.getBytes("UTF-8"); 
			for(int i=0; i<1000; i++) 
				digest = messageDigest.digest(digest);
			passwordHash = Base64.getEncoder().encodeToString(digest);  
			passwordRecord = passwordSalt+"$"+Base64.getEncoder().encodeToString(digest);  
			return this; 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	
	public boolean check() throws UserCredentialsExcetption{
			try {
				if(passwordSalt.length()==0)   throw new UserCredentialsExcetption(UserExceptionMessageCodebook.NO_SALT).setCredentials(this); 
				String base64 = Base64.getEncoder().encodeToString(passwordPlain.getBytes("UTF-8")); 
				MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
				byte[] digest = base64.getBytes("UTF-8"); 
				for(int i=0; i<1000; i++) 
					digest = messageDigest.digest(digest);
				String hash = Base64.getEncoder().encodeToString(digest);  
				String record = passwordSalt+"$"+Base64.getEncoder().encodeToString(digest);  
				if(passwordHash.length()!=0) return passwordHash.contentEquals(hash); 
				if(passwordRecord.length()!=0) return passwordRecord.contentEquals(record); 
				return false; 
			}catch(RuntimeException ex) {
				throw ex; 
			}catch(Exception ex) {
				throw new RuntimeException(ex); 
			}
	}
}
