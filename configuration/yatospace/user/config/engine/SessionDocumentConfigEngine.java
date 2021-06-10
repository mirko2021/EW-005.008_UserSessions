package yatospace.user.config.engine;

import yatospace.user.config.constants.NamesConfigurationsConstants;
import yatospace.user.config.impl.SessionDocumentDatabaseConfig;

/**
 * Баратање с конфигурацијама за документно оријентисане базе у улози управљања сесијама.
 * @author MV
 * @version 1.0
 */
public class SessionDocumentConfigEngine extends PropertyConfigEngine{
	private static final String NAME_ID = NamesConfigurationsConstants.SESSION_DOCUMENT_DATABASE_CONFIG; 
	
	public SessionDocumentConfigEngine() {
		putConfiguration(NAME_ID, new SessionDocumentDatabaseConfig()); 
	}
	
	public SessionDocumentDatabaseConfig getDatabaseConfigurations() {
		return (SessionDocumentDatabaseConfig) this.getConfiguration(NAME_ID); 
	}
}
