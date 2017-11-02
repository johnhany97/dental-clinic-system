package com2002.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com2002.utils.Database;

public class Patient {
	
	private int patientID;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String phoneNumber;
	private String houseNumber;
	private String postcode;

	public Patient(String fName, String lName, LocalDate dob, String pNumber, String hNumber, String pcode){
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = execQuery("SELECT MAX(PatientID) FROM Patients", conn);
			if(rs == null){
				patientID = 1;
			} else if(rs.next()) {
				patientID = rs.getInt(1) + 1;
			}
		    firstName = fName;
			lastName = lName; 
			dateOfBirth = dob;
		    phoneNumber = pNumber;
			houseNumber = hNumber;
			postcode = pcode;
			if(!dbHasPatient(firstName, houseNumber, postcode)){
				execUpdate("INSERT INTO Patients Values('" + patientID + "', '" + fName + "', '" + lName + "', '" 
					+ dob + "', '" + pNumber + "', '" + hNumber + "', '" + pcode + "')");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Patient(String fName, String hNumber, String pcode) {
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = execQuery("SELECT * FROM Patients WHERE FirstName = 'fName' AND HouseNumber = 'hNumber' AND Postcode = 'pcode'", conn);
			if(rs.next()) {
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
				phoneNumber = rs.getString("LastName");
				houseNumber = rs.getString("HouseNumber");
				postcode = rs.getString("Postcode");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean dbHasPatient(String fName, String hNumber, String pcode) {
		String found_name = getData("FirstName", "Patients", "FirstName", fName);
		String found_house = getData("HouseNumber", "Patients", "HouseNumber", hNumber);
		String found_postcode = getData("Postcode", "Patients", "Postcode", pcode);
		return fName == found_name && found_house == hNumber && found_postcode == pcode;
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
	
	public int getPatientID(){
		return this.patientID;
	}
	public String getFirstName(){
		return this.firstName;
	}
	public String getLastName(){
		return this.lastName;
	}
	public LocalDate getDateOfBirth(){
		return this.dateOfBirth;
	}
	public String getPhoneNumbere(){
		return this.phoneNumber;
	}
	public String getHouseNumber(){
		return this.houseNumber;
	}
	public String getPostcode(){
		return this.postcode;
	}
	
	public static void main(String[] args) {
		LocalDate dt = LocalDate.of(1997,05, 17);
		Patient nur = new Patient("Nur", "Magid", dt, "07543867024", "57", "W5 1LF");
		System.out.println(nur.getPatientID() + nur.getFirstName() + nur.getLastName() + nur.getDateOfBirth() + nur.getPhoneNumbere()+nur.getHouseNumber()+ nur.getPostcode());
	}
	
}
