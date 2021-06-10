package yatospace.user.config.model;

/**
 * Опште операције за конфигурисање. 
 * @author MV
 * @version 1.0
 * @param EM - Engine Memory
 */
public interface AbstractConfigModel<EM> {
	public abstract PropertyConfigModel init();
	public abstract PropertyConfigModel finish();
	public abstract boolean isInitialized(); 
	public abstract boolean isFinished(); 
	public abstract EM getConfigurations(); 
}
