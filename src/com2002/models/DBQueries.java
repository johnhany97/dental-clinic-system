package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

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
	public static ResultSet execQuery(String query, Connection conn) throws SQLException {
		ResultSet rs = null;
		Statement stmt = conn.createStatement();
		rs = stmt.executeQuery(query);
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
	 * @return first String value from specified table, column and row.
	 * @throws SQLException 
	 */
	public static String getData(String returnCol, String table, String selectCol, String selectData) throws SQLException {
		String data = "";
		Connection conn = Database.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT " + returnCol + " FROM " + table 
					+ " WHERE " + selectCol + " = '" + selectData + "'");
			if(rs.next()) {
				data = rs.getString(returnCol);
			}
		} finally {
			conn.close();;
		}
		return data;
	}
	
	/**
	 * Checks whether Employees table contains a specified username.
	 * @param username Username of staff.
	 * @return True if username already exists.
	 * @throws SQLException 
	 */
	public static boolean staffUsernameExists(String username) throws SQLException {
		String found = DBQueries.getData("Username", "Employees", "Username", username);
		return username.equals(found);
	}
	
	/**
	 * Returns a list of patients by their address.
	 * @param houseNumber houseNumber of the address to be searched
	 * @param postcode postcode of the address to be searched
	 * @return patients that exist at the address.
	 */
	public static ArrayList<Patient> getPatientsByAddress(String houseNumber, String postcode) throws Exception {
		ArrayList<Patient> patients = new ArrayList<Patient>();
		Connection conn = Database.getConnection();
		ResultSet rs = execQuery("SELECT * FROM Patients WHERE HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode + "'", conn);
		while (rs.next()) {
			String title = rs.getString("Title");
			String firstName = rs.getString("FirstName");
			String lastName = rs.getString("LastName");
			LocalDate dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
			String phoneNumber = rs.getString("PhoneNumber");
			patients.add(new Patient(title, firstName, lastName, dateOfBirth, phoneNumber, houseNumber, postcode));
		}
		conn.close();
		return patients;
	}
	
	/**
	 * Constructs staff member depending on staff member's role.
	 * @param username of staff member
	 * @param password of staff member
	 * @return an instance of either secretary or doctor, or null if username/password not found
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException any other error
	 */
	public static Staff constructStaff(String username, String password) throws CommunicationsException, SQLException {
		Staff staff = null;
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = execQuery("SELECT * FROM Employees WHERE username = '" + username + "' AND password = '" + password + "'", conn);
			if(rs.next()) {
				String role = rs.getString("Role");
				if(role.equalsIgnoreCase("secretary")) {
					staff = new Secretary(username, password);
				} else {
					staff = new Doctor(username, password);
				}
			}
		} finally {
			conn.close();
		}
		return staff;
	}
	
	public static ArrayList<Patient> searchPatients(String firstName, String lastName, String houseNumber, String postcode) throws Exception {
		ArrayList<Patient> patients = new ArrayList<Patient>();
		Connection conn = Database.getConnection();
		ResultSet rs = execQuery("SELECT * FROM Patients WHERE FirstName LIKE '%" + firstName + "%' AND LastName LIKE '%" + lastName +"%' AND HouseNumber LIKE '%" + houseNumber + "%' AND Postcode LIKE '%" + postcode + "%'", conn);
		while (rs.next()) {
			int id = rs.getInt("PatientID");
			patients.add(new Patient(id));
		}
		conn.close();
		return patients;
	}
	
	public static ArrayList<Address> searchAddresses(String houseNumber, String streetName, String district, String city, String postcode) throws SQLException {
		ArrayList<Address> addresses = new ArrayList<Address>();
		Connection conn = Database.getConnection();
		ResultSet rs = execQuery("SELECT * FROM Address WHERE HouseNumber LIKE '%" + houseNumber + "%' AND StreetName LIKE '%" + streetName + "%' AND District LIKE '%" + district + "%' AND City LIKE '%" + city + "%' AND Postcode LIKE '%" + postcode + "%'", conn);
		while (rs.next()) {
			String hnum = rs.getString("HouseNumber");
			String pcode = rs.getString("Postcode");
			addresses.add(new Address(hnum, pcode));
		}
		conn.close();
		return addresses;
	}
	
	/**
	 * Returns a HashMap of all of the treatments and their prices stored in the database.
	 * @return HashMap with the treatment name (String) as the key and the price as a value (Double)
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException any other error
	 */
	public static HashMap<String, Double> getTreatments() throws CommunicationsException, SQLException {
		HashMap<String, Double> treatments = new HashMap<String, Double>();
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = execQuery("SELECT * FROM Treatments", conn);
			while(rs.next()) {
				treatments.put(rs.getString("Name"), rs.getDouble("Price"));
			}
		} finally {
			conn.close();
		}
		return treatments;
	}
	
}
