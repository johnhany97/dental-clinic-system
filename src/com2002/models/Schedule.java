package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import com2002.utils.Database;

import com2002.models.Appointment;

public class Schedule {
	
	/**
	 * Returns all the appointments as an ArrayList of Appointments.
	 * @return An ArrayList of all the Appointments.
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error.
	 */
	public static ArrayList<Appointment> getAppointments() throws CommunicationsException, SQLException {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments", conn);
			while (rs.next()) {
				Timestamp startDate = rs.getTimestamp("StartDate");
				String username = rs.getString("Username");
				appointments.add(new Appointment(startDate, username));
			}
		} finally {
			conn.close();
		}
		return appointments;
	}
	
	/**
	 * Returns all the appointments by doctor as an ArrayList of Appointments.
	 * @param doctor A doctor, either hygienist or dentist.
	 * @return An ArrayList of all the Appointments by doctor.
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public static ArrayList<Appointment> getAppointmentsByDay(Date date) throws CommunicationsException, SQLException {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		String year  = String.valueOf(localDate.getYear());
		String month = String.valueOf(localDate.getMonthValue());
		String day   = String.valueOf(localDate.getDayOfMonth());
		String startDay = year + "-" + month + "-" + day + " 00:00:00.0";
		String endDay = year + "-" + month + "-" + day + " 23:59:59.0";
		try {
			ResultSet apps = DBQueries.execQuery("SELECT * FROM Appointments WHERE (StartDate <= '" + startDay + "' AND EndDate >= '" + startDay + "') "
					+ "OR (StartDate >= '" + startDay + "' AND StartDate <= '" + endDay + "')", conn);
			while (apps.next()) {
				Timestamp startDate = apps.getTimestamp("StartDate");
				String username = apps.getString("Username");
				appointments.add(new Appointment(startDate, username));
			}
		} finally {
			conn.close();
		}
		return appointments;
	}
	
	
	/**
	 * Returns all the appointments by doctor as an ArrayList of Appointments.
	 * @param doctor A doctor, either hygienist or dentist.
	 * @return An ArrayList of all the Appointments by doctor.
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public static ArrayList<Appointment> getAppointmentsByDoctor(Doctor doctor) throws CommunicationsException, SQLException  {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		String username = doctor.getUsername();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments WHERE Username = '" + username + "'", conn);
			while (rs.next()) {
				Timestamp startDate = rs.getTimestamp("StartDate");
				appointments.add(new Appointment(startDate, username));
			}
		} finally {
			conn.close();
		}
		return appointments;
	}
	
	/**
	 * Returns all the appointments by doctor and day as an ArrayList of Appointments.
	 * @param doctor A doctor, either hygienist or dentist.
	 * @param date A date of the java class Date.
	 * @return An ArrayList of all the Appointments by doctor and day.
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public static ArrayList<Appointment> getAppointmentsByDoctorAndDay(Doctor doctor, Date date) throws CommunicationsException, SQLException   {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		String username = doctor.getUsername();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		String year  = String.valueOf(localDate.getYear());
		String month = String.valueOf(localDate.getMonthValue());
		String day   = String.valueOf(localDate.getDayOfMonth());
		String startDay = year + "-" + month + "-" + day + " 00:00:00.0";
		String endDay = year + "-" + month + "-" + day + " 23:59:59.0";
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments WHERE (StartDate <= '" + startDay + "' AND EndDate >= '" + startDay + "') "
					+ "OR (StartDate >= '" + startDay + "' AND StartDate <= '" + endDay + "') AND Username = '" +  username + "'", conn);
			while (rs.next()) {
				Timestamp startDate = rs.getTimestamp("StartDate");
				appointments.add(new Appointment(startDate, username));
			}
		} finally {
			conn.close();
		}
		return appointments;
	}
	
	/**
	 * Returns all the appointments by patient as an ArrayList of Appointments.
	 * @param patientID The Patient ID of a patient.
	 * @return Returns all the appointments by patient as an ArrayList of Appointments.
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public static ArrayList<Appointment> getAppointmentsByPatient(int patientID) throws CommunicationsException, SQLException  {
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
	    try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments WHERE PatientID = '" + patientId + "' and Username = '" + username + "'", conn);
			while (rs.next()) {
				Timestamp startDate = rs.getTimestamp("StartDate");
				appointments.add(new Appointment(startDate, username));
			}
	    } finally {
	    	conn.close();
	    }
		return appointments;
	}
	
	/**
	 * Calls the appointment constructor to create an appointment.
	 * @param start Timestamp of when the appointment should start.
	 * @param end Timestamp of when the appointment should end.
	 * @param userN Username of staff member conducting the appointment.
	 * @param patID The patient's ID.
	 * @param nts Any notes for the Appointment.
	 * @param treatmentN The appointment type (Remedial, Cleaning, etc.).
	 * @param totalA The total number of appointments if it's a course treatment, otherwise just set to 1.
	 * @param currA The current appointment number out of the total appointments (set to 1 if not course treatment).
	 * @throws MySQLIntegrityConstraintViolationException If appointment already exists.
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public static void setAppointment(Timestamp start, Timestamp end, String userN, int patID, String nts, 
			   AppointmentType treatmentN, int totalA, int currA) throws MySQLIntegrityConstraintViolationException, CommunicationsException, SQLException {
		new Appointment(start, end, userN, patID, nts, treatmentN, totalA, currA);
	}
	
	/**
	 * Deletes an appointment from the Appointments Table.
	 * @param start Timestamp of when the appointment should start.
	 * @param userN Username of staff member conducting the appointment.
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public void deleteAppointment(Timestamp start, String userN) throws CommunicationsException, SQLException  {
		Appointment app = new Appointment(start, userN);
		app.removeAppointment();
	}
}