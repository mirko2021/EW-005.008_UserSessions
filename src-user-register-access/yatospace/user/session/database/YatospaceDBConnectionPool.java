package yatospace.user.session.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import yatospace.user.config.impl.UserRegisterDatabaseConfig;

public class YatospaceDBConnectionPool {
  public UserRegisterDatabaseConfig configs; 
  
  public static final String DATABASE = "yatospace_db"; 
  public static final String HOST = "localhost"; 
  public static final String PORT = "3306"; 
  public static final String USER = "root";
  public static final String PASSWORD = "root"; 
  public static final String DRIVER = "com.mysql.cj.jdbc.Driver"; 
  public static final int PRECONNECT_COUNT = 0;
  public static final int MAX_IDLE_CONNECTIONS = 10; 
  public static final int MAX_CONNECTIONS = 10; 
  
  public static final boolean  ERROR_REMIX = true;
  
  private static boolean driver_loaded = false; 
  
  public static YatospaceDBConnectionPool getConnectionPool() {
      if(!driver_loaded) {
    	    String driver = DRIVER;
		    try {
		        Class.forName(driver);
		        driver_loaded = true;
		    } catch (Exception ex) {
		    	if(ERROR_REMIX) ex.printStackTrace();
		    }
      }
	  if(connectionPool==null) {
    	  try {
    		    String jdbcURL = "jdbc:mysql://"+HOST+":"+PORT+"/"+DATABASE+"?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&characterSetResults=utf8&connectionCollation=utf8_general_ci";
    		    String username = USER;
    		    String password = PASSWORD;
    		    String database = DATABASE; 
    		    int preconnectCount = PRECONNECT_COUNT;
    		    int maxIdleConnections = MAX_IDLE_CONNECTIONS;
    		    int maxConnections = MAX_CONNECTIONS;
    	        connectionPool = new YatospaceDBConnectionPool(
    	        jdbcURL, username, password,
    	        preconnectCount, maxIdleConnections,
    	        maxConnections, database);
    	    } catch (Exception ex) {
    	       if(ERROR_REMIX) ex.printStackTrace();
    	    }
      }
	  return connectionPool;
  }
  
  
  public static YatospaceDBConnectionPool getConnectionPool(UserRegisterDatabaseConfig configs) {
      if(configs==null) return getConnectionPool(); 
	  if(!driver_loaded) {
		    String driver = configs.getDBDriver();
		    try {
		        Class.forName(driver);
		        driver_loaded = true;
		    } catch (Exception ex) {
		    	if(ERROR_REMIX) ex.printStackTrace();
		    }
	  }
      if(connectionPool==null) {
    	  try {
    		  	String username = configs.getDBUser();
  		    	String password = configs.getDBPassword();
  		    	String database = configs.getDBDatabase(); 
  		    	int preconnectCount = configs.getPreconnectCount();
  		    	int maxIdleConnections = configs.getMaximalConnections();
  		    	int maxConnections = configs.getMaximalConnections();
    		    String jdbcURL = "jdbc:mysql://"+configs.getHost()+":"+configs.getPort()+"/"+configs.getDBDatabase()+"?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&characterSetResults=utf8&connectionCollation=utf8_general_ci";
    	        connectionPool = new YatospaceDBConnectionPool(
    	        jdbcURL, username, password,
    	        preconnectCount, maxIdleConnections,
    	        maxConnections, database);
    	        connectionPool.configs = configs; 
    	    } catch (Exception ex) {
    	       if(ERROR_REMIX) ex.printStackTrace();
    	    }
      }
	  return connectionPool;
  }
  
  private static YatospaceDBConnectionPool connectionPool;

  
  protected YatospaceDBConnectionPool(String aJdbcURL, String aUsername,
    String aPassword, int aPreconnectCount,
    int aMaxIdleConnections,
    int aMaxConnections, String aDatabase)
    throws ClassNotFoundException, SQLException {

    freeConnections = new Vector<Connection>();
    usedConnections = new Vector<Connection>();
    jdbcURL = aJdbcURL;
    username = aUsername;
    password = aPassword;
    database = aDatabase; 
    preconnectCount = aPreconnectCount;
    maxIdleConnections = aMaxIdleConnections;
    maxConnections = aMaxConnections;

    for (int i = 0; i < preconnectCount; i++) {
      Connection conn = DriverManager.getConnection(
        jdbcURL, username, password);
      conn.setAutoCommit(true);
      freeConnections.addElement(conn);
    }
    connectCount = preconnectCount;
  }

  public synchronized Connection checkOut()
    throws SQLException {

    Connection conn = null;
    if (freeConnections.size() > 0) {
      conn = (Connection)freeConnections.elementAt(0);
      freeConnections.removeElementAt(0);
      usedConnections.addElement(conn);
    } else {
      if (connectCount < maxConnections) {
        conn = DriverManager.getConnection(
          jdbcURL, username, password);
        usedConnections.addElement(conn);
        connectCount++;
      } else {
        try {
          wait();
          conn = (Connection)freeConnections.elementAt(0);
          freeConnections.removeElementAt(0);
          usedConnections.addElement(conn);
        } catch (InterruptedException ex) {
          if(ERROR_REMIX) ex.printStackTrace();
        }
      }
    }
    return conn;
  }

  public synchronized void checkIn(Connection aConn) {
    if (aConn ==  null)
      return;
    if (usedConnections.removeElement(aConn)) {
      freeConnections.addElement(aConn);
      while (freeConnections.size() > maxIdleConnections) {
        int lastOne = freeConnections.size() - 1;
        Connection conn = (Connection)
          freeConnections.elementAt(lastOne);
        try { conn.close(); } catch (SQLException ex) { }
        freeConnections.removeElementAt(lastOne);
      }
      notify();
    }
  }

  private String jdbcURL;
  private String username;
  private String password;
  private String database; 
  private int preconnectCount;
  private int connectCount;
  private int maxIdleConnections;
  private int maxConnections;
  private Vector<Connection> usedConnections;
  private Vector<Connection> freeConnections;

  public UserRegisterDatabaseConfig getConfigs() {
	  return configs;
  }

  public String getJdbcURL() {
	 return jdbcURL;
  }

  public String getUsername() {
	  return username;
  }

  public String getPassword() {
	  return password;
  }

  public String getDatabase() {
	  return database;
  }

  public int getPreconnectCount() {
	  return preconnectCount;
  }

  public int getMaxIdleConnections() {
	  return maxIdleConnections;
  }

  public int getMaxConnections() {
	  return maxConnections;
  }

}
