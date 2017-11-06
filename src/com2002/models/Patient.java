package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	/**
	 * This constructor should be called when creating a new patient
	 * @param fName First Name of the patient.
	 * @param lName Last Name of the patient.
	 * @param dob Date of Birth of the patient.
	 * @param pNumber Phone Number of the patient
	 * @param hNumber House Number of the patient
	 * @param pcode Postcode of the patient 
	 */
	public Patient(String fName, String lName, LocalDate dob, String pNumber, String hNumber, String pcode){
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = DBQueries.execQuery("SELECT MAX(PatientID) FROM Patients", conn);
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
				DBQueries.execUpdate("INSERT INTO Patients Values('" + patientID + "', '" + fName + "', '" + lName + "', '" 
					+ dob + "', '" + pNumber + "', '" + hNumber + "', '" + pcode + "')");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This constructor should be called when searching for a particular patient.
	 * @param fName First Name of the patient.
	 * @param hNumber House Number of the patient.
	 * @param pcode Postcode of the patient 
	 */
	public Patient(String fName, String hNumber, String pcode) {
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Patients WHERE  FirstName = '" 
					+ fName + "' AND HouseNumber = '" + hNumber + "' AND Postcode = '" + pcode + "'", conn);
			if(rs.next()) {
				patientID = rs.getInt("PatientID");
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
	
	/**
	 * Checks whether Patients table contains a specified patient.
	 * @param fName First Name of the patient.
	 * @param lName Last Name of the patient.
	 * @param pcode Postcode of the patient 
	 * @return True if patient already exists.
	 */
	private boolean dbHasPatient(String fName, String hNumber, String pcode) {
		String found_name = DBQueries.getData("FirstName", "Patients", "FirstName", fName);
		String found_house = DBQueries.getData("HouseNumber", "Patients", "HouseNumber", hNumber);
		String found_postcode = DBQueries.getData("Postcode", "Patients", "Postcode", pcode);
		return fName == found_name && found_house == hNumber && found_postcode == pcode;
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
	
	/**
	 * Returns a patientID of a particular patient.
	 * @return patientID of when the appointment starts.
	 */
	public int getPatientID(){
		return this.patientID;
	}
	
	/**
	 * Updates the patientID of a patient to a given value.
	 * @param pID The new patientID of the patient.
	 */
	protected void SetPatientID(int pID) {
		try {
			DBQueries.execUpdate("UPDATE Patients SET PatientID = '" + pID 
					+ "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("patientID");
			return;
		}
		patientID = pID;
	}
	
	/**
	 * Returns a First Name of a particular patient.
	 * @return firstName of a patient.
	 */
	public String getFirstName(){
		return this.firstName;
	}
	
	/**
	 * Updates the First Name of a patient to a given name.
	 * @param newFName The new first name of a patient.
	 */
	protected void setFirstName(String newFName) { 
		try {
			DBQueries.execUpdate("UPDATE Patients SET FirstName = '" + newFName 
					+ "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("first name");
			return;
		}
		firstName = newFName;
	}
	
	/**
	 * Returns a Last Name of a particular patient.
	 * @return lastName The last name of a patient.
	 */
	public String getLastName(){
		return this.lastName;
	}
	
	/**
	 * Updates the Last Name of a patient to a given name.
	 * @param lastN The new last name of a patient.
	 */
	protected void setLastName(String lastN) {
		try {
			DBQueries.execUpdate("UPDATE Patients SET LastName = '" + lastN 
					+ "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("last name");
			return;
		}
		lastName = lastN;
	}
	
	/**
	 * Returns a Date of Birth of a particular patient.
	 * @return dateOfBirth The date of birth of a patient.
	 */
	public LocalDate getDateOfBirth(){
		return this.dateOfBirth;
	}
	
	/**
	 * Updates the Date of Birth of a patient to a given date.
	 * @param dob The new date of birth of a patient.
	 */
	protected void SetDateOfBirth(LocalDate dob) {
		try {
			DBQueries.execUpdate("UPDATE Patients SET DateOfBirth = '" + dob 
					+ "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("date of birth");
			return;
		}
		dateOfBirth = dob;
	}
	
	/**
	 * Returns a Phone Number of a particular patient.
	 * @return phoneNumber The phone number of a patient.
	 */
	public String getPhoneNumbere(){
		return this.phoneNumber;
	}
	
	/**
	 * Updates the Phone Number of a patient to given numbers.
	 * @param pNumber The new phone Number of a patient.
	 */
	protected void setPhoneNumber(String pNumber) {
		try {
			DBQueries.execUpdate("UPDATE Patients SET PhoneNumber = '" + pNumber 
					+ "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("phone number");
			return;
		}
		phoneNumber = pNumber;
	}
	
	/**
	 * Returns a House Number of a particular patient.
	 * @return houseNumber The house number of a patient.
	 */
	public String getHouseNumber(){
		return this.houseNumber;
	}
	
	/**
	 * Updates the House Number of a patient to a given value/name.
	 * @param newHNumber The new house number of a patient.
	 */
	protected void setHouseNumber(String newHNumber) {
		try {
			DBQueries.execUpdate("UPDATE Patients SET HouseNumber = '" + newHNumber 
					+ "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("house number");
			return;
		}
		houseNumber = newHNumber;
	}
	
	/**
	 * Returns a Postcode of a particular patient.
	 * @return postcode The postcode of a patient.
	 */
	public String getPostcode(){
		return this.postcode;
	}
	
	/**
	 * Updates the postcode of a patient.
	 * @param newPcode The new postcode of a patient.
	 */
	protected void setPostcode(String newPcode) {
		try {
			DBQueries.execUpdate("UPDATE Patients SET Postcode = '" + newPcode 
					+ "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("postcode");
			return;
		}
		postcode = newPcode;
	}
	
	public static void main(String[] args) {
		LocalDate dt = LocalDate.of(1997,05, 18);
		Patient nur = new Patient("Nur", "Magid", dt, "07543867024", "57", "W5 1LF");
		System.out.println(nur.getPatientID() + nur.getFirstName() + nur.getLastName() + nur.getDateOfBirth() + nur.getPhoneNumbere()+nur.getHouseNumber()+ nur.getPostcode());
	}
	
}
