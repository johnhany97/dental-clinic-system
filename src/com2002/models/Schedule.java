package com2002.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com2002.models.*; 
import com2002.utils.*;

public class Schedule {

	@SuppressWarnings("deprecation")
	public static ArrayList<Appointment> getAppointments() throws SQLException {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		ResultSet rs = execQuery("SELECT * FROM Appointments", conn);
		while (rs.next()) {
			String startDate = rs.getString("StartDate");
			//'2007-09-23 10:10:10'
			int yyyy = Integer.valueOf(startDate.substring(0, 4));
			int mm = Integer.valueOf(startDate.substring(5, 7));
			int dd = Integer.valueOf(startDate.substring(8, 10));
			int h = Integer.valueOf(startDate.substring(11, 13));
			int m = Integer.valueOf(startDate.substring(14, 16));
			int s = Integer.valueOf(startDate.substring(17, 19));
			Timestamp startDateTs = new Timestamp(yyyy, mm, dd, h, m, s, 0);
			String username = rs.getString("Username");
			appointments.add(new Appointment(startDateTs, username));
		}
		conn.close();
		return appointments;
	}

	@SuppressWarnings("deprecation")
	public static ArrayList<Appointment> getAppointmentsByDoctor(String username) throws SQLException {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		ResultSet rs = execQuery("SELECT * FROM Appointments WHERE Username = '" + username + "'", conn);
		while (rs.next()) {
			String startDate = rs.getString("StartDate");
			//'2007-09-23 10:10:10'
			int yyyy = Integer.valueOf(startDate.substring(0, 4));
			int mm = Integer.valueOf(startDate.substring(5, 7));
			int dd = Integer.valueOf(startDate.substring(8, 10));
			int h = Integer.valueOf(startDate.substring(11, 13));
			int m = Integer.valueOf(startDate.substring(14, 16));
			int s = Integer.valueOf(startDate.substring(17, 19));
			Timestamp startDateTs = new Timestamp(yyyy, mm, dd, h, m, s, 0);
			appointments.add(new Appointment(startDateTs, username));
		}
		return appointments;
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
		Appointment app = new Appointment(start, end, userN, patID, nts, treatmentN, totalA, currA);
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
		setAppointment(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"), java.sql.Timestamp.valueOf("2007-09-23 11:10:10.0"), "ayjee", 1, "abc", AppointmentType.CHECKUP, 1, 1);
		
	}
	
}