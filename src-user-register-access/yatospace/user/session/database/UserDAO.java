package yatospace.user.session.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Преузимања података по корисничком имену, укључив и непромјењиву идентификацију. 
 * @author MV
 * @version 1.0 
 */
public class UserDAO {
	private YatospaceDBConnectionPool connectionPool; 
	public static final String SQL_GET_USER       = "SELECT id_user, username, passwordcode FROM "+YatospaceDBConnectionPool.DATABASE+".yi_users WHERE username=?"; 
	public static final String SQL_GET_USER_BY_ID = "SELECT id_user, username, passwordcode FROM "+YatospaceDBConnectionPool.DATABASE+".yi_users WHERE id_user=?";
	public static final String SQL_LIST_USERS     = "SELECT id_user, username, passwordcode FROM "+YatospaceDBConnectionPool.DATABASE+".yi_users WHERE username LIKE ?"; 
	
	public UserDAO(YatospaceDBConnectionPool connectionPool) {
		if(connectionPool==null) throw new NullPointerException(); 
		this.connectionPool = connectionPool; 
	}

	public YatospaceDBConnectionPool getConnectionPool() {
		return connectionPool;
	}
	
	public UserDTO get(String username) throws SQLException {
		if(username==null) return null; 
		Connection connection = connectionPool.checkOut();
		try (PreparedStatement statement = connection.prepareStatement(SQL_GET_USER)){
			UserDTO user = null; 
			statement.setString(1, username);
			try(ResultSet result = statement.executeQuery()){
				while(result.next()) {
					String id = result.getString("id_user");
					String userId = result.getString("username"); 
					String passwordRecord = result.getString("passwordcode");
					user = new UserDTO(); 
					user.setId(id);
					user.setUsername(userId);
					user.setPasswordRecored(passwordRecord);
				}
			}
			return user; 
		}catch(RuntimeException ex) {
			throw ex; 
		}catch(Exception ex) {
			throw new RuntimeException(ex); 
		}finally {
			connectionPool.checkIn(connection);
		}
	}
	
	public UserDTO getById(String id) throws SQLException {
		if(id==null) return null; 
		Connection connection = connectionPool.checkOut();
		try (PreparedStatement statement = connection.prepareStatement(SQL_GET_USER_BY_ID)){
			UserDTO user = null; 
			statement.setString(1, id);
			try(ResultSet result = statement.executeQuery()){
				while(result.next()) {
					String fixId = result.getString("id_user");
					String userId = result.getString("username");
					String passwordRecord = result.getString("passwordcode");
					user = new UserDTO();
					user.setId(fixId);
					user.setUsername(userId);
					user.setPasswordRecored(passwordRecord);
				}
			}
			return user; 
		}catch(RuntimeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}finally {
			connectionPool.checkIn(connection);
		}
	}
	
	public List<UserDTO> select(String userFilter) throws SQLException{
		if(userFilter==null) userFilter = "";
		ArrayList<UserDTO> users = new ArrayList<>();
		Connection connection = connectionPool.checkOut();
		try (PreparedStatement statement = connection.prepareStatement(SQL_LIST_USERS)){
			UserDTO user = null;
			statement.setString(1, userFilter.replaceAll("%", "").replaceAll("_", "")+"%");
			try(ResultSet result = statement.executeQuery()){
				while(result.next()) {
					String fixId = result.getString("id_user");
					String userId = result.getString("username");
					String passwordRecord = result.getString("passwordcode");
					user = new UserDTO();
					user.setId(fixId);
					user.setUsername(userId);
					user.setPasswordRecored(passwordRecord);
					users.add(user); 
				}
			}
			return users; 
		}catch(RuntimeException ex) {
			throw ex;
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}finally {
			connectionPool.checkIn(connection);
		}
	}
}
