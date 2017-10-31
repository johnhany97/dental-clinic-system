package com2002.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

	final private static String userName = "team035";
	final private static String password = "84680a5d";
	final private static String serverName = "stusql.dcs.shef.ac.uk";
	final private static String dbName = "team035";
	
	public static Connection getConnection() throws SQLException {
	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", userName);
	    connectionProps.put("password", password);
	    
        conn = DriverManager.getConnection(
                   "jdbc:mysql://" + serverName + "/" + dbName,
                   connectionProps);

	    return conn;
	}
	
	public static void closeDb(Connection conn) {
	    try { conn.close(); } catch (Exception e) {}
	}
}