package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import com2002.utils.Database;


public abstract class Staff {
	
	private String firstName;
	private String lastName;
	private String username;
	private String role;
	//private String password;
	
	/**
	 * This constructor should be called with inputs which already exist in the Staff table.
	 * @param username Username of the staff member.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public Staff(String username) throws CommunicationsException, SQLException  {
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Employees WHERE username = '" 
					+ username + "'", conn);
			if(rs.next()) {
				this.firstName = rs.getString("FirstName");
				this.lastName = rs.getString("LastName");
				this.username = username;
				this.role = rs.getString("Role");
			}
		} finally {
			conn.close();
		}
	}
	
	/**
	 * This constructor should be called with inputs which already exist in the Staff table.
	 * @param username Username of the staff member.
	 * @param password Password of the staff member.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public Staff(String username, String password) throws CommunicationsException, SQLException  {
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Employees WHERE username = '" 
					+ username + "' AND password = '" + password + "'", conn);
			if(rs.next()) {
				this.firstName = rs.getString("FirstName");
				this.lastName = rs.getString("LastName");
				this.username = username;
				this.role = rs.getString("Role");
			}
		} finally {
			conn.close();
		}
	}
	
	/**
	 * This constructor is called whenever a new Doctor or Secretary staff member is created.
	 * @param firstName First name of staff member.
	 * @param lastName Last name of staff member.
	 * @param username Username of staff member, needs to be unique.
	 * @param password Password of staff member.
	 * @param role Role of staff member.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws MySQLIntegrityConstraintViolationException if username already exists
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public Staff(String firstName, String lastName, String username, String password, Role role) 
			throws CommunicationsException, MySQLIntegrityConstraintViolationException, SQLException {
		if(!DBQueries.staffUsernameExists(username)) {
			DBQueries.execUpdate("INSERT INTO Employees VALUES ('" + firstName + "', '" + lastName + "', '" 
						+ username + "', '" + password + "', '" + getRoleString(role) + "')");
			this.firstName = firstName;
			this.lastName = lastName;
			this.username = username;
			this.role = getRoleString(role);
		} else {
			throw new MySQLIntegrityConstraintViolationException("A staff member with username " + username + " already exists.");
		}
	}
	
	
	
	/**
	 * Returns the first name of the staff member.
	 * @return The first name of the staff member.
	 */
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	 * Updates the value of the first name.
	 * @param firstName The new value of first name.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void setFirstName(String firstName) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Employees SET FirstName = '" + firstName 
					+ "' WHERE Username = '" + username + "'");
		this.firstName = firstName;
	}
	
	/**
	 * Returns the last name of staff member.
	 * @return The last name of staff member.
	 */
	public String getLastName() {
		return this.lastName;
	}
	
	/**
	 * Updates the last name to given value.
	 * @param lastName
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void setLastName(String lastName) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Employees SET LastName = '" + lastName 
					+ "' WHERE Username = '" + username + "'");
		this.lastName = lastName;
	}
	
	/**
	 * Returns the username of the staff member.
	 * @return The username of the staff member.
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Updates the value of the username to the given value.
	 * @param username The new username value.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws MySQLIntegrityConstraintViolationException if username already exists
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void setUsername(String username) throws CommunicationsException, MySQLIntegrityConstraintViolationException, SQLException {
		DBQueries.execUpdate("UPDATE Employees SET Username = '" + username 
					+ "' WHERE Username = '" + this.username + "'");
		this.username = username;
	}
	
	/**
	 * Returns the role of staff member as a string.
	 * @return The role of staff member as a string.
	 */
	public String getRole() {
		return this.role;
	}
	
	/**
	 * Updates the role of staff member to the given role.
	 * @param role The new role you want the staff member to be.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void setRole(Role role) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Employees SET Role = '" + getRoleString(role) 
					+ "' WHERE Username = '" + username + "'");
		this.role = getRoleString(role);
	}
	
	/**
	 * Converts Role of staff member into a string and returns it.
	 * @param role Role of staff member as an enum.
	 * @return Role of staff member as a string.
	 */
	public static String getRoleString(Role role) {
		if(role == Role.HYGIENIST) {
			return "Hygienist";
		} else if(role == Role.SECRETARY) {
			return "Secretary";
		}
		return "Dentist";
	}
	
	public static void main(String[] args) {
		Staff arthur;
		try {
			arthur = new Secretary("Arthur", "Granacher", "ayjee", "password");
			System.out.println(arthur.getFirstName() + " " + arthur.getLastName() + " with username " 
					+ arthur.getUsername() + " is a " + arthur.getRole());
		} catch (MySQLIntegrityConstraintViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
