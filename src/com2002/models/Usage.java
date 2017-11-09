package com2002.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class Usage {
	
	private int patientID;
	private String healthPlanName;
	private int checkUpUsed;
	private int hygieneUsed;
	private int repairUsed;
	private LocalDate dateJoined;
	
	/**
	 * This constructor should be called when finding a treatment plan of a patient
	 * @param patientID ID of the patient to be checked 
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public Usage(int patientID) throws CommunicationsException, SQLException {
		ResultSet rs = DBQueries.execQuery("SELECT * FROM PatientHealthPlan WHERE  PatientID = '" 
				+ patientID + "'");
		this.patientID = patientID;
		if(rs.next()) {
			this.patientID = rs.getInt("PatientID");
			this.healthPlanName = rs.getString("HealthPlanName");
			this.checkUpUsed = rs.getInt("CheckUpUsed");
			this.hygieneUsed = rs.getInt("HygieneUsed");
			this.repairUsed = rs.getInt("RepairUsed");
			dateJoined = rs.getDate("DateJoined").toLocalDate();	
		}	 
	}
	
	/**
	 * This constructor should be called when subscribing a health plan to a patients
	 * @param patientID patient ID to subscribe the health plan to them
	 * @param healthPlanName Health plan name of the patient to subscribe to them
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws MySQLIntegrityConstraintViolationException if patient id already exists
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public Usage(int patientID, String healthPlanName) throws CommunicationsException, MySQLIntegrityConstraintViolationException, SQLException{
		this.patientID = patientID;
		this.healthPlanName = healthPlanName; 
		this.checkUpUsed = 0;
		this.hygieneUsed = 0;
		this.repairUsed = 0;
		this.dateJoined = LocalDate.now();
		if(!dbHasPatientID(patientID)){
			DBQueries.execUpdate("INSERT INTO PatientHealthPlan Values('" + patientID + "', '" + healthPlanName + "', '" + this.checkUpUsed + "', '" +
				this.hygieneUsed + "', '" + this.repairUsed + "', '" + this.dateJoined + "')");
		} else {
			throw new MySQLIntegrityConstraintViolationException("A patient with patient id " + patientID + " already has a heath plan.");
		}
	}
	
	/**
	 * Checks whether PatientHealthPlan table contains a specified patientID.
	 * @param patientID checks if the patient you supply has a health plan
	 * @return True if a HealthPlan already exists.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	private boolean dbHasPatientID(int patientID) throws CommunicationsException, SQLException {
		int foundID = -1;
		ResultSet rs = DBQueries.execQuery("SELECT PatientID FROM PatientHealthPlan WHERE  PatientID = " + patientID);
		if(rs.next()) {
			foundID = rs.getInt("PatientID");
		}
		return foundID == patientID;
	}
	
	/**
	 * Returns a patientID of a particular patient.
	 * @return patientID of a patient.
	 */
	public int getPatientID(){
		return this.patientID;
	}
	
	/**
	 * Returns a name of a particular health plan.
	 * @return healthPlanName The name of the health plan.
	 */
	public String getHealthPlanName() {
		return this.healthPlanName;
	}

	/**
	 * Updates the health plan of a patient to a another health plan name.
	 * @param healthPlanName The new name of a HealthPlan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	protected void setHealthPlanName(String healthPlanName) throws CommunicationsException, SQLException  {
		DBQueries.execUpdate("UPDATE PatientHealthPlan SET HealthPlanName = '" + healthPlanName + "'"
			+ " WHERE patientID = " + this.patientID);
		this.healthPlanName = healthPlanName;
	}
	
	/**
	 * Returns a Date which when the patient subscribed .
	 * @return dateJoined The date of birth of a patient.
	 */
	public LocalDate getDateJoined() {
		return dateJoined;
	}
	
	/**
	 * Updates the date joined of a patient health plan.
	 * @param dateJoined The new date joined of a patients health plan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	protected void setDateJoined(LocalDate dateJoined) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE PatientHealthPlan SET DateJoined = '" + dateJoined.toString() + "'"
			+ " WHERE patientID = " + this.patientID);
		this.dateJoined = dateJoined;
	}
	
	/**
	 * Returns a Number of appointments of checked up used of a patients HealthPlan.
	 * @return checkUpUsed The number of check up appointments of a health plan.
	 */
	public int getCheckUpUsed() {
		return checkUpUsed;
	}
	
	/**
	 * Updates the checkUpUsed of a HealthPlan to a given value.
	 * @param checkUpUsed The new check up used of a health plan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	protected void setCheckUpUsed(int checkUpUsed) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE PatientHealthPlan SET CheckUpUsed = " + checkUpUsed
			+ " WHERE patientID = " + this.patientID);
		this.checkUpUsed = checkUpUsed;
	}

	/**
	 * Returns a number of appointments of hygiene used of a patients HealthPlan.
	 * @return hygieneUsed The number of hygiene appointments of a HealthPlan.
	 */
	public int getHygieneUsed() {
		return hygieneUsed;
	}
	
	/**
	 * Updates the hygiene used of a HealthPlan to a given value.
	 * @param hygieneUsed The new number of hygiene appointments of a health plan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	protected void setHygieneUsed(int hygieneUsed) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE PatientHealthPlan SET HygieneUsed = " + hygieneUsed
			+ " WHERE patientID = " + this.patientID);
		this.hygieneUsed = hygieneUsed;
	}
	
	/**
	 * Returns a number of appointments of repair used of a patients HealthPlan.
	 * @return hygieneUsed The number of repair appointments of a health plan.
	
	 */
	public int getRepairUsed() {
		return repairUsed;
	}
	
	/**
	 * Updates the repair used of a health plan to a given value.
	 * @param repairUsed The new number of repair appointments of a HealthPlan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	protected void setRepairUsed(int repairUsed) throws CommunicationsException, SQLException{
		DBQueries.execUpdate("UPDATE PatientHealthPlan SET RepairUsed = " + repairUsed
			+ " WHERE patientID = " + this.patientID);
		this.repairUsed = repairUsed;
	}
	
	/**
	 * Increments the check up used of a HealthPlan by 1.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void incrementCheckUp() throws CommunicationsException, SQLException{
		checkUpUsed =+ 1;
		DBQueries.execUpdate("UPDATE PatientHealthPlan SET CheckUpUsed = " + checkUpUsed
				+ " WHERE patientID = " + this.patientID);
	}
	
	/**
	 * Increments the hygiene used of a HealthPlan by 1.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void incrementHygiene() throws CommunicationsException, SQLException{
		hygieneUsed =+ 1;
		DBQueries.execUpdate("UPDATE PatientHealthPlan SET HygieneUsed = " + hygieneUsed
			+ " WHERE patientID = " + this.patientID);
			
	}

	/**
	 * Increments the repair used of a HealthPlan by 1.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void incrementRepair() throws CommunicationsException, SQLException{
		repairUsed =+ 1;
		DBQueries.execUpdate("UPDATE PatientHealthPlan SET RepairUsed = " + repairUsed
			+ " WHERE patientID = " + this.patientID);
	}

	/**
	 * Rests the HealthPlan if a year has passed from when they joined.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void resetHealthPlan() throws CommunicationsException, SQLException{
		LocalDate lastYear = LocalDate.now().plusYears(-1);
		if(lastYear.isAfter(dateJoined)){
			this.dateJoined = dateJoined.plusYears(1);
			this.checkUpUsed = 0;
			this.hygieneUsed = 0;
			this.repairUsed = 0;
			DBQueries.execUpdate("UPDATE PatientHealthPlan SET CheckUpUsed = '" + this.checkUpUsed 
				+ "', HygieneUsed = '" + this.hygieneUsed + "', RepairUsed = '" + this.repairUsed +"', DateJoined = '" + this.dateJoined + "'   WHERE PatientID = '" + this.patientID + "'");
		}
	}

	
	public static void main(String[] args) {
		//LocalDate dt = LocalDate.of(2016,11, 06);
		//Usage patient1  = new Usage(1, "NHS free plan");
		//Usage patient2 = new Usage(2, "The oral health plan");
		//Usage patientTest = new Usage(1);
		
		//patient2.setHealthPlanName("The dental health plan");
		//patient2.setCheckUpUsed(2);
		//patient2.setHygieneUsed(2);
		//patient2.setRepairUsed(2);
		//patient2.setDateJoined(dt);
		//patient2.resetHealthPlan();
		//patient2.incrementCheckUp();
		//patient2.incrementHygiene();
		//patient2.incrementRepair();

		//System.out.println(patient1.getPatientID() + patient1.getHealthPlanName() + patient1.getCheckUpUsed() + patient1.getHygieneUsed() + patient1.getRepairUsed() + patient1.getDateJoined());
		//System.out.println(patient2.getPatientID() + patient2.getHealthPlanName() + patient2.getCheckUpUsed() + patient2.getHygieneUsed() + patient2.getRepairUsed() + patient2.getDateJoined());
		//System.out.println(patientTest.getPatientID() + patientTest.getHealthPlanName() + patientTest.getCheckUpUsed() + patientTest.getHygieneUsed() + patientTest.getRepairUsed() + patientTest.getDateJoined());
		
	}
}

