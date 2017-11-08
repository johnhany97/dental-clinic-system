package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
	 * @param startD The timestamp of when the appointment starts.
	 * @param patID The patient's ID
	 * @throws Exception 
	 */
	public Appointment(Timestamp startD, String userN) throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = Database.getConnection();
			rs = DBQueries.execQuery("SELECT * FROM Appointments WHERE StartDate = " 
					+ startD + " AND Username = " + userN + "", conn);
			if(rs.next()) {
				startTime = startD;
				endTime = rs.getTimestamp("EndDate");
				username = userN;
				patientID = rs.getInt("PatientID");
				notes = rs.getString("Notes");
				appointmentType = rs.getString("Type");
				totalAppointments = rs.getInt("TotalAppointments");
				currentAppointment = rs.getInt("CurrentAppointment");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			if(conn == null) {
				throw new Exception("Could not get connection.");
			} else if(rs == null) {
				throw new Exception("There was an error with your SQL query. "
						+ "An entry with the specified startDate and username may not exist.");
			}
			try {
				conn.close();
			} catch (SQLException e1) {
				throw new Exception("Could not close connection.");
			}
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
	 * @return 
	 */
	public Appointment(Timestamp start, Timestamp end, String userN, int patID, String nts,
					   AppointmentType treatmentN, int totalA, int currA) {
		try {
			DBQueries.execUpdate("INSERT INTO Appointments VALUES ('" + start.toString() + "', '" + end.toString() + "', '" + userN + "', '" 
						+ treatmentN + "', '" + patID + "', '" + nts + "', '" + totalA + "', '" + currA + "')");
		} catch (SQLException e) {
			e.printStackTrace();
			
			System.out.println("Something went wrong with creating an appointment.");
			
			return;
		}
		startTime = start;
		endTime = end;
		username = userN;
		patientID = patID;
		notes = nts;
		appointmentType = getAppointmentTypeString(treatmentN);
		totalAppointments = totalA;
		currentAppointment = currA;
	}
	
	/**
	 * Removes appointment from Appointments table and sets all instance values to null/defaults.
	 */
	protected void removeAppointment(Timestamp start, String userN) {
		try {
			DBQueries.execUpdate("DELETE FROM Appointments WHERE StartDate = " + startTime + " AND Username = " + username);
		} catch (SQLException e) {
			System.out.println("Failed to delete appointment. Make sure appointment is properly initialised.");
			return;
		}
		startTime = start;
		endTime = null;
		username = userN;
		patientID = 0;
		notes = null;
		appointmentType = "";
		totalAppointments = -1;
		currentAppointment = -1;
	}
	
	/**
	 * Calculates the cost of the appointment.
	 * @return The total cost of all the treatments in appointment.
	 */
	protected Float calculateCost() {
		float cost = 0;
		try {
			Connection conn = Database.getConnection();
			if(appointmentType.equals("Remedial")) {
				ResultSet treatmentRS = DBQueries.execQuery("SELECT TreatmentName FROM AppointmentTreatment WHERE StartDate = '" 
						+ startTime.toString() + "' AND Username = '" + username + "'", conn);
				while(treatmentRS.next()) {
					String treatment = treatmentRS.getString("TreatmentName");
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
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.print("Something went wrong with calculating the cost.");
		}
		return cost;
	}
	
	/**
	 * Returns timestamp of when the appointment starts.
	 * @return Timestamp of when the appointment starts.
	 */
	protected Timestamp getStartTime() {
		return startTime;
	}
	
	/**
	 * Returns timestamp of when the appointment ends.
	 * @return Timestamp of when the appointment ends.
	 */
	protected Timestamp getEndTime() {
		return endTime;
	}
	
	/**
	 * Updates the start and end timestamps of the appointment to the given values.
	 * @param start The new start timestamp.
	 * @param end The new end timestamp.
	 */
	protected void setStartEndTime(Timestamp start, Timestamp end) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET StartDate = " + start + ", EndDate = " + end 
					+ " WHERE StartDate = '" + startTime.toString() + "' AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("start and end times");
			return;
		}
		startTime = start;
		endTime = end;
	}
	
	/**
	 * Returns the username of the staff member conducting appointment.
	 * @return The username of the staff member conducting appointment.
	 */
	protected String getUsername() {
		return username;
	}
	
	/**
	 * Updates the username of the staff member conducting appointment to given value.
	 * @param user The new username of the staff member conducting appointment.
	 */
	protected void setUsername(String user) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET Username = '" + user 
					+ "' WHERE StartDate = '" + startTime.toString() + "' AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("username");
			return;
		}
		username = user;
	}
	
	/**
	 * Returns the ID of the patient who is booked for this appointment.
	 * @return The ID of the patient who is booked for this appointment.
	 */
	protected int getPatientID() {
		return patientID;
	}
	
	/**
	 * Updates the patient's ID to the given value.
	 * @param patID The new patient's ID.
	 */
	protected void setPatientID(int patID) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET PatientID = " + patID 
								  + " WHERE StartDate = '"
					+ startTime.toString() + "' AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("patientID");
			return;
		}
		patientID = patID;
	}
	
	/**
	 * Returns the notes stored for this appointment.
	 * @return The notes stored for this appointment.
	 */
	protected String getNotes() {
		return notes;
	}
	
	/**
	 * Updates the notes to the given string.
	 * @param note The new string for the notes.
	 */
	protected void setNotes(String note) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET Notes = '" + note + "' WHERE StartDate = '"
					+ startTime.toString() + "' AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("notes");
			return;
		}
		notes = note;
	}
	
	/**
	 * Returns the appointment type as a String.
	 * @return The appointment type as a String.
	 */
	protected String getAppointmentType() {
		return appointmentType;
	}
	
	/**
	 * Updates the appointment type to the given type.
	 * @param appointmentT The new appointment type.
	 */
	protected void setAppointmentType(AppointmentType appointmentT) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET Type = '" + getAppointmentTypeString(appointmentT) 
			+ "' WHERE StartDate = '" + startTime.toString() + "' AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("appointment type");
			return;
		}
		appointmentType = getAppointmentTypeString(appointmentT);
	}
	
	/**
	 * Returns the total number of appointments.
	 * @return The total number of appointments.
	 */
	protected int getTotalAppointments() {
		return totalAppointments;
	}
	
	/**
	 * Updates the total number of appointments to the given value.
	 * @param total The new value of total appointments.
	 */
	protected void setTotalAppointments(int total) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET TotalAppointments = " + total + " WHERE StartDate = '"
					+ startTime.toString() + "' AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("total appointments");
			return;
		}
		totalAppointments = total;
	}
	
	/**
	 * Returns the current appointment value.
	 * @return The current appointment value.
	 */
	protected int getCurrentAppointment() {
		return currentAppointment;
	}
	
	/**
	 * Set the current appointment to the given value.
	 * @param current The new value of current appointment.
	 */
	protected void setCurrentAppointment(int current) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET CurrentAppointment = " + current + " WHERE StartDate = '"
					+ startTime.toString() + "' AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("current appointment");
			return;
		}
		currentAppointment = current;
	}
	
	/**
	 * Converts AppointmentType enum to a string and returns it.
	 * @param app The appointment type as an enum.
	 * @return String version of appointment type.
	 */
	private String getAppointmentTypeString(AppointmentType app) {
		if(app == AppointmentType.CHECKUP) {
			return "Checkup";
		} else if(app == AppointmentType.CLEANING) {
			return "Cleaning";
		} else if(app == AppointmentType.REMEDIAL) {
			return "Remedial";
		}
		return "Empty";
	}
	
	/**
	 * Method for printing error message to the console.
	 * @param method The method from which the error has occurred.
	 */
	private void printError(String method) {
		System.out.println("Something went wrong with updating the " + method + ". "
				+ "The appointment may have not been initialised properly "
				+ "(some instance variables might be null).");
	}
	
}
