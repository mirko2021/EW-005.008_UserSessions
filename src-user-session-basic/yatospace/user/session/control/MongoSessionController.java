package yatospace.user.session.control;

import yatospace.user.session.engine.MongoSessionMemory;

/**
 * Контролер сесија преко монго базе података. 
 * @author MV
 * @version 1.0
 */
public class MongoSessionController extends SessionController{
	private MongoSessionMemory sessionMemory; 
	
	public MongoSessionController(String host, int port, String database, String collection) {
		sessionMemory = new MongoSessionMemory(host, port, database, collection);
	}
	
	@Override
	public MongoSessionMemory getMemoryForSession() {
		return sessionMemory;
	}

}
