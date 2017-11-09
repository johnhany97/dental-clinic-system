package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com2002.utils.Database;

/**
 * Provides helper methods with extracting and inserting into database.
 * Do DBQueries.method to call methods from this class.
 * @author arthurgranacher
 *
 */
public class DBQueries {

	/**
	 * Use this method with a query which doesn't make changes to the databases (e.g. SELECT).
	 * Must call close() method on connection instance when you have finished using the returned ResultSet.
	 * @param query Query to be executed. Needs to be in correct SQL format.
	 * @param conn Provide a Connection instance. MUST CLOSE after finished with result set.
	 * @return Returns a ResultSet from the given query.
	 * @throws SQLException 
	 */
	public static ResultSet execQuery(String query) throws SQLException {
		Connection conn = Database.getConnection();
		ResultSet rs = null;
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} finally {
			conn.close();
		}
		return rs;
	}
	
	/**
	 * Use this method with a query which makes changes to the database (e.g. INSERT).
	 * @param query Query to be executed. Needs to be in correct SQL format.
	 * @throws SQLException 
	 */
	public static void execUpdate(String query) throws SQLException {
		Connection conn = Database.getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} finally {
			conn.close();
		}
	}
	
	/**
	 * Returns first String value from specified table, column and row.
	 * @param returnCol The name of the column of the value you want returned.
	 * @param table The name of the table you want to search in.
	 * @param selectCol The name of the column you want check for the WHERE condition.
	 * @param selectData The String you want to find in the specified column.
	 * @return
	 */
	public static String getData(String returnCol, String table, String selectCol, String selectData) {
		String data = "";
		try {
			Connection conn = Database.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT " + returnCol + " FROM " + table 
					+ " WHERE " + selectCol + " = '" + selectData + "'");
			if(rs.next()) {
				data = rs.getString(returnCol);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * Checks whether Employees table contains a specified username.
	 * @param username Username of staff.
	 * @return True if username already exists.
	 */
	public static boolean staffUsernameExists(String username) {
		String found = DBQueries.getData("Username", "Employees", "Username", username);
		return username.equals(found);
	}
	
	/*
	 * private static <T> ArrayList<T> getData(String returnCol, String table, String condition, T type) {
		ArrayList<T> alist = new ArrayList<T>();
		try {
			Connection conn = Database.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT " + returnCol + " FROM " + table 
					+ " WHERE " + condition );
			while(rs.next()) {
				if(type instanceof String) {
					alist.add(rs.getString(returnCol));
				} else if(type instanceof Integer) {
					
				} else if(type instanceof Float) {
					
				} else if(type instanceof Date) {
					
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alist;
	}
	 */
	
}
