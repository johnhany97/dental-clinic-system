package com2002.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {

	final private static String userName = "team035";
	final private static String password = "84680a5d";
	final private static String serverName = "stusql.dcs.shef.ac.uk";
	final private static String dbName = "team035";
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			Connection conn = getConnection();
			
//			Statement stmt = conn.createStatement();
//			String SQL = "CREATE TABLE `Test` (`name` TEXT,  `securityCode` INTEGER)";
//			stmt.executeUpdate(SQL);
//			
			Statement stmt2 = conn.createStatement();
			String SQL2 = "INSERT INTO `Test` (`name`, `securityCode`) VALUES ('john', '1')";
			stmt2.executeUpdate(SQL2);
			
			Statement stmt3 = conn.createStatement();
			String SQL3 = "SELECT * FROM Test";
			ResultSet rs = stmt3.executeQuery(SQL3);
			
			rs.next();
			String name= rs.getString("name");
			int securityCode = rs.getInt("securityCode");
			System.out.println("Name:" + name + " Security code: "+ securityCode);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
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
}