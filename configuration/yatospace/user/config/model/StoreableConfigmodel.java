package yatospace.user.config.model;

/**
 * Функционалности записа у ток или датотеку, односно ресурс. 
 * @author MV
 * @version 1.0
 */
public interface StoreableConfigmodel {
	public void loadFromResource();
	public void load(); 
	public void store(); 
}
