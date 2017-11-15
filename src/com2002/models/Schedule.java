package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import com2002.utils.Database;

public class Schedule {

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
	
	@SuppressWarnings("deprecation")
	public static ArrayList<Appointment> getAppointmentsByDoctorAndDay(Doctor doctor, Date date) throws Exception {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		String username = doctor.getUsername();
		Date datePlusOne = new Date(date.getTime() + (1000 * 60 * 60 * 24));
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		datePlusOne.setHours(0);
		datePlusOne.setMinutes(0);
		datePlusOne.setSeconds(0);
		Timestamp s1 = new Timestamp(date.getTime());
		Timestamp s2 = new Timestamp(datePlusOne.getTime());
		ResultSet rs = execQuery("SELECT * FROM Appointments WHERE Username = '" + username + 
				"' and StartDate >= '" + s1.toString() + "' and StartDate < '" + s2.toString() + "'", conn);
		while (rs.next()) {
			Timestamp startDate = rs.getTimestamp("StartDate");
			appointments.add(new Appointment(startDate, username));
		}
		return appointments;
	}
	public static ArrayList<Appointment> getAppointmentsByPatient(int patientID) throws Exception {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		int patID = patientID;
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments WHERE PatientID = '" + patID + "'", conn);
			while(rs.next()) {
				Timestamp startDate = rs.getTimestamp("StartDate");
				String username = rs.getString("Username");
				appointments.add(new Appointment(startDate, username));
			}
			conn.close();
		} finally {
			conn.close();
		}
		return appointments;
	}
	public static ArrayList<Appointment> getDoctorAppointmentsByPatient(String username, Patient patient) throws Exception {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		String patientId = String.valueOf(patient.getPatientID());
		ResultSet rs = execQuery("SELECT * FROM Appointments WHERE PatientID = '" + patientId + "' and Username = '" + username + "'", conn);
		while (rs.next()) {
			Timestamp startDate = rs.getTimestamp("StartDate");
			appointments.add(new Appointment(startDate, username));
		}
		return appointments;
	}
	
	public static ArrayList<Appointment> getAppointmentsByPatient(Patient patient) throws Exception {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		String patientId = String.valueOf(patient.getPatientID());
		ResultSet rs = execQuery("SELECT * FROM Appointments WHERE PatientID = '" + patientId + "'", conn);
		while (rs.next()) {
			Timestamp startDate = rs.getTimestamp("StartDate");
			String username = rs.getString("Username");
			appointments.add(new Appointment(startDate, username));
		}
		return appointments;
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