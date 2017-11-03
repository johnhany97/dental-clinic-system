package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
	
	public Appointment(Timestamp startD, String userN) {
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments WHERE StartDate = " 
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
		}
	}
	
	public void setAppointment(Timestamp start, Timestamp end, String userN, int patID, String nts, 
					   AppointmentType treatmentN, int totalA, int currA) {
		try {
			DBQueries.execUpdate("INSERT INTO Appointments VALUES ('" + start + "', '" + end + "', '" + userN + "', '" 
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
	
	protected Float calculateCost() {
		float cost = 0;
		try {
			Connection conn = Database.getConnection();
			if(appointmentType.equals("Remedial")) {
				ResultSet treatmentRS = DBQueries.execQuery("SELECT TreatmentName FROM AppointmentTreatment WHERE StartDate = " 
						+ startTime + " AND Username = '" + username + "'", conn);
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
		}
		return cost;
	}
	
	protected Timestamp getStartTime() {
		return startTime;
	}
	
	protected Timestamp getEndTime() {
		return endTime;
	}
	
	protected void setStartEndTime(Timestamp start, Timestamp end) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET StartDate = " + start + ", EndDate = " + end 
					+ " WHERE StartDate = " + startTime + " AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("start and end times");
			return;
		}
		startTime = start;
		endTime = end;
	}
	
	protected String getUsername() {
		return username;
	}
	
	protected void setUsername(String user) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET Username = '" + user 
					+ "' WHERE StartDate = " + startTime + " AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("username");
			return;
		}
		username = user;
	}
	
	protected int getPatientID() {
		return patientID;
	}
	
	protected void setPatientID(int patID) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET PatientID = " + patID 
								  + " WHERE StartDate = "
					+ startTime + " AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("patientID");
			return;
		}
		patientID = patID;
	}
	
	protected String getNotes() {
		return notes;
	}
	
	protected void setNotes(String note) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET Notes = '" + note + "' WHERE StartDate = "
					+ startTime + " AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("notes");
			return;
		}
		notes = note;
	}
	
	protected String getAppointmentType() {
		return appointmentType;
	}
	
	protected void setAppointmentType(AppointmentType appointmentT) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET Type = '" + getAppointmentTypeString(appointmentT) 
			+ "' WHERE StartDate = " + startTime + " AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("appointment type");
			return;
		}
		appointmentType = getAppointmentTypeString(appointmentT);
	}
	
	protected int getTotalAppointments() {
		return totalAppointments;
	}
	
	protected void setTotalAppointments(int total) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET TotalAppointments = " + total + " WHERE StartDate = "
					+ startTime + " AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("total appointments");
			return;
		}
		totalAppointments = total;
	}
	
	protected int getCurrentAppointment() {
		return currentAppointment;
	}
	
	protected void setCurrentAppointment(int current) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET CurrentAppointment = " + current + " WHERE StartDate = "
					+ startTime + " AND PatientID = " + patientID);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("current appointment");
			return;
		}
		currentAppointment = current;
	}
	
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
	
	
	
	private void printError(String method) {
		System.out.println("Something went wrong with updating the " + method + ". "
				+ "The appointment may have not been initialised properly "
				+ "(some instance variables might be null).");
	}
	
}
