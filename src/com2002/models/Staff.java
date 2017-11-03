package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com2002.utils.Database;

// remind John to remove password from class diag


public abstract class Staff {
	
	private String firstName;
	private String lastName;
	private String username;
	private String role;
	//private String password;
	
	/**
	 * This constructor should be called with inputs which already exist in the Staff table.
	 * @param userN Username of the staff member.
	 * @param pass Password of the staff member.
	 */
	public Staff(String userN, String pass) {
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Employees WHERE username = '" 
					+ userN + "' AND password = '" + pass + "'", conn);
			if(rs.next()) {
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				username = userN;
				role = rs.getString("Role");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Something went wrong. A staff member with this username and password may not exist.");
		}
	}
	
	/**
	 * This constructor is called whenever a new Doctor or Secretary staff member is created.
	 * @param fName First name of staff member.
	 * @param lName Last name of staff member.
	 * @param userN Username of staff member, needs to be unique.
	 * @param pass Password of staff member.
	 * @param r Role of staff member.
	 */
	public Staff(String fName, String lName, String userN, String pass, Role r) {
		if(!dbHasUsername(userN)) {
			try {
				DBQueries.execUpdate("INSERT INTO Employees VALUES ('" + fName + "', '" + lName + "', '" 
						+ userN + "', '" + pass + "', '" + getRoleString(r) + "')");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("An error has occurred. A staff member with username " + userN + " might already exist.");
				return;
			}
			firstName = fName;
			lastName = lName;
			username = userN;
			role = getRoleString(r);
		}
	}
	
	/**
	 * Checks whether Employees table contains a specified username.
	 * @param userN Username of staff.
	 * @return True if username already exists.
	 */
	private boolean dbHasUsername(String userN) {
		String found = DBQueries.getData("Username", "Employees", "Username", userN);
		return userN == found;
		
	}
	
	/**
	 * Returns the first name of the staff member.
	 * @return The first name of the staff member.
	 */
	protected String getFirstName() {
		return firstName;
	}
	
	/**
	 * Updates the value of the first name.
	 * @param firstN The new value of first name.
	 */
	protected void setFirstName(String firstN) {
		try {
			DBQueries.execUpdate("UPDATE Employees SET FirstName = '" + firstN 
					+ "' WHERE Username = '" + username + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("first name");
			return;
		}
		firstName = firstN;
	}
	
	/**
	 * Returns the last name of staff member.
	 * @return The last name of staff member.
	 */
	protected String getLastName() {
		return lastName;
	}
	
	/**
	 * Updates the last name to given value.
	 * @param lastN
	 */
	protected void setLastName(String lastN) {
		try {
			DBQueries.execUpdate("UPDATE Employees SET LastName = '" + lastN 
					+ "' WHERE Username = '" + username + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("last name");
			return;
		}
		lastName = lastN;
	}
	
	/**
	 * Returns the username of the staff member.
	 * @return The username of the staff member.
	 */
	protected String getUsername() {
		return username;
	}
	
	/**
	 * Updates the value of the username to the given value.
	 * @param userN The new username value.
	 */
	protected void setUsername(String userN) {
		try {
			DBQueries.execUpdate("UPDATE Employees SET Username = '" + userN 
					+ "' WHERE Username = '" + username + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("username");
			return;
		}
		username = userN;
	}
	
	/**
	 * Returns the role of staff member as a string.
	 * @return The role of staff member as a string.
	 */
	protected String getRole() {
		return role;
	}
	
	/**
	 * Updates the role of staff member to the given role.
	 * @param r The new role you want the staff member to be.
	 */
	protected void setRole(Role r) {
		try {
			DBQueries.execUpdate("UPDATE Employees SET Role = '" + getRoleString(r) 
					+ "' WHERE Username = '" + username + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("role");
			return;
		}
		role = getRoleString(r);
	}
	
	/**
	 * Converts Role of staff member into a string and returns it.
	 * @param r Role of staff member as an enum.
	 * @return Role of staff member as a string.
	 */
	protected String getRoleString(Role r) {
		if(r == Role.HYGIENIST) {
			return "Hygienist";
		} else if(r == Role.SECRETARY) {
			return "Secretary";
		}
		return "Dentist";
	}
	
	/**
	 * Method for printing error message to the console.
	 * @param method The method from which the error has occurred.
	 */
	private void printError(String method) {
		System.out.println("Something went wrong with updating the " + method + ". "
				+ "The staff member may have not been initialised properly "
				+ "(some instance variables might be null).");
	}
	
	public static void main(String[] args) {
		Staff arthur = new Secretary("Arthur", "Granacher", "ayjee", "password");
		System.out.println(arthur.getFirstName() + " " + arthur.getLastName() + " with username " 
		+ arthur.getUsername() + " is a " + arthur.getRole());
	}
	
	
	
}
