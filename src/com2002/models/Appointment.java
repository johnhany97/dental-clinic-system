package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import com2002.utils.Database;

public class Appointment {
	private Timestamp startTime;
	private Timestamp endTime;
	private String username;
	private int patientID;
	private String notes;
	private String appointmentType;
	private int totalAppointments;
	private int currentAppointment;
	
	/**
	 * This constructor should be called with inputs which already exist in the Appointments table.
	 * @param startTime The timestamp of when the appointment starts.
	 * @param patID The patient's ID
	 */
	public Appointment(Timestamp startTime, String username) throws CommunicationsException, SQLException {
		Connection conn =  Database.getConnection();;
		ResultSet rs = null;
		try {
			rs = DBQueries.execQuery("SELECT * FROM Appointments WHERE StartDate = '" 
					+ startTime.toString() + "' AND Username = '" + username + "'", conn);
			if(rs.next()) {
				this.startTime = startTime;
				this.endTime = rs.getTimestamp("EndDate");
				this.username = username;
				this.patientID = rs.getInt("PatientID");
				this.notes = rs.getString("Notes");
				this.appointmentType = rs.getString("Type");
				this.totalAppointments = rs.getInt("TotalAppointments");
				this.currentAppointment = rs.getInt("CurrentAppointment");
			}
		} finally {
			conn.close();
		}
	}
	
	/**
	 * This constructor should be called when creating a new appointment.
	 * @param start Timestamp of when the appointment should start.
	 * @param end Timestamp of when the appointment should end.
	 * @param userN Username of staff member conducting the appointment.
	 * @param patID The patient's ID.
	 * @param nts Any notes for the Appointment.
	 * @param treatmentN The appointment type (Remedial, Cleaning, etc.).
	 * @param totalA The total number of appointments if it's a course treatment, otherwise just set to 1.
	 * @param currA The current appointment number out of the total appointments (set to 1 if not course treatment).
	 */
	public Appointment(Timestamp startTime, Timestamp endTime, String username, int patientID, String notes,
					   AppointmentType treatmentName, int totalAppointments, int currentAppointments)
							   throws CommunicationsException, MySQLIntegrityConstraintViolationException, SQLException {
		DBQueries.execUpdate("INSERT INTO Appointments VALUES ('" + startTime.toString() + "', '" + endTime.toString() + "', '" + username + "', '" 
						+ getAppointmentTypeString(treatmentName) + "', '" + patientID + "', '" + notes + "', '" + totalAppointments + "', '" + currentAppointments + "')");
		this.startTime = startTime;
		this.endTime = endTime;
		this.username = username;
		this.patientID = patientID;
		this.notes = notes;
		this.appointmentType = getAppointmentTypeString(treatmentName);
		this.totalAppointments = totalAppointments;
		this.currentAppointment = currentAppointments;
	}
	
	/**
	 * Removes appointment from Appointments table and sets all instance values to null/defaults.
	 */
	public void removeAppointment() throws CommunicationsException, SQLException {
		DBQueries.execUpdate("DELETE FROM Appointments WHERE StartDate = " + startTime.toString() + " AND Username = " + username);
		this.startTime = null;
		this.endTime = null;
		this.username = null;
		this.patientID = 0;
		this.notes = null;
		this.appointmentType = "";
		this.totalAppointments = -1;
		this.currentAppointment = -1;
	}
	
	/**
	 * Calculates the cost of the appointment.
	 * @return The total cost of all the treatments in appointment.
	 */
	public Float calculateCost() throws CommunicationsException, SQLException {
		float cost = 0;
		Connection conn = Database.getConnection();
		try {
			if(appointmentType.equals("Remedial")) {
				ResultSet treatmentRs = DBQueries.execQuery("SELECT TreatmentName FROM AppointmentTreatment WHERE StartDate = '" 
							+ this.startTime.toString() + "' AND Username = '" + this.username + "'", conn);
				while(treatmentRs.next()) {
					String treatment = treatmentRs.getString("TreatmentName");
					ResultSet rs = DBQueries.execQuery("SELECT Price FROM Treatments WHERE Name = '" + treatment + "'", conn);
					if(rs.next()) {
						cost += rs.getFloat("Price");
					}
				}
			} else if(appointmentType.equals("Checkup")) {
				ResultSet rs = DBQueries.execQuery("SELECT Price FROM AppointmentTypes WHERE Name = 'Checkup'", conn);
				if(rs.next()) {
					cost = rs.getFloat("Price");
				}
			} else if(appointmentType.equals("Cleaning")) {
				ResultSet rs = DBQueries.execQuery("SELECT Price FROM AppointmentTypes WHERE Name = 'Cleaning'", conn);
				if(rs.next()) {
					cost = rs.getFloat("Price");
				}
			}
		} finally {
			conn.close();
		}
		return cost;
	}
	
	/**
	 * Returns timestamp of when the appointment starts.
	 * @return Timestamp of when the appointment starts.
	 */
	public Timestamp getStartTime() {
		return this.startTime;
	}
	
