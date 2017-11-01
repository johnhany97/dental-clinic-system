package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com2002.utils.Database;

// remind John to remove password from class diag


public abstract class Staff {
	
	private String firstName;
	private String lastName;
	private String username;
	private String role;
	//private String password;
	
	public Staff(String userN, String pass) {
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = execQuery("SELECT * FROM Employees WHERE username = '" 
					+ userN + "' AND password = '" + pass + "'", conn);
			if(rs.next()) {
				System.out.println("HAS NEXT");
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				username = userN;
				role = rs.getString("Role");
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Staff(String fName, String lName, String userN, String pass, Role r) {
		if(!dbHasUsername(userN)) {
			execUpdate("INSERT INTO Employees (" + fName + ", " + lName + ", " 
					+ userN + ", " + pass + ", " + getRoleString(r));
			firstName = fName;
			lastName = lName;
			username = userN;
			role = getRoleString(r);
		}
		
	}

	
	
	private boolean dbHasUsername(String userN) {
		String found = getData("Username", "Employees", "Username", userN);
		return userN == found;
		
	}
	
	private static String getData(String returnCol, String table, String selectCol, String selectData) {
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
	
	private static ResultSet execQuery(String query, Connection conn) {
		ResultSet rs = null;
		try {
			conn = Database.getConnection();
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	private static void execUpdate(String query) {
		try {
			Connection conn = Database.getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected String getRoleString(Role r) {
		if(r == Role.HYGIENIST) {
			return "Hygienist";
		} else if(r == Role.SECRETARY) {
			return "Secretary";
		}
		return "Dentist";
	}
	
	
	public static void main(String[] args) {
		Staff arthur = new Secretary("ayjee", "password");
		System.out.println(arthur.getFirstName() + " " + arthur.getLastName() + " with username " + arthur.getUsername() + " is a " + arthur.getRole());
	}
	
	protected String getFirstName() {
		return firstName;
	}
	
	protected String getLastName() {
		return lastName;
	}
	
	protected String getUsername() {
		return username;
	}
	
	protected String getRole() {
		return role;
	}
	
}
