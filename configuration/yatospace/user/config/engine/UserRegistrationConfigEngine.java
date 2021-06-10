package yatospace.user.config.engine;


import yatospace.user.config.constants.NamesConfigurationsConstants;
import yatospace.user.config.impl.UserRegisterDatabaseConfig;

/**
 * Конфигурације које су везане за регистровање корисника. 
 * @author MV
 * @version 1.0
 */
public class UserRegistrationConfigEngine extends PropertyConfigEngine{
	private static final String NAME_ID = NamesConfigurationsConstants.USER_REGISTER_DATABASE_CONFIG; 
	
	public UserRegistrationConfigEngine() {
		putConfiguration(NAME_ID, new UserRegisterDatabaseConfig()); 
	}
	
	public UserRegisterDatabaseConfig getDatabaseConfigurations() {
		return (UserRegisterDatabaseConfig) this.getConfiguration(NAME_ID); 
	}
}
