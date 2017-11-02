package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com2002.utils.Database;

public class Appointment {
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String username;
	private int patientID;
	private String notes;
	private String appointmentType;
	private int totalAppointments;
	private int currentAppointment;
	
	public Appointment(LocalDateTime start, LocalDateTime end, String userN, int patID, String nts, 
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
	
	protected Float calculateCost() {
		float cost = 0;
		try {
			Connection conn = Database.getConnection();
			ResultSet treatmentRS = DBQueries.execQuery("SELECT TreatmentName FROM AppointmentTreatment WHERE StartDate = " 
					+ startTime + " AND Username = '" + username + "'", conn);
			while(treatmentRS.next()) {
				String treatment = treatmentRS.getString("TreatmentName");
				ResultSet rs = DBQueries.execQuery("SELECT Price FROM Treatments WHERE Name = '" + treatment + "'", conn);
				if(rs.next()) {
					cost += rs.getFloat("Price");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cost;
	}
	
	protected LocalDateTime getStartTime() {
		return startTime;
	}
	
	protected LocalDateTime getEndTime() {
		return endTime;
	}
	
	protected void setStartEndTime(LocalDateTime start, LocalDateTime end) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET StartDate = " + start + ", EndDate = " + end 
					+ " WHERE StartDate = " + startTime + " AND PatientID = " + patientID);
			startTime = start;
			endTime = end;
		} catch (SQLException e) {
			e.printStackTrace();
			printError("start and end times");
			return;
		}
	}
	
	protected String getUsername() {
		return username;
	}
	
	protected void setUsername(String user) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET Username = '" + user 
					+ "' WHERE StartDate = " + startTime + " AND PatientID = " + patientID);
			username = user;
		} catch (SQLException e) {
			e.printStackTrace();
			printError("username");
			return;
		}
	}
	
	protected int getPatientID() {
		return patientID;
	}
	
	protected void setPatientID(int patID) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET PatientID = " + patID 
								  + " WHERE StartDate = "
					+ startTime + " AND PatientID = " + patientID);
			patientID = patID;
		} catch (SQLException e) {
			e.printStackTrace();
			printError("patientID");
			return;
		}
	}
	
	protected String getNotes() {
		return notes;
	}
	
	protected void setNotes(String note) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET Notes = '" + note + "' WHERE StartDate = "
					+ startTime + " AND PatientID = " + patientID);
			notes = note;
		} catch (SQLException e) {
			e.printStackTrace();
			printError("notes");
			return;
		}
	}
	
	protected String getAppointmentType() {
		return appointmentType;
	}
	
	protected void setAppointmentType(AppointmentType appointmentT) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET Type = '" + getAppointmentTypeString(appointmentT) 
			+ "' WHERE StartDate = " + startTime + " AND PatientID = " + patientID);
			appointmentType = getAppointmentTypeString(appointmentT);
		} catch (SQLException e) {
			e.printStackTrace();
			printError("appointment type");
			return;
		}
	}
	
	protected int getTotalAppointments() {
		return totalAppointments;
	}
	
	protected void setTotalAppointments(int total) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET TotalAppointments = " + total + " WHERE StartDate = "
					+ startTime + " AND PatientID = " + patientID);
			totalAppointments = total;
		} catch (SQLException e) {
			e.printStackTrace();
			printError("total appointments");
			return;
		}
	}
	
	protected int getCurrentAppointment() {
		return currentAppointment;
	}
	
	protected void setCurrentAppointment(int current) {
		try {
			DBQueries.execUpdate("UPDATE Appointments SET CurrentAppointment = " + current + " WHERE StartDate = "
					+ startTime + " AND PatientID = " + patientID);
			currentAppointment = current;
		} catch (SQLException e) {
			e.printStackTrace();
			printError("current appointment");
			return;
		}
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
