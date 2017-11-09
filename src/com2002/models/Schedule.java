package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import com2002.utils.Database;

public class Schedule {

	@SuppressWarnings("deprecation")
	public static ArrayList<Appointment> getAppointments() throws Exception {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		ResultSet rs = execQuery("SELECT * FROM Appointments", conn);
		while (rs.next()) {
			Timestamp startDate = rs.getTimestamp("StartDate");
			String username = rs.getString("Username");
			appointments.add(new Appointment(startDate, username));
		}
		conn.close();
		return appointments;
	}

	public static ArrayList<Appointment> getAppointmentsByDoctor(Doctor doctor) throws Exception {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		String username = doctor.getUsername();
		ResultSet rs = execQuery("SELECT * FROM Appointments WHERE Username = '" + username + "'", conn);
		while (rs.next()) {
			Timestamp startDate = rs.getTimestamp("StartDate");
			appointments.add(new Appointment(startDate, username));
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
			   AppointmentType treatmentN, int totalA, int currA) throws MySQLIntegrityConstraintViolationException, CommunicationsException, SQLException {
		Appointment app = new Appointment(start, end, userN, patID, nts, treatmentN, totalA, currA);
	}
	
	public void deleteAppointment(Timestamp start, String userN) throws Exception {
		Appointment app = new Appointment(start, userN);
//		app.removeAppointment( start, userN);
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
	
//	public static void main(String[] args) throws MySQLIntegrityConstraintViolationException, CommunicationsException, SQLException {
//		setAppointment(java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0"), java.sql.Timestamp.valueOf("2007-09-23 11:10:10.0"), "ayjee", 1, "abc", AppointmentType.CHECKUP, 1, 1);
//		
//	}
	
}