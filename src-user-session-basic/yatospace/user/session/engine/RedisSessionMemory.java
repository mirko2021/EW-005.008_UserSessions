package yatospace.user.session.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import yatospace.user.session.model.Session;

/**
 * Употребна меморија за корисничке сесије. РЕДИС база података. 
 * @author MV
 * @version 1.0
 */
public class RedisSessionMemory implements SessionMemory{
	private Jedis driver;
	
	public RedisSessionMemory(Jedis driver) {
		if(driver==null) throw new RuntimeException(); 
		if(driver!=null) this.driver = driver; 
	}

	public Jedis getDriver() {
		return driver;
	}

	@Override
	public void login(String userId, String sessionId) {
		if(driver.get("sessions:"+sessionId)!=null) return; 
		driver.set("sessions:"+sessionId, userId);
	}	

	@Override
	public void logout(String sessionId) {
		driver.del("sessions:"+sessionId); 
	}

	@Override
	public void logoutAll(String userId) {
		for(Session s: getFor(userId))
			logout(s.getSessionId()); 
	}

	@Override
	public List<Session> getFor(String userId) {
		ArrayList<Session> list = new ArrayList<>();
		List<Session> target = list(); 
		for(Session s: target) {
				if(s.getUserId().contentEquals(userId))
					list.add(s);
		}
		return list;
	}

	@Override
	public List<Session> getFor(String userId, int pageNo, int pageSize, String startFilter) {
		if(startFilter==null) startFilter = ""; 
		if(pageNo<0) pageNo = 0;
		if(pageSize<1) pageSize = 1;
		if(pageNo==0) return new ArrayList<>();
		pageNo--;
		ArrayList<Session> list = new ArrayList<>();
		ArrayList<Session> result = new ArrayList<>();
		List<Session> target = list(); 
		for(Session s: target) {
			if(s.getSessionId().startsWith(startFilter))
				if(s.getUserId().contentEquals(userId))
					list.add(s);
		}
		int a = pageNo*pageSize; 
		int b = pageNo*pageSize+pageSize; 
		a = Math.min(a, list.size());
		b = Math.min(b, list.size());
		for(int i=a; i<b; i++)
			result.add(list.get(i)); 
		return result;
	}

	@Override
	public Session get(String sessionId) {
		if(sessionId==null) return null;
		String userId = driver.get("sessions:"+sessionId); 
		if(userId==null) return null; 
		return new Session(sessionId, userId); 
	}

	@Override
	public int count() {
		return list().size(); 
	}

	@Override
	public int countUsers() {
		return listUsers().size();
	}

	@Override
	public int countFor(String userId) {
		return getFor(userId).size();
	}

	@Override
	public List<Session> list() {
		ScanParams scanParams = new ScanParams();
	    scanParams.match("sessions:*");
	    
	    List<String> result = new ArrayList<>();
	    String cursor = redis.clients.jedis.ScanParams.SCAN_POINTER_START;
	    boolean cycleIsFinished = false;
	    while (!cycleIsFinished) {
	    	
	        ScanResult<String> scanResult = driver.scan(cursor, scanParams);
	        result = scanResult.getResult();

	        cursor = scanResult.getStringCursor();
	        if (cursor.equals("0")) {
	            cycleIsFinished = true;
	        }
	    }
	    
	    ArrayList<Session> sessions = new ArrayList<>(); 
	    Collections.sort(result); 
	    for(String key: result) {
	    	String sessionId = key.substring("sessions:".length());
	    	String userId = driver.get("sessions:"+sessionId); 
	    	Session session = new Session(userId, sessionId);
	    	sessions.add(session);
	    }
	    return sessions; 
	}

	@Override
	public List<Session> list(int pageNo, int pageSize, String startFilter) {
		if(startFilter==null) startFilter = ""; 
		if(pageNo<0) pageNo = 0;
		if(pageSize<1) pageSize = 1;
		if(pageNo==0) return new ArrayList<>(); 
		pageNo--;
		
		List<Session> sessions = list(); 
		ArrayList<Session> list = new ArrayList<>();
		ArrayList<Session> result = new ArrayList<>();
		for(Session s: sessions) {
			if(s.getSessionId().startsWith(startFilter))
				list.add(s); 
		}
		int a = pageNo*pageSize; 
		int b = pageNo*pageSize+pageSize; 
		a = Math.min(a, list.size());
		b = Math.min(b, list.size());
		for(int i=a; i<b; i++)
			result.add(list.get(i)); 
		return result; 
	}

	@Override
	public List<Session> list(int pageNo, int pageSize, String sessionStartFilter, String userStartFilter) {
		if(sessionStartFilter==null) userStartFilter = ""; 
		if(pageNo<0) pageNo = 0;
		if(pageSize<1) pageSize = 1;
		if(pageNo==0) return new ArrayList<>(); 
		pageNo--;
		
		List<Session> sessions = list(); 
		ArrayList<Session> list = new ArrayList<>();
		ArrayList<Session> result = new ArrayList<>();
		for(Session s: sessions) {
			if(s.getSessionId().startsWith(sessionStartFilter))
				if(s.getUserId().startsWith(userStartFilter))
					list.add(s); 
		}
		int a = pageNo*pageSize; 
		int b = pageNo*pageSize+pageSize; 
		a = Math.min(a, list.size());
		b = Math.min(b, list.size());
		for(int i=a; i<b; i++)
			result.add(list.get(i)); 
		return result; 
	}

	@Override
	public List<String> listUsers() {
		List<String> users = new ArrayList<>(); 
		for(Session session: list()) {
			if(!users.contains(session.getUserId())) 
				users.add(session.getUserId()); 
		}
		return users;
	}

	@Override
	public List<String> listUsers(int pageNo, int pageSize, String startFilter) {
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
	
	
}
