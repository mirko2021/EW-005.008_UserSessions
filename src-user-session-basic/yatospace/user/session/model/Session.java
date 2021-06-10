package yatospace.user.session.model;

import java.io.Serializable;

/**
 * Општи модел за корисничке сесије. 
 * @author MV
 * @version 1.0
 */
public class Session implements Serializable, Comparable<Session>{
	private static final long serialVersionUID = 3970966847440274259L;
	
	private String userId = "";
	private String sessionId = ""; 
	
	public Session(String userId, String sessionId) {
		if(userId==null) userId = "";
		if(sessionId==null) sessionId = "";
		this.userId = userId; 
		this.sessionId = sessionId; 
	}

	public String getUserId() {
		return userId;
	}

	public String getSessionId() {
		return sessionId;
	}
	
	@Override
	public String toString() {
		return "USER SESSION >> "+ sessionId;		
	}
	
	@Override
	public int hashCode() {
		return sessionId.hashCode(); 
	}
	
	@Override 
	public boolean equals(Object object) {
		if(object instanceof Session) {
			Session session = (Session) object;
			return sessionId.contentEquals(session.sessionId); 
		}
		return false; 
	}

	@Override
	public int compareTo(Session o) {
		return sessionId.compareTo(o.sessionId);
	}
}
