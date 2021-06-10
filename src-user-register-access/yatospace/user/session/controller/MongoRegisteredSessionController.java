package yatospace.user.session.controller;

import yatospace.user.session.control.SessionController;
import yatospace.user.session.engine.MongoRegisteredSessionEngine;
import yatospace.user.session.util.SessionIdGeneratorEngine;

/**
 * Контрола кад су у питању логовање у сесију, као и баратање информацијама, 
 * о актуелној сесији. 
 * @author MV
 * @version 1.0
 */
public class MongoRegisteredSessionController extends SessionController{
	private SessionIdGeneratorEngine sessionIdGeneratorEngine;
	private MongoRegisteredSessionEngine registerEngine;
	
	
	public MongoRegisteredSessionController(MongoRegisteredSessionEngine registerEngine) {
		if(registerEngine==null) throw new NullPointerException(); 
		this.sessionIdGeneratorEngine = new SessionIdGeneratorEngine(); 
		this.registerEngine = registerEngine; 
	}
	
	public SessionIdGeneratorEngine getSessionIdGeneratorEngine() {
		return sessionIdGeneratorEngine;
	}
	public MongoRegisteredSessionEngine getRegisterEngine() {
		return registerEngine;
	}
	
	public boolean login(String username, String password) {
		String sessionId = sessionIdGeneratorEngine.generateNewCode(); 
		while(registerEngine.get(sessionId)!=null) sessionId = sessionIdGeneratorEngine.generateNewCode();
		return registerEngine.login(username, password, sessionId);  
	}
	
	public boolean login(String username, String password, String sessionId) {
		return registerEngine.login(username, password, sessionId);
	}

	@Override
	public MongoRegisteredSessionEngine getMemoryForSession() {
		return registerEngine;
	}
}
