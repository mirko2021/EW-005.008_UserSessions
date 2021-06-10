package yatospace.user.config.model;

/**
 * Могућности отварања и затварања, односно чувања.
 * @author MV
 * @version 1.0
 */
public interface SaveableConfigModel extends AutoCloseable{
	public void open(); 
	public void save(); 
	public void close(); 
}
