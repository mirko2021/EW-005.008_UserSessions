package yatospace.user.config.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * Модел о конфигурисања има методе за иницијализацију и финализацију.
 * Има могућности подразумјеване конфигурације и испис конфигурације у 
 * датотеку.  
 * @author MV
 * @version 1.0
 */
public abstract class PropertyConfigModel implements Serializable, AbstractConfigModel<Properties>, StoreableConfigmodel{
	private static final long serialVersionUID = -6407326505427714772L;
	private Properties configurations = new Properties(); 
	private String resource = ""; 
	private File file = null;
	
	public PropertyConfigModel(File destination, String resource) {this.file = destination; this.resource = resource;}
	public PropertyConfigModel(File destination) {this.file = destination;}
	public PropertyConfigModel(String resource) {this.resource = resource;}
	public PropertyConfigModel() {}
	
	public Properties getConfigurations() {
		return configurations;
	}
	public String getResource() {
		return resource;
	}
	public File getFile() {
		return file;
	}
	
	public abstract PropertyConfigModel init();
	public abstract PropertyConfigModel finish();
	public abstract boolean isInitialized(); 
	public abstract boolean isFinished(); 
	
	
	
	public void loadFromResource() {
		try (InputStream is = getClass().getResourceAsStream(resource)){
			configurations.load(is);
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void load() {
		try (FileInputStream fis = new FileInputStream(file)){
			configurations.load(fis);
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	public void store() {
		try (FileOutputStream fos = new FileOutputStream(file)){
			configurations.store(fos, "UTF-8");
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
