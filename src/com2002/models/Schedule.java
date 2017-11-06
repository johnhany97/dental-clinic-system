package com2002.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import com2002.models.*; 
import com2002.utils.*;

public class Schedule {
	
	public static ResultSet[] getAppointments() {
		ResultSet[] schedule = new ResultSet[1000];
		int count = 0;
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = execQuery("SELECT * FROM Appointments", conn);
			while(rs.next()) {
				schedule[count] = rs;
				count++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return schedule;
	}
		
	public static ResultSet[] getAppointmentsByDoctor(String userN, String pass) {
		ResultSet[] schedule = new ResultSet[1000];
		int count = 0;
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = execQuery("SELECT * FROM Appointments WHERE username = '" 
					+ userN + "' AND password = '" + pass + "'", conn);
			while(rs.next()) {
				schedule[count] = rs;
				count++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return schedule;
	}
	
	public static ResultSet[] getAppointmentsByPatient(int patID) {
		ResultSet[] schedule = new ResultSet[1000];
		int count = 0;
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = execQuery("SELECT * FROM Appointments WHERE PatientID = " + patID + "", conn);
			while(rs.next()) {
				schedule[count] = rs;
				count++;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return schedule;
	}
	
	public static void setAppointment(Timestamp start, Timestamp end, String userN, int patID, String nts, 
			   AppointmentType treatmentN, int totalA, int currA) {
		Appointment app = new Appointment(start, userN);
		app.setAppointment(start, end, userN, patID, nts, treatmentN, totalA, currA);
	}
	
	public void deleteAppointment(Timestamp start, String userN) {
		Appointment app = new Appointment(start, userN);
		app.removeAppointment( start, userN);
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
	
	public static void main(String[] args) {
		//YYYY-MM-DD HH:MM:SS
		Appointment app1 = new Appointment(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"), "a");
		app1.setAppointment(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"), java.sql.Timestamp.valueOf("2007-09-23 11:10:10.0"), "a", 1, "abc", AppointmentType.CHECKUP, 1, 1);
		
	}
	
}