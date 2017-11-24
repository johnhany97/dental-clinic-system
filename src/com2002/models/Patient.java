package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import com2002.utils.Database;
/**
 * The class which handles the registration of a patient 
 */
public class Patient {
	
	private Usage usage;
	private int patientID;
	private String title;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String phoneNumber;
	private String houseNumber;
	private String postcode;
	
	/**
	 * This constructor should be called when creating a new patient
	 * @param title Title of the patient.
	 * @param firstName First Name of the patient.
	 * @param lastName Last Name of the patient.
	 * @param dateOfBirth Date of Birth of the patient.
	 * @param phoneNumber Phone Number of the patient
	 * @param houseNumber House Number of the patient
	 * @param postcode Postcode of the patient 
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 * @throws MySQLIntegrityConstraintViolationException if patient already exists
	 */
	
	public Patient(String title, String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String houseNumber, String postcode) throws CommunicationsException, MySQLIntegrityConstraintViolationException, SQLException{
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT MAX(PatientID) FROM Patients", conn);
			if(rs == null){
				patientID = 1;
			} else if(rs.next()) {
				patientID = rs.getInt(1) + 1;
			}
			this.title = title;
			this.firstName = firstName;
			this.lastName = lastName; 
			this.dateOfBirth = dateOfBirth;
			this.phoneNumber = phoneNumber;
			this.houseNumber = houseNumber;
			this.postcode = postcode;
			if(!dbHasPatient(firstName, houseNumber, postcode)){
				DBQueries.execUpdate("INSERT INTO Patients Values(" + patientID + ", '" + title + "', '" + firstName + "', '" + lastName + "', '" 
					+ dateOfBirth + "', '" + phoneNumber + "', '" + houseNumber + "', '" + postcode + "')");
			}else {
				throw new MySQLIntegrityConstraintViolationException("A patient with first name " + firstName + " house number " + houseNumber + " and postcode " + postcode +" already exists.");
			}
		} finally{
			conn.close();
		}
	}
	
	/**
	 * This constructor should be called when searching for a particular patient.
	 * @param firstName First Name of the patient.
	 * @param houseNumber House Number of the patient.
	 * @param postcode Postcode of the patient
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters. 
	 */
	public Patient(String firstName, String houseNumber, String postcode) throws CommunicationsException, SQLException{
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Patients WHERE  FirstName Like '%" 
				+ firstName + "%' AND HouseNumber LIKE '%" + houseNumber + "%' AND Postcode LIKE '%" + postcode + "%'", conn);
			if(rs.next()) {
				this.patientID = rs.getInt("PatientID");
				this.title = rs.getString("Title");
				this.firstName = rs.getString("FirstName");
				this.lastName = rs.getString("LastName");
				this.dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
				this.phoneNumber = rs.getString("PhoneNumber");
				this.houseNumber = rs.getString("HouseNumber");
				this.postcode = rs.getString("Postcode");
				if(Usage.hasPatientID(patientID)){
					this.usage = new Usage(patientID);
				}
			}
		} finally {
			conn.close();
		}
	}
	
	/**
	 * This constructor should be called when searching for a particular patient by ID.
	 * @param patientId Patient id of the patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters. 
	 */
	public Patient(int patientId) throws CommunicationsException, SQLException{
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Patients WHERE PatientID = '" + patientId + "'", conn);
			if (rs.next()) {
				this.patientID = rs.getInt("PatientID");
				this.title = rs.getString("Title");
				this.firstName = rs.getString("FirstName");
				this.lastName = rs.getString("LastName");
				this.dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
				this.phoneNumber = rs.getString("PhoneNumber");
				this.houseNumber = rs.getString("HouseNumber");
				this.postcode = rs.getString("Postcode");
				if(Usage.hasPatientID(patientID)){
					this.usage = new Usage(patientID);
				}
			}
		} finally {
			conn.close();
		}
	}
	
	/**
	 * Checks whether Patients table contains a specified patient.
	 * @param firstName First Name of the patient.
	 * @param lastName Last Name of the patient.
	 * @param postcode Postcode of the patient 
	 * @return True if patient already exists.
	 * @throws SQLException 
	 */
	private boolean dbHasPatient(String firstName, String houseNumber, String postcode) throws SQLException {
		String found_name = DBQueries.getData("FirstName", "Patients", "FirstName", firstName);
		String found_house = DBQueries.getData("HouseNumber", "Patients", "HouseNumber", houseNumber);
		String found_postcode = DBQueries.getData("Postcode", "Patients", "Postcode", postcode);
		return firstName == found_name && found_house == houseNumber && found_postcode == postcode;
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
	 * @param patientID The new patientID of the patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 * @throws MySQLIntegrityConstraintViolationException if patientID already exists
	 */
	public void setPatientID(int patientID) throws CommunicationsException, MySQLIntegrityConstraintViolationException, SQLException {
		DBQueries.execUpdate("UPDATE Patients SET PatientID = '" + patientID + "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		this.patientID = patientID;
	}
	
	/**
	 * Returns a Tile of a particular patient.
	 * @return title of a patient.
	 */
	public String getTitle(){
		return this.title;
	}
	
	/**
	 * Updates the Title of a patient to a given title.
	 * @param title The new Title of a patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void setTitle(String title) throws CommunicationsException, SQLException { 
		DBQueries.execUpdate("UPDATE Patients SET Title = '" + title + "' WHERE FirstName = '" + this.firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		this.title = title;
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
	 * @param firstName The new first name of a patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void setFirstName(String firstName) throws CommunicationsException, SQLException { 
		DBQueries.execUpdate("UPDATE Patients SET FirstName = '" + firstName + "' WHERE FirstName = '" + this.firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		this.firstName = firstName;
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
	 * @param lastName The new last name of a patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void setLastName(String lastName) throws CommunicationsException, SQLException{
		DBQueries.execUpdate("UPDATE Patients SET LastName = '" + lastName + "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		this.lastName = lastName;
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
	 * @param dateOfBirth The new date of birth of a patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void setDateOfBirth(LocalDate dateOfBirth) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Patients SET DateOfBirth = '" + dateOfBirth + "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		this.dateOfBirth = dateOfBirth;
	}
	
	/**
	 * Returns a Phone Number of a particular patient.
	 * @return phoneNumber The phone number of a patient.
	 */
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	
	/**
	 * Updates the Phone Number of a patient to given numbers.
	 * @param phoneNumber The new phone Number of a patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void setPhoneNumber(String phoneNumber) throws CommunicationsException, SQLException{
		DBQueries.execUpdate("UPDATE Patients SET PhoneNumber = '" + phoneNumber + "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		this.phoneNumber = phoneNumber;
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
	 * @param houseNumber The new house number of a patient.
	 */
	public void setHouseNumber(String houseNumber) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Patients SET HouseNumber = '" + houseNumber + "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + postcode +"'");
		this.houseNumber = houseNumber;
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
	 * @param postcode The new postcode of a patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void setPostcode(String postcode) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Patients SET Postcode = '" + postcode + "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + this.postcode +"'");
		this.postcode = postcode;
	}
	
	/**
	 * Returns a Date of Birth of a particular patient.
	 * @return dateOfBirth The date of birth of a patient.
	 */
	public Usage getUsage(){
		return this.usage;
	}
	
	
	/**
	 * Subscribe the patient to a health plan.
	 * @param healthPlanName The healthplan of the patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 * @throws MySQLIntegrityConstraintViolationException  patient already exists 
	 */
	public void subscribePatient(String healthPlanName) throws CommunicationsException, SQLException,  MySQLIntegrityConstraintViolationException {
		if(!Usage.hasPatientID(patientID)){
			this.usage =  new Usage(this.patientID, healthPlanName);
		} else {
			throw new MySQLIntegrityConstraintViolationException("A patient with patient id " + patientID + " is already subsrcribed.");
		}
	}
	
	/**
	 * Unsubscribes the patient from a health plan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void unsubscribePatient() throws CommunicationsException, SQLException {
		if(Usage.hasPatientID(patientID)){
			this.usage.unsubscribePatient();
			this.usage = null;
		}
	}
	
	/**
	 * Reset health plan for a patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void resetHealthPlan() throws CommunicationsException, SQLException {
		if(Usage.hasPatientID(patientID)){
			this.usage.resetHealthPlan();
		} 
	}
	
	/**
	 * Increments the check up used of a HealthPlan by 1.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void incrementCheckUp() throws CommunicationsException, SQLException{
		if(Usage.hasPatientID(patientID)){
			this.usage.incrementCheckUp();
		} 
	}
	
	/**
	 * Increments the hygiene used of a HealthPlan by 1.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void incrementHygiene() throws CommunicationsException, SQLException{
		if(Usage.hasPatientID(patientID)){
			this.usage.incrementHygiene();
		}
	}

	/**
	 * Increments the repair used of a HealthPlan by 1.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void incrementRepair() throws CommunicationsException, SQLException{
		if(Usage.hasPatientID(patientID)){
			this.usage.incrementRepair();
		}
	}
	
	/**
	 * Deletes a patient 
	 * @param patientID to distinguish the patient to be deleted
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public static void deletePatient(int patientID) throws SQLException {
		Connection conn = Database.getConnection();
		Timestamp startTime;
		String username;
		try{
		ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments WHERE  PatientID = '" + patientID + "'", conn);
		while (rs.next()) {
			username = rs.getString("Username");
			startTime = rs.getTimestamp("StartDate");
			DBQueries.execUpdate("DELETE FROM AppointmentTreatment WHERE Username = '" + username + "' AND StartDate = '" + startTime.toString() +"'");		
		}
		DBQueries.execUpdate("DELETE FROM Appointments WHERE PatientID = '" + patientID + "'");
		DBQueries.execUpdate("DELETE FROM PatientHealthPlan WHERE PatientID = '" + patientID + "'");
		DBQueries.execUpdate("DELETE FROM Patients WHERE PatientID = '" + patientID + "'"); 
		} finally {
			conn.close();
		}
	}		
			
	
	public static ArrayList<Patient> getAllPatients() throws CommunicationsException, SQLException {
		ArrayList<Patient> patients = new ArrayList<Patient>();
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Patients", conn);
			while (rs.next()) {
				int patientId = rs.getInt("PatientID");
				patients.add(new Patient(patientId));
			}
		} finally {
			conn.close();
		}
		return patients;
	}
	
}
