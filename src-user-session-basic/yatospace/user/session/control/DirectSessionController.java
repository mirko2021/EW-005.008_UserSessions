package yatospace.user.session.control;

import yatospace.user.session.engine.DirectSessionMemory;

/**
 * Дирктна контрола за сесије. Више има спецификационо тестну улогу. 
 * У објекту програмске меморије се отварају, прегледају и затварају. 
 * @author MV
 * @version 1.0
 */
public class DirectSessionController extends SessionController{
	private DirectSessionMemory sessionMemory = new DirectSessionMemory(); 
	
	@Override
	public DirectSessionMemory getMemoryForSession() {
		return sessionMemory;
	}
}
