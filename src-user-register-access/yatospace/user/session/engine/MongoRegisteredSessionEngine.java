package yatospace.user.session.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yatospace.user.session.database.UserDAO;
import yatospace.user.session.database.UserDTO;
import yatospace.user.session.model.Session;
import yatospace.user.session.util.MongoRegisterSessionsUtilitizer;
import yatospace.user.session.util.SessionsGarbageCollector;
import yatospace.user.util.UserCredentials;

/**
 * Алатка комом се може вршити логовање и одјава корисника. 
 * @author MV
 * @version 1.0
 */
public class MongoRegisteredSessionEngine extends MongoSessionMemory{
	private transient UserDAO userDAO;
	private transient MongoRegisterSessionsUtilitizer registerUtility; 
	private transient SessionsGarbageCollector sessionGC;
	
	public MongoRegisteredSessionEngine(String host, int port, String database, String collection, UserDAO userDAO, SessionsGarbageCollector sessionGC) {
		super(host, port, database, collection);
		this.userDAO = userDAO;
		this.registerUtility = new MongoRegisterSessionsUtilitizer(userDAO, this);
		this.sessionGC =  sessionGC;
		this.sessionGC.setUtilitizer(registerUtility);
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public MongoRegisterSessionsUtilitizer getRegisterUtility() {
		return registerUtility;
	}
		
	public SessionsGarbageCollector getSessionGC() {
		return sessionGC;
	}

	public void sessionTest(String id) {
		try {
			if(id==null) return; 
			if(id.trim().length()==0) return; 
			UserDTO user = userDAO.getById(id);
			if(user==null) { 
				sessionGC.push(()->logoutAllByUserId(id)); 
			}
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	public boolean login(String username, String password, String sessionId) {
		try {
			UserDTO user = userDAO.get(username);
			UserCredentials credentials = new UserCredentials();
			credentials.setPasswordRecord(user.getPasswordRecored());
			credentials.convertRecordToHashSaltCombination();
			credentials.setPasswordPlain(password);
			if(!credentials.check()) return false;
			credentials.resetPasswordPlain();
			if(get(sessionId)!=null) return false;
			super.login(user.getId(), sessionId);
			return true;
		}catch(Exception ex) {
			return false;
		}
	}
	
	@Override
	@Deprecated
	public void login(String userId, String sessionId) {
		throw new UnsupportedOperationException(); 
	}
	
	public UserDTO getByUsername(String username) {
		try {
			return userDAO.get(username); 
		}catch(Exception ex) {
			return null;
		}
	}
	
	public UserDTO getById(String id) {
		try {
			sessionTest(id);
			return userDAO.getById(id); 
		}catch(Exception ex) {
			return null;
		}
	}


	@Override
	public List<Session> getFor(String userId) {
		try {
			if(userDAO.get(userId)==null) {return new ArrayList<>();}
			String uId = userDAO.get(userId).getId();
			List<Session> target = super.getFor(uId);
			List<Session> result = new ArrayList<>(); 
			for(Session session: target) {
				String username = userDAO.getById(session.getUserId()).getUsername(); 
				result.add(new Session(username, session.getSessionId()));
			} 
			return result; 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}


	@Override
	public List<Session> getFor(String userId, int pageNo, int pageSize, String startFilter) {
		try {
			if(userDAO.get(userId)==null) return new ArrayList<>();
			String uId = userDAO.get(userId).getId();
			List<Session> sessions = super.getFor(uId, pageNo, pageSize, startFilter);
			List<Session> result = new ArrayList<>(); 
			for(Session session: sessions) {
				String username = userDAO.getById(session.getUserId()).getUsername(); 
				result.add(new Session(username, session.getSessionId()));
			}
			return result; 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}


	@Override
	public List<Session> list() {
		try {
			List<Session> sessions = super.list();
			List<Session> result = new ArrayList<>();
			for(Session session: sessions) {
				String username = userDAO.getById(session.getUserId()).getUsername(); 
				if(username==null) sessionTest(session.getUserId());
				else result.add(new Session(username, session.getSessionId()));
			}
			return result; 
		}catch(RuntimeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}


	@Override
	public List<Session> list(int pageNo, int pageSize, String startFilter) {
		try {
			List<Session> target =  super.list(pageNo, pageSize, startFilter);
			List<Session> result = new ArrayList<>();
			for(Session session: target) {
				String username = userDAO.get(session.getUserId()).getUsername(); 
				if(username==null) sessionTest(session.getSessionId());
				else result.add(new Session(username, session.getSessionId()));
			}
			return result; 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}


	@Override
	public List<Session> list(int pageNo, int pageSize, String sessionStartFilter, String userStartFilter) {
		try {
			if(pageNo<0) pageNo = 0; 
			if(pageSize<1) pageSize = 1; 
			if(sessionStartFilter==null) sessionStartFilter = ""; 
			if(userStartFilter==null) userStartFilter = "";
			if(pageNo==0) return new ArrayList<>(); pageNo--; 
			List<UserDTO> dtos = userDAO.select(userStartFilter);
			List<Session> result = new ArrayList<>(); 
			for(UserDTO dto: dtos) {
				if(!dto.getUsername().startsWith(userStartFilter)) continue; 
				List<Session> sessions = getFor(dto.getUsername());
				for(Session session: sessions) 
					if(session.getSessionId().startsWith(sessionStartFilter)) 
						result.add(session);
			}
			List<Session> target = result; 
			result = new ArrayList<>();
			int a = pageNo*pageSize; 
			int b = pageNo*pageSize+pageSize; 
			a = Math.min(a, target.size());
			b = Math.min(b, target.size());
			for(int i=a; i<b; i++)
				result.add(target.get(i));
			return result; 
		}catch(RuntimeException ex) {
			ex.printStackTrace();
			throw ex;
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}


	@Override
	public List<String> listUsers() {
		try {
			List<String> userIds = super.listUsers();
			List<String> users = new ArrayList<String>(); 
			for(String user: userIds) {
				if(super.getFor(user).size()>0)
					users.add(userDAO.getById(user).getUsername());
			}
			return users;
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(); 
		}
	}

	
	public List<String> listSessionsUserIds(){
		return super.listUsers(); 
	}
	
	public void logoutAllByUserId(String userId) {
		super.logoutAll(userId);
	}

	@Override
	public List<String> listUsers(int pageNo, int pageSize, String startFilter) {
		try {
			if(pageNo<0) pageNo =  0; 
			if(pageSize<1) pageSize = 1; 
			if(startFilter=="") startFilter = ""; 
			if(pageNo==0) return new ArrayList<>(); 
			pageNo--; 
			List<UserDTO> users = userDAO.select(startFilter);
			List<String> target = new ArrayList<>(); 
			List<String> result = new ArrayList<>();
			for(UserDTO dto: users)
				if(super.getFor(dto.getId()).size()>0)
					target.add(dto.getUsername());
			Collections.sort(target);
			int a = pageNo*pageSize;
			int b = pageNo*pageSize+pageSize; 
			a = Math.min(a, target.size()); 
			b = Math.min(b, target.size()); 
			for(int i=a; i<b; i++)
				result.add(target.get(i));
			return result; 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}



	@Override
	public void logoutAll(String userId) {
		try {
			UserDTO userDTO = userDAO.get(userId);
			super.logoutAll(userDTO.getId());
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}
	}


	@Override
	public Session get(String sessionId) {
		try {
			Session session = super.get(sessionId);
			String username = userDAO.getById(session.getUserId()).getUsername();
			return new Session(username, session.getSessionId());
		}catch(Exception ex) {
			return null;
		}
	}


	@Override
	public int countFor(String userId) {
		try {
			String username = userDAO.get(userId).getId();
			return super.countFor(username);
		}catch(Exception ex) {
			return 0; 
		}
	}
}
