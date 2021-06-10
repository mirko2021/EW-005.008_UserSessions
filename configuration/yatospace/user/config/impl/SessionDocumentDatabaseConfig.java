package yatospace.user.config.impl;

import java.io.File;

import yatospace.user.config.constants.FSConfigurationConstants;
import yatospace.user.config.constants.NamesConfigurationsConstants;
import yatospace.user.config.constants.RCConfigurationsConstants;
import yatospace.user.config.model.PropertyConfigModel;

/**
 * Конфигурације МОНГО базе података кад су у питању документно оријентисане базе података, 
 * за смјештање података, за сесије, као и њихова баратања. 
 * @author MV
 * @version 1.0
 */
public class SessionDocumentDatabaseConfig extends PropertyConfigModel{
	private static final long serialVersionUID = -5744761944669767101L;
	private static final String NAME_FILE = NamesConfigurationsConstants.SESSION_DOCUMENT_DATABASE_CONFIG+".properties"; 
	private static final String NAME_RESOURCE = NamesConfigurationsConstants.SESSION_DOCUMENT_DATABASE_CONFIG+".properties"; 
	private static final String FILE_DEST_DIR = FSConfigurationConstants.CONFIGURATIONS_FILESYSTEM_DIR_MAIN_LOCATION;
	private static final String RESOURCE_ADDR_PATH = RCConfigurationsConstants.CONFIGURATIONS_RESOURCE_PATH_ADDRESS_MAIN_LOCATION;
	public static final String FILE_PATH = FILE_DEST_DIR + File.separator + NAME_FILE; 
	public static final String RESOURCE_PATH = RESOURCE_ADDR_PATH + "/" + NAME_RESOURCE;
	
	private boolean initialized; 
	private boolean finalized; 
	
	public SessionDocumentDatabaseConfig() {
		super(new File(FILE_PATH), RESOURCE_PATH);
	}
	
	@Override
	public SessionDocumentDatabaseConfig init() {
		initialized = true;
		try{
			if(!getFile().exists()) {
				getFile().createNewFile();
				loadFromResource(); 
			}else load(); 
			store();
		}catch(Exception ex) {ex.printStackTrace();}
		return this;
	}

	@Override
	public SessionDocumentDatabaseConfig finish() {
		finalized = true; 
		return this;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}

	@Override
	public boolean isFinished() {
		return finalized;
	}	
	
	public String getHost() {
		String host = getConfigurations().getProperty("host"); 
		if(host==null) return "localhost"; 
		return host; 
	}
	
	public int getPort() {
		try {
			int port = Integer.parseInt(getConfigurations().getProperty("host"));
			return port;
		}catch(Exception ex) {
			return 27017; 
		}
	}
	
	public String getDatabase() {
		String database = getConfigurations().getProperty("database"); 
		if(database==null) return "yatospace_db"; 
		return database; 
	}
	
	public String getCollection() {
		String database = getConfigurations().getProperty("collection"); 
		if(database==null) return "yi_sessions"; 
		return database;
	}
}
