package yatospace.user.session.util;

import java.util.ArrayList;
import java.util.List;

import yatospace.user.session.database.UserDAO;
import yatospace.user.session.engine.MongoRegisteredSessionEngine;

/**
 * Мост за чишћење и одучивање за раскорак међу базама података. 
 * На примјер, ако корисник није више у регистру, али сесије за 
 * њег постоје. При захтхјеву за неку операцију укидају се.
 * @author MV
 * @version 1.0 
 */
public class MongoRegisterSessionsUtilitizer {
	private UserDAO userDAO;
	private MongoRegisteredSessionEngine sessionEngine;
	private List<String> usersIds = new ArrayList<>(); 
	private List<Runnable> usersRnb = new ArrayList<>(); 
	
	public MongoRegisterSessionsUtilitizer(UserDAO userDAO, MongoRegisteredSessionEngine sessionEngine) {
		if(userDAO == null) throw new RuntimeException();
		if(sessionEngine == null) throw new RuntimeException(); 
		this.userDAO = userDAO;
		this.sessionEngine = sessionEngine;
		initialize();
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	public List<String> getUsersIds() {
		return usersIds;
	}

	public void setUsersIds(List<String> usersIds) {
		this.usersIds = usersIds;
	}
	
	public List<Runnable> getUsersRnb() {
		return usersRnb;
	}

	public void setUsersRnb(List<Runnable> usersRnb) {
		this.usersRnb = usersRnb;
	}

	public MongoRegisteredSessionEngine getSessionEngine() {
		return sessionEngine;
	}
	
	public void ifNotLogout(String userId) {
		if(sessionEngine.getById(userId)==null) sessionEngine.logoutAllByUserId(userId);
	}
	
	public void initialize() {
		usersIds = sessionEngine.listSessionsUserIds();
		for(String userId: usersIds)
			usersRnb.add(()->ifNotLogout(userId));
	}
}