	/**
	 * Returns timestamp of when the appointment ends.
	 * @return Timestamp of when the appointment ends.
	 */
	public Timestamp getEndTime() {
		return this.endTime;
	}
	
	/**
	 * Updates the start and end timestamps of the appointment to the given values.
	 * @param start The new start timestamp.
	 * @param end The new end timestamp.
	 */
	public void setStartEndTime(Timestamp startTime, Timestamp endTime) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Appointments SET StartDate = " + startTime.toString() + ", EndDate = " + endTime.toString() 
					+ " WHERE StartDate = '" + this.startTime.toString() + "' AND PatientID = " + this.patientID);
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * Returns the username of the staff member conducting appointment.
	 * @return The username of the staff member conducting appointment.
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Updates the username of the staff member conducting appointment to given value.
	 * @param user The new username of the staff member conducting appointment.
	 */
	public void setUsername(String username) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Appointments SET Username = '" + username 
				+ "' WHERE StartDate = '" + this.startTime.toString() + "' AND PatientID = " + this.patientID);
		this.username = username;
	}
	
	/**
	 * Returns the ID of the patient who is booked for this appointment.
	 * @return The ID of the patient who is booked for this appointment.
	 */
	public int getPatientID() {
		return this.patientID;
	}
	
	/**
	 * Updates the patient's ID to the given value.
	 * @param patID The new patient's ID.
	 */
	public void setPatientID(int patientID) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Appointments SET PatientID = " + patientID 
						+ " WHERE StartDate = '" + this.startTime.toString() 
						+ "' AND PatientID = " + this.patientID);
		this.patientID = patientID;
	}
	
	/**
	 * Returns the notes stored for this appointment.
	 * @return The notes stored for this appointment.
	 */
	public String getNotes() {
		return this.notes;
	}
	
	/**
	 * Updates the notes to the given string.
	 * @param note The new string for the notes.
	 */
	public void setNotes(String notes) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Appointments SET Notes = '" + notes + "' WHERE StartDate = '"
					+ this.startTime.toString() + "' AND PatientID = " + this.patientID);
		this.notes = notes;
	}
	
	/**
	 * Returns the appointment type as a String.
	 * @return The appointment type as a String.
	 */
	public String getAppointmentType() {
		return this.appointmentType;
	}
	
	/**
	 * Updates the appointment type to the given type.
	 * @param appointmentT The new appointment type.
	 */
	public void setAppointmentType(AppointmentType appointmentType) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Appointments SET Type = '" + getAppointmentTypeString(appointmentType) 
			+ "' WHERE StartDate = '" + this.startTime.toString() + "' AND PatientID = " + this.patientID);
		this.appointmentType = getAppointmentTypeString(appointmentType);
	}
	
	/**
	 * Returns the total number of appointments.
	 * @return The total number of appointments.
	 */
	public int getTotalAppointments() {
		return this.totalAppointments;
	}
	
	/**
	 * Updates the total number of appointments to the given value.
	 * @param total The new value of total appointments.
	 */
	public void setTotalAppointments(int totalAppointments) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE Appointments SET TotalAppointments = " + totalAppointments + " WHERE StartDate = '"
				+ this.startTime.toString() + "' AND PatientID = " + this.patientID);
		this.totalAppointments = totalAppointments;
	}
	
	/**
	 * Returns the current appointment value.
	 * @return The current appointment value.
	 */
	public int getCurrentAppointment() {
		return this.currentAppointment;
	}
	
	/**
	 * Set the current appointment to the given value.
	 * @param current The new value of current appointment.
	 */
	public void setCurrentAppointment(int currentAppointment) throws CommunicationsException, SQLException {
			DBQueries.execUpdate("UPDATE Appointments SET CurrentAppointment = " + currentAppointment + " WHERE StartDate = '"
					+ this.startTime.toString() + "' AND PatientID = " + this.patientID);
		this.currentAppointment = currentAppointment;
	}
	
	/**
	 * Converts AppointmentType enum to a string and returns it.
	 * @param app The appointment type as an enum.
	 * @return String version of appointment type.
	 */
	private String getAppointmentTypeString(AppointmentType appointmentType) {
		if(appointmentType == AppointmentType.CHECKUP) {
			return "Checkup";
		} else if(appointmentType == AppointmentType.CLEANING) {
			return "Cleaning";
		} else if(appointmentType == AppointmentType.REMEDIAL) {
			return "Remedial";
		}
		return "Empty";
	}
	
	/**
	 * Function used to return patient of this appointment
	 * @return Patient representing said patient
	 * @throws SQLException when error with db connection occurs
	 */
	public Patient getPatient() throws SQLException {
		return new Patient(this.patientID);
	}
	
	public boolean equals(Appointment app) {
		String username2 = app.getUsername();
		Timestamp startTime2 = app.getStartTime();
		if (username2 == this.username && this.startTime.equals(startTime2)) {
			return true;
		}
		return false;
	}
	
}
