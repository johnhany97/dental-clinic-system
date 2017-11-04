/**
 * Database class
 * 
 * This is used to connect to the database
 * and to close connection to the database
 * @author John Ayad
 */
package com2002.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

	/** Constant to represent db username **/
	final private static String userName = "team035";

	/** Constant to represent db password **/
	final private static String password = "84680a5d";

	/** Constant to represent db server name **/
	final private static String serverName = "stusql.dcs.shef.ac.uk";

	/** Constant to represent db name **/
	final private static String dbName = "team035";

	/**
	 * getConnection Static method
	 * 
	 * This function is used to get a connection to the database
	 * 
	 * @return Connection to the db
	 * @throws SQLException if an error happened while attempting connection
	 */
	public static Connection getConnection() throws SQLException {
		//Initialize variables
	    Connection conn;
	    //Connection properties (username, password)
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", userName);
	    connectionProps.put("password", password);
	    //Attempt connection
        conn = DriverManager.getConnection(
                   "jdbc:mysql://" + serverName + "/" + dbName,
                   connectionProps);

	    return conn; //if successful
	}
	
	/**
	 * closeDb Static method
	 * 
	 * This function is used to close a connection given said connection
	 * 
	 * @param conn Connection to the db that is to be closed
	 */
	public static void closeDb(Connection conn) {
	    try { conn.close(); } catch (Exception e) {}
	}
}