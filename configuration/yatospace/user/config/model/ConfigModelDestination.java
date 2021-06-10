package yatospace.user.config.model;

import java.io.File;
import java.util.Properties;

/**
 * Мјеста складиштења конфигурација.
 * @author MV
 * @version 1.0
 */
public interface ConfigModelDestination {
	public Properties getConfigurations(); 
	public abstract String getResource(); 
	public abstract File getFile(); 
	
	public boolean hasResource();
	public boolean hasDestination();	
	public boolean hasFile(); 
}
