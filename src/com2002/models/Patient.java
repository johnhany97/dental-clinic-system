package com2002.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

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
	 * @param firstName First Name of the patient.
	 * @param lastName Last Name of the patient.
	 * @param dateOfBirth Date of Birth of the patient.
	 * @param phoneNumber Phone Number of the patient
	 * @param houseNumber House Number of the patient
	 * @param postcode Postcode of the patient 
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 * @throws MySQLIntegrityConstraintViolationException if username already exists
	 */
	
	public Patient(String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String houseNumber, String postcode) throws CommunicationsException, MySQLIntegrityConstraintViolationException, SQLException{
		ResultSet rs = DBQueries.execQuery("SELECT MAX(PatientID) FROM Patients");
		if(rs == null){
			patientID = 1;
		} else if(rs.next()) {
			patientID = rs.getInt(1) + 1;
		}
		this.firstName = firstName;
		this.lastName = lastName; 
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
		this.houseNumber = houseNumber;
		this.postcode = postcode;
		if(!dbHasPatient(firstName, houseNumber, postcode)){
			DBQueries.execUpdate("INSERT INTO Patients Values('" + patientID + "', '" + firstName + "', '" + lastName + "', '" 
				+ dateOfBirth + "', '" + phoneNumber + "', '" + houseNumber + "', '" + postcode + "')");
		}else {
			throw new MySQLIntegrityConstraintViolationException("A patient with first name " + firstName + " house number " + houseNumber + " and postcode " + postcode +" already exists.");
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
		ResultSet rs = DBQueries.execQuery("SELECT * FROM Patients WHERE  FirstName = '" 
			+ firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode + "'");
		if(rs.next()) {
			this.patientID = rs.getInt("PatientID");
			this.firstName = rs.getString("FirstName");
			this.lastName = rs.getString("LastName");
			this.dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
			this.phoneNumber = rs.getString("LastName");
			this.houseNumber = rs.getString("HouseNumber");
			this.postcode = rs.getString("Postcode");
		}
	}
	
	/**
	 * This constructor should be called when searching for a particular patient by ID.
	 * @param patientId Patient id of the patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters. 
	 */
	public Patient(int patientId) throws CommunicationsException, SQLException{
		ResultSet rs = DBQueries.execQuery("SELECT * FROM Patients WHERE PatientID = '" + patientId + "'");
		if (rs.next()) {
			this.patientID = rs.getInt("PatientID");
			this.firstName = rs.getString("FirstName");
			this.lastName = rs.getString("LastName");
			this.dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
			this.phoneNumber = rs.getString("LastName");
			this.houseNumber = rs.getString("HouseNumber");
			this.postcode = rs.getString("Postcode");
		}
	}
	
	/**
	 * Checks whether Patients table contains a specified patient.
	 * @param firstName First Name of the patient.
	 * @param lastName Last Name of the patient.
	 * @param postcode Postcode of the patient 
	 * @return True if patient already exists.
	 */
	private boolean dbHasPatient(String firstName, String houseNumber, String postcode) {
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
	 * @throws MySQLIntegrityConstraintViolationException if username already exists
	 */
	protected void setPatientID(int patientID) throws CommunicationsException, MySQLIntegrityConstraintViolationException, SQLException {
		DBQueries.execUpdate("UPDATE Patients SET PatientID = '" + patientID + "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		this.patientID = patientID;
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
	protected void setFirstName(String firstName) throws CommunicationsException, SQLException { 
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
	protected void setLastName(String lastName) throws CommunicationsException, SQLException{
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
	protected void setDateOfBirth(LocalDate dateOfBirth) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Patients SET DateOfBirth = '" + dateOfBirth + "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		this.dateOfBirth = dateOfBirth;
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
	 * @param phoneNumber The new phone Number of a patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	protected void setPhoneNumber(String phoneNumber) throws CommunicationsException, SQLException{
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
	protected void setHouseNumber(String houseNumber) throws CommunicationsException, SQLException {
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
	protected void setPostcode(String postcode) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Patients SET Postcode = '" + postcode + "' WHERE FirstName = '" + firstName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + this.postcode +"'");
		this.postcode = postcode;
	}
	
	public static void main(String[] args) {
		//LocalDate dt = LocalDate.of(1997,05, 18);
		//Patient nur = new Patient("Nur", "Magid", dt, "07543867024", "57", "W5 1LF");
		// nur.setPatientID(2);
		// nur.setFirstName("Arthur");
		// nur.setLastName("Granacher");
		// nur.setDateOfBirth(newDT);
		// nur.setPhoneNumber("07543867023");
		// nur.setHouseNumber("59");
		// nur.setPostcode("s10 3an");

		//System.out.println(nur.getPatientID() + nur.getFirstName() + nur.getLastName() + nur.getDateOfBirth() + nur.getPhoneNumbere()+nur.getHouseNumber()+ nur.getPostcode());
	}
	
}
