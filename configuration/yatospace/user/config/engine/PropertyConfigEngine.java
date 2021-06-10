package yatospace.user.config.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yatospace.user.config.model.PropertyConfigModel;

/**
 * Контролна алатка за извршење груписаних и изабраних кофнигурација. 
 * @author MV
 * @version 1.0
 */
public class PropertyConfigEngine {
	private HashMap<String, PropertyConfigModel> configurationsRegister =  new HashMap<>();
	
	public List<String> listConfigurationsId(){
		ArrayList<String> idList = new ArrayList<>(configurationsRegister.keySet());
		return idList; 
	}
	
	public PropertyConfigModel getConfiguration(String id) {
		return configurationsRegister.get(id); 
	}
	
	public PropertyConfigModel getAndIfSetConfiguration(String id, PropertyConfigModel configUnit) {
		if(id==null) throw new NullPointerException(); 
		if(configUnit==null) throw new NullPointerException();
		PropertyConfigModel configuration = getConfiguration(id);
		if(configuration==null) configurationsRegister.put(id, configuration=configUnit); 
		return configuration; 
	}
	
	public PropertyConfigModel putConfiguration(String id, PropertyConfigModel configUnit) {
		if(id==null) throw new NullPointerException(); 
		if(configUnit==null) throw new NullPointerException();
		configurationsRegister.put(id, configUnit); 
		return configUnit; 
	} 
	
	public boolean initialized; 
	public boolean finalized; 
	
	public void initAction() {
		for(PropertyConfigModel model: configurationsRegister.values())
			model.init(); 
	}
	
	public void finishAction() {
		for(PropertyConfigModel model: configurationsRegister.values())
			model.finish(); 
	}
	
	public final void initialize() {
		if(initialized) return; 
		initAction();
		initialized = true;
	}
	
	public final void finish() {
		if(finalized) return; 
			finishAction();
		finalized = true;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public boolean isFinalized() {
		return finalized;
	}

	public boolean isFinished() {
		return finalized; 
	}
}
