package yatospace.user.session.control;

import redis.clients.jedis.Jedis;
import yatospace.user.session.engine.RedisSessionMemory;

/**
 * Функционална контрола за РЕДИС базе података као меморије сесија и контрола тих сесија. 
 * @author MV
 * @version 1.0
 */
public class RedisSessionController extends SessionController{
	private RedisSessionMemory sessionMemory;

	public RedisSessionController(String memoryHost) {
		if(memoryHost==null) throw new NullPointerException(); 
		sessionMemory = new RedisSessionMemory(new Jedis(memoryHost));
	}
	
	public RedisSessionMemory getSessionMemory() {
		return sessionMemory;
	}

	@Override
	public RedisSessionMemory getMemoryForSession() {
		return sessionMemory;
	}
}
