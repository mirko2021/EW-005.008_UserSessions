package yatospace.user.session.engine;

import java.util.List;

import yatospace.user.session.model.Session;

/**
 * Општи оквир за меморију сесија. 
 * @author MV
 * @version 1.0 
 */
public interface SessionMemory {
	public void login(String userId, String sessionId); 
	public void logout(String sessionId); 
	public void logoutAll(String userId);
	public List<Session> getFor(String userId);
	public List<Session> getFor(String userId, int pageNo, int pageSize, String startFilter);
	public Session get(String sessionId);
	public int count();
	public int countUsers();
	public int countFor(String userId); 
	public List<Session> list();
	public List<Session> list(int pageNo, int pageSize, String startFilter);
	public List<Session> list(int pageNo, int pageSize, String sessionStartFilter, String userStartFilter);
	public List<String> listUsers(); 
	public List<String> listUsers(int pageNo, int pageSize, String startFilter); 
}
