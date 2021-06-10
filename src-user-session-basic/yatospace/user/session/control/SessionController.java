package yatospace.user.session.control;

import yatospace.user.session.engine.SessionMemory;

/**
 * Општи облик контролера за сесије. 
 * @author MV
 * @version 1.0 
 */
public abstract class SessionController {
	public abstract SessionMemory getMemoryForSession();
}
