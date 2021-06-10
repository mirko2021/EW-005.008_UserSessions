package yatospace.user.session.util;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Општи скупљач сесија, које су изгубиле регистрацију за корисника. 
 * @author MV
 * @version 1.0
 */
public class SessionsGarbageCollector extends Thread implements Runnable, AutoCloseable{
	private ArrayDeque<Runnable> gcTasks = new ArrayDeque<>();
	private MongoRegisterSessionsUtilitizer utilitizer; 
	private Object gcSynchron = new Object(); 
	private boolean closed;
	
	public MongoRegisterSessionsUtilitizer getUtilitizer() {
		return utilitizer;
	}

	public void setUtilitizer(MongoRegisterSessionsUtilitizer utilitizer) {
		this.utilitizer = utilitizer;
		for(Runnable x: utilitizer.getUsersRnb()) 
			push(x);
	}

	public SessionsGarbageCollector() {}
	
	public ArrayDeque<Runnable> getGcTasks() {
		return gcTasks;
	}

	public Object getGcSynchron() {
		return gcSynchron;
	}

	public boolean isClosed() {
		return closed;
	}
	
	public synchronized void push(Runnable task) {
		if(task!=null) gcTasks.add(task); 
		synchronized(gcSynchron) {gcSynchron.notify();}
	}
	
	public synchronized Runnable pop() {
		return gcTasks.pop();
	}
	
	public void run() {
		System.out.println("GARBADGE COLLECTOR FOR SESSIONS RUN.");
		while(true) {
			Runnable action = ()->{};
			if(gcTasks.size()!=0) {
				synchronized(this) {action = gcTasks.pop();}
				if(action!=null) 
					try{
						action.run();
					}catch(Exception ex) {
						ex.printStackTrace();
					}
			}else {
				synchronized(gcSynchron) {
					try{gcSynchron.wait();} 
					catch(Exception ex) {throw new RuntimeException(ex);}
				}
			}
			if(closed) break;
		}
		System.out.println("GARBADGE COLLECTOR FOR SESSIONS STOP.");
	}

	@Override
	public synchronized void close() throws Exception {
		synchronized(gcSynchron) {
			gcSynchron.notify();
			closed = true; 
		}
	}
	
	public synchronized ArrayList<Runnable> get() {
		ArrayList<Runnable> result = new ArrayList<>(gcTasks); 
		return result; 
	}
}
