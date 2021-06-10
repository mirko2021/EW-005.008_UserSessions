package yatospace.user.config.engine;

import yatospace.user.config.constants.NamesConfigurationsConstants;
import yatospace.user.config.impl.SessionMemoryDatabaseConfig;

/**
 * Машина за конфигурације меморије сесија. 
 * @author MV
 * @version 1.0 
 */
public class SessionMemoryConfigEngine extends PropertyConfigEngine{
	private static final String NAME_ID = NamesConfigurationsConstants.SESSION_MEMORY_DATABASE_CONFIG; 
	
	public SessionMemoryConfigEngine() {
		putConfiguration(NAME_ID, new SessionMemoryDatabaseConfig()); 
	}
	
	public SessionMemoryDatabaseConfig getDatabaseConfigurations() {
		return (SessionMemoryDatabaseConfig) this.getConfiguration(NAME_ID); 
	}
}
