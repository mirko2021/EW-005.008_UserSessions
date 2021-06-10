package yatospace.user.config.center;

import java.io.File;

import yatospace.user.config.constants.FSConfigurationConstants;
import yatospace.user.config.engine.SessionDocumentConfigEngine;

/**
 * Документ оријентисано меморисање и управљање сесијама. 
 * @author MV
 * @version 1.0
 */
public class SessionDocumentConfigCenter {
	public static final SessionDocumentConfigEngine  cofigurationsEngine = new SessionDocumentConfigEngine(); 
	private static final String FILE_DEST_DIR = FSConfigurationConstants.CONFIGURATIONS_FILESYSTEM_DIR_MAIN_LOCATION;
	
	static {if(!new File(FILE_DEST_DIR).exists()) new File(FILE_DEST_DIR).mkdirs();}
	static {cofigurationsEngine.initialize();}
}
