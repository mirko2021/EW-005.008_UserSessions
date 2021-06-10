package yatospace.user.config.impl;

import java.io.File;

import yatospace.user.config.constants.FSConfigurationConstants;
import yatospace.user.config.constants.NamesConfigurationsConstants;
import yatospace.user.config.constants.RCConfigurationsConstants;
import yatospace.user.config.model.PropertyConfigModel;

/**
 * Конфигурације везане за параметре базе података. 
 * @author MV
 * @version 1.0
 */
public class UserRegisterDatabaseConfig extends PropertyConfigModel{
	private static final long serialVersionUID = 6081145746022773621L;
	private static final String NAME_FILE = NamesConfigurationsConstants.USER_REGISTER_DATABASE_CONFIG+".properties"; 
	private static final String NAME_RESOURCE = NamesConfigurationsConstants.USER_REGISTER_DATABASE_CONFIG+".properties"; 
	private static final String FILE_DEST_DIR = FSConfigurationConstants.CONFIGURATIONS_FILESYSTEM_DIR_MAIN_LOCATION;
	private static final String RESOURCE_ADDR_PATH = RCConfigurationsConstants.CONFIGURATIONS_RESOURCE_PATH_ADDRESS_MAIN_LOCATION;
	public static final String FILE_PATH = FILE_DEST_DIR + File.separator + NAME_FILE; 
	public static final String RESOURCE_PATH = RESOURCE_ADDR_PATH + "/" + NAME_RESOURCE; 
	
	private boolean initialized; 
	private boolean finalized; 
	
	public UserRegisterDatabaseConfig() {
		super(new File(FILE_PATH), RESOURCE_PATH);
	}
	
	@Override
	public UserRegisterDatabaseConfig init() {
		if(initialized) return this; 
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
	public UserRegisterDatabaseConfig finish() {
		if(finalized) return this; 
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
			return Integer.parseInt(getConfigurations().getProperty("port")); 
		}catch(Exception ex) {
			return 3306; 
		}
	}
	
	public String getDBUser() {
		String user = getConfigurations().getProperty("user"); 
		if(user==null) return user;
		return user; 
	}
	
	public String getDBPassword() {
		String password = getConfigurations().getProperty("password"); 
		if(password==null) return password;
		return password; 
	}
	
	public String getDBDatabase() {
		String database = getConfigurations().getProperty("database"); 
		if(database==null) return database;
		return database; 
	}
	
	public String getDBDriver() {
		String driver = getConfigurations().getProperty("driver"); 
		if(driver==null) return driver;
		return driver; 
	}
	
	
	public int getPreconnectCount() {
		try {
			return Integer.parseInt(getConfigurations().getProperty("precconect_count")); 
		}catch(Exception ex) {
			return 0; 
		}
	}
	
	public int getMaximalIdleConnections() {
		try {
			return Integer.parseInt(getConfigurations().getProperty("max_idle_connections")); 
		}catch(Exception ex) {
			return 10; 
		}
	}
	
	public int getMaximalConnections() {
		try {
			return Integer.parseInt(getConfigurations().getProperty("max_connections")); 
		}catch(Exception ex) {
			return 10; 
		}
	}
}
