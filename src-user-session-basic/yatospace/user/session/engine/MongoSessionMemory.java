package yatospace.user.session.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import yatospace.user.session.model.Session;

/**
 * Меморија за баратање сесијама користећи МОНГО бату података. 
 * @author MV
 * @version 1.0 
 */
public class MongoSessionMemory implements SessionMemory{
	private MongoClient driver; 
	private MongoDatabase database; 
	private MongoCollection<Document> table;
	
	public MongoSessionMemory(String host, int port, String database, String collection) {
		if(host==null) throw new NullPointerException(); 
		if(database==null) throw new NullPointerException(); 
		if(collection==null) throw new NullPointerException(); 
		this.driver = new MongoClient(host, port);
		this.database = driver.getDatabase(database); 
		this.table = this.database.getCollection(collection); 
	}
	
	public MongoClient getDriver() {
		return driver;
	}

	public MongoDatabase getDatabase() {
		return database;
	}
	
	public MongoCollection<Document> getTable() {
		return table;
	}

	@Override
	public void login(String userId, String sessionId) { 
		Document document = new Document(); 
		document.append("user_id", userId);
		document.append("session_id", sessionId);
		if(table.find(document).first()==null) 
			table.insertOne(document);
	}

	@Override
	public void logout(String sessionId) {
		table.deleteOne(Filters.eq("session_id", sessionId)); 
	}

	@Override
	public void logoutAll(String userId) {
		table.deleteMany(Filters.eq("user_id", userId)); 
	}

	@Override
	public List<Session> getFor(String userId) {
		FindIterable<Document> documents = table.find(Filters.eq("user_id", userId)); 
		ArrayList<Session> result = new ArrayList<Session>();
		for(Document document: documents) {
			String sessionId = document.getString("session_id").toString();
			Session session = new Session(userId, sessionId);
			result.add(session); 
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public List<Session> getFor(String userId, int pageNo, int pageSize, String startFilter) {
		if(pageNo<0)   pageNo   = 0; 
		if(pageSize<1) pageSize = 1;
		if(startFilter==null) startFilter = ""; 
		if(pageNo==0) return new ArrayList<>(); 
		pageNo--;
		FindIterable<Document> documents = table.find(Filters.and(Filters.eq("user_id", userId), Filters.regex("session_id", Pattern.quote(startFilter)+".*")))
				.sort(Sorts.ascending("session_id")).skip(pageNo*pageSize).limit(pageSize); 
		ArrayList<Session> result = new ArrayList<Session>();
		for(Document document: documents) {
			String sessionId = document.getString("session_id").toString();
			Session session = new Session(userId, sessionId);
			result.add(session); 
		}
		return result;
	}
	

	@Override
	public Session get(String sessionId) {
		Document document = table.find(Filters.eq("session_id", sessionId)).first(); 
		if(document==null) return null; 
		Session session = new Session(document.getString("user_id"), document.getString("session_id"));
		return session; 
	}

	@Override
	public int count() {
		return (int) table.countDocuments();
	}

	@Override
	public int countUsers() {
		return listUsers().size();
	}

	@Override
	public int countFor(String userId) {
		return (int) table.countDocuments(Filters.eq("user_id", userId));
	}

	@Override
	public List<Session> list() {
		ArrayList<Session> result = new ArrayList<>();
		ArrayList<Document> list = table.find().into(new ArrayList<>());
		for(Document doc: list) 
			result.add(new Session(doc.getString("user_id"), doc.getString("session_id"))); 
		Collections.sort(result); 
		return result;
	}

	@Override
	public List<Session> list(int pageNo, int pageSize, String startFilter) {
		if(pageSize<1) pageSize = 1; 
		if(pageNo<0) pageNo = 0; 
		if(startFilter == null) startFilter = ""; 
		if(pageNo==0) return new ArrayList<>(); 
		ArrayList<Session> result = new ArrayList<>();
		ArrayList<Document> list = table.find(Filters.regex("session_id", Pattern.quote(startFilter)+".*"))
				.sort(Sorts.ascending("session_id")).skip(pageNo*pageSize).limit(pageSize).into(new ArrayList<>());
		for(Document doc: list) 
			result.add(new Session(doc.getString("user_id"), doc.getString("session_id"))); 
		return result;
	}

	@Override
	public List<Session> list(int pageNo, int pageSize, String sessionStartFilter, String userStartFilter) {
		if(pageSize<1) pageSize = 1; 
		if(pageNo<0) pageNo = 0; 
		if(sessionStartFilter == null) sessionStartFilter = ""; 
		if(userStartFilter== null) userStartFilter = ""; 
		if(pageNo==0) return new ArrayList<>(); 
		pageNo--; 
		ArrayList<Session> result = new ArrayList<>();
		ArrayList<Document> list = table.find(Filters.and(Filters.regex("session_id", Pattern.quote(sessionStartFilter)+".*"),Filters.regex("user_id", Pattern.quote(userStartFilter)+".*")))
				                   .sort(Sorts.ascending("session_id")).skip(pageNo*pageSize).limit(pageSize).into(new ArrayList<>());
		for(Document doc: list) 
			result.add(new Session(doc.getString("user_id"), doc.getString("session_id"))); 
		return result;
	}

	@Override
	public List<String> listUsers() {
		ArrayList<Document> documents = table.find().into(new ArrayList<>()); 
		ArrayList<String> users = new ArrayList<>();
		for(Document document: documents) 
			if(!users.contains(document.getString("user_id")))
				users.add(document.getString("user_id"));
		Collections.sort(users);
		return users;
	}

	@Override
	public List<String> listUsers(int pageNo, int pageSize, String startFilter) {
		if(pageSize<1) pageSize = 1; 
		if(pageNo<0) pageNo = 0; 
		if(startFilter == null) startFilter = ""; 
		if(pageNo==0) return new ArrayList<>(); 
		pageNo--; 
		ArrayList<String> result = new ArrayList<String>(); 
		ArrayList<Document> list = table.find(Filters.and(Filters.regex("session_id", Pattern.quote(startFilter)+".*")))
				.skip(pageNo*pageSize).limit(pageSize).sort(Sorts.ascending("user_id")).into(new ArrayList<>());
		for(Document doc: list) 
			if(!result.contains(doc.getString("user_id")))
				result.add(doc.getString("user_id"));  
		return result; 
	}
	
}
