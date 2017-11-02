package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		execUpdate("INSERT INTO Appointments VALUES ('" + start + "', '" + end + "', '" + userN + "', '" 
					+ patID + "', '" + nts + "', '" + treatmentN + "', '" + totalA + "', '" + currA + "')");
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
			ResultSet treatmentRS = execQuery("SELECT TreatmentName FROM AppointmentTreatment WHERE StartDate = " 
					+ startTime + " AND Username = '" + username + "'", conn);
			while(treatmentRS.next()) {
				String treatment = treatmentRS.getString("TreatmentName");
				ResultSet rs = execQuery("SELECT Price FROM Treatments WHERE Name = '" + treatment + "'", conn);
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
	
	protected void setStartTime(LocalDateTime start) {
		
	}
	
	protected LocalDateTime getEndTime() {
		return endTime;
	}
	
	protected String getUsername() {
		return username;
	}
	
	protected int getPatientID() {
		return patientID;
	}
	
	protected String getNotes() {
		return notes;
	}
	
	protected String getAppointmentType() {
		return appointmentType;
	}
	
	protected int getTotalAppointments() {
		return totalAppointments;
	}
	
	protected int getCurrentAppointment() {
		return currentAppointment;
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
	
}
