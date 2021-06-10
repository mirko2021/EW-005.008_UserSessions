package yatospace.user.session.application;

import yatospace.user.config.center.SessionDocumentConfigCenter;
import yatospace.user.config.center.UserRegistrationConfigCenter;
import yatospace.user.config.engine.SessionDocumentConfigEngine;
import yatospace.user.config.engine.UserRegistrationConfigEngine;
import yatospace.user.config.impl.SessionDocumentDatabaseConfig;
import yatospace.user.config.impl.UserRegisterDatabaseConfig;
import yatospace.user.session.controller.MongoRegisteredSessionController;
import yatospace.user.session.database.UserDAO;
import yatospace.user.session.database.YatospaceDBConnectionPool;
import yatospace.user.session.engine.MongoRegisteredSessionEngine;
import yatospace.user.session.util.SessionsGarbageCollector;

/**
 * Контролне тачке за апликативности. 
 * @author MV
 * @version 1.0
 */
public class GeneralUserSessionControlPoint implements AutoCloseable{
	private final UserRegistrationConfigEngine configurationEngine = UserRegistrationConfigCenter.cofigurationsEngine; 
	private final YatospaceDBConnectionPool connectionPool = YatospaceDBConnectionPool.getConnectionPool(configurationEngine.getDatabaseConfigurations());
	private final UserRegisterDatabaseConfig registerConfig = configurationEngine.getDatabaseConfigurations(); 
	private final UserDAO userDAO = new UserDAO(connectionPool); 
	private final SessionDocumentConfigEngine configEngine = SessionDocumentConfigCenter.cofigurationsEngine;
	private final SessionDocumentDatabaseConfig config = configEngine.getDatabaseConfigurations();
	private final SessionsGarbageCollector sessionGC = new SessionsGarbageCollector(); 
	private final MongoRegisteredSessionController sessionController = new MongoRegisteredSessionController(new MongoRegisteredSessionEngine(config.getHost(), config.getPort(), config.getDatabase(), config.getCollection(), userDAO, sessionGC));
	
	public UserRegistrationConfigEngine getConfigurationEngine() {
		return configurationEngine;
	}
	public YatospaceDBConnectionPool getConnectionPool() {
		return connectionPool;
	}
	public UserRegisterDatabaseConfig getRegisterConfig() {
		return registerConfig;
	}
	public UserDAO getUserDAO() {
		return userDAO;
	}
	public SessionDocumentConfigEngine getConfigEngine() {
		return configEngine;
	}
	public SessionDocumentDatabaseConfig getConfig() {
		return config;
	}
	public MongoRegisteredSessionController getSessionController() {
		return sessionController;
	}
	public SessionsGarbageCollector getSessionGC() {
		return sessionGC;
	}
	
	{sessionGC.start();}

	@Override
	public void close() throws Exception {
		sessionGC.close(); 
	}
}
