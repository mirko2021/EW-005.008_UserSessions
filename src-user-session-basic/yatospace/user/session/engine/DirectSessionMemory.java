package yatospace.user.session.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yatospace.user.session.model.Session;

/**
 * Општа мемеорија за сесије. 
 * @author MV
 * @version 1.0
 */
public class DirectSessionMemory implements SessionMemory, Serializable{
	private static final long serialVersionUID = 3832593743086871335L;
	private HashMap<String, Session> sessions = new HashMap<>(); 
	
	@Override
	public synchronized void login(String userId, String sessionId) {
		Session session = new Session(userId, sessionId);
		sessions.put(sessionId, session); 
	}

	@Override
	public synchronized void logout(String sessionId) {
		sessions.remove(sessionId); 
	}

	@Override
	public synchronized void logoutAll(String userId) {
		for(Map.Entry<String,Session> me: new ArrayList<>(sessions.entrySet())) {
			if(me.getValue().getUserId().contentEquals(userId))
				sessions.remove(userId);
		}
	}

	@Override
	public synchronized List<Session> getFor(String userId) {
		ArrayList<Session> result = new ArrayList<>(); 
		for(Map.Entry<String,Session> me: new ArrayList<>(sessions.entrySet())) 
			if(me.getValue().getUserId().contentEquals(userId))
				result.add(me.getValue()); 
		return result; 
	}

	@Override
	public synchronized Session get(String sessionId) {
		return sessions.get(sessionId);
	}

	@Override
	public synchronized List<Session> getFor(String userId, int pageNo, int pageSize, String startFilter) {
		if(startFilter==null) startFilter = ""; 
		if(pageNo<0) pageNo = 0;
		if(pageSize<1) pageSize = 1;
		if(pageNo==0) return new ArrayList<>();
		pageNo--;
		ArrayList<Session> list = new ArrayList<>();
		ArrayList<String> target = new ArrayList<>(sessions.keySet());
		ArrayList<String> temp = target;
		target = new ArrayList<>(); 
		for(String sessionId: temp)
			if(sessionId.startsWith(startFilter))
				if(sessions.get(sessionId).getUserId().contentEquals(userId))
					target.add(sessionId);
		Collections.sort(target);
		int a = pageNo*pageSize; 
		int b = pageNo*pageSize+pageSize; 
		a = Math.min(a, target.size());
		b = Math.min(b, target.size());
		Collections.sort(list);
		for(int i=a; i<b; i++)
			list.add(sessions.get(target.get(i))); 
		return list;
	}

	@Override
	public synchronized int count() {
		return sessions.size();
	}

	@Override
	public synchronized List<Session> list() {
		return new ArrayList<>(sessions.values());
	}

	@Override
	public synchronized List<Session> list(int pageNo, int pageSize, String startFilter) {
		if(startFilter==null) startFilter = ""; 
		if(pageNo<0) pageNo = 0;
		if(pageSize<1) pageSize = 1;
		if(pageNo==0) return new ArrayList<>(); 
		pageNo--; 
		ArrayList<String> target = new ArrayList<>(sessions.keySet());
		ArrayList<String> temp = target; 
		target = new ArrayList<>(); 
		for(String sessionId: temp)
			if(sessionId.startsWith(startFilter)) target.add(sessionId); 
		ArrayList<Session> list = new ArrayList<>();
		int a = pageNo*pageSize; 
		int b = pageNo*pageSize+pageSize; 
		a = Math.min(a, target.size());
		b = Math.min(b, target.size());
		Collections.sort(target);
		for(int i=a; i<b; i++)
			list.add(sessions.get(target.get(i))); 
		return list; 
	}

	@Override
	public synchronized int countUsers() {
		List<String> users = new ArrayList<>(); 
		for(Session session: sessions.values()) {
			if(!users.contains(session.getUserId())) 
				users.add(session.getUserId()); 
		}
		return users.size();
	}

	@Override
	public synchronized int countFor(String userId) {
		int n = 0; 
		for(Session session: sessions.values())  
			if(session.getUserId().contentEquals(userId)) n++;
		return n;
	}

	@Override
	public synchronized List<String> listUsers() {
		List<String> users = new ArrayList<>(); 
		for(Session session: sessions.values()) {
			if(!users.contains(session.getUserId())) 
				users.add(session.getUserId()); 
		}
		return users;
	}

	@Override
	public synchronized List<String> listUsers(int pageNo, int pageSize, String startFilter) {
		if(startFilter==null) startFilter = ""; 
		List<String> users = listUsers(); 
		List<String> temp = users; 
		users = new ArrayList<>(); 
		for(String user: temp) 
			if(user.startsWith(startFilter)) users.add(user); 
		List<String> result  = new ArrayList<>(); 
		Collections.sort(users); 
		if(pageNo<0) pageNo=0; 
		if(pageSize<1) pageSize=1;
		pageNo--; 
		int a = pageNo*pageSize; 
		int b = pageNo*pageSize+pageSize; 
		a = Math.min(a, users.size());
		b = Math.min(b, users.size());
		for(int i=a; i<b; i++) result.add(users.get(i));
		return result;
	}

	@Override
	public synchronized List<Session> list(int pageNo, int pageSize, String sessionStartFilter, String userStartFilter) {
		if(sessionStartFilter==null) sessionStartFilter = ""; 
		if(userStartFilter==null) userStartFilter = ""; 
		if(pageNo<0) pageNo = 0;
		if(pageSize<1) pageSize = 1;
		if(pageNo==0) return new ArrayList<>(); 
		pageNo--;
		ArrayList<String> target = new ArrayList<>(sessions.keySet());
		ArrayList<String> temp = target; 
		target = new ArrayList<>(); 
		for(String sessionId: temp)
			if(sessionId.startsWith(sessionStartFilter)) 
				if(sessions.get(sessionId).getUserId().startsWith(userStartFilter))
					target.add(sessionId); 
		ArrayList<Session> list = new ArrayList<>();
		int a = pageNo*pageSize; 
		int b = pageNo*pageSize+pageSize; 
		a = Math.min(a, target.size());
		b = Math.min(b, target.size());
		Collections.sort(target);
		for(int i=a; i<b; i++)
			list.add(sessions.get(target.get(i))); 
		return list; 
	}
}
