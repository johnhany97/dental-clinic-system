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
	 * Returns all the appointments by day as an ArrayList of Appointments.
	 * @param date A date of the java class Date.
	 * @return An ArrayList of all the Appointments by day.
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public static ArrayList<Appointment> getAppointmentsByDay(Date date) throws CommunicationsException, SQLException {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year  = localDate.getYear();
		int month = localDate.getMonthValue();
		int day   = localDate.getDayOfMonth();
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
		int year  = localDate.getYear();
		int month = localDate.getMonthValue();
		int day   = localDate.getDayOfMonth();
		String startDay = year + "-" + month + "-" + day + " 00:00:00.0";
		String endDay = year + "-" + month + "-" + day + " 23:59:59.0";
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments WHERE ((StartDate <= '" + startDay + "' AND EndDate >= '" + startDay + "') "
					+ "OR (StartDate >= '" + startDay + "' AND StartDate <= '" + endDay + "')) AND Username = '" +  username + "'", conn);
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
	 * @return An ArrayList of all the Appointments by patient.
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
		} finally {
			conn.close();
		}
		return appointments;
	}
	
	/**
	 * Returns a list of appointments for the given doctor for patients who have similar first and last name to the given parameters.
	 * @param doctor instance of a doctor
	 * @param firstName patient's first name, can be partial
	 * @param lastName patient's last name, can be partial
	 * @return list of appointments according to the given parameters
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public static  ArrayList<Appointment> getAppointmentsByDocAndName(Doctor doctor, String firstName, String lastName) throws CommunicationsException, SQLException  {
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		Connection conn = Database.getConnection();
		try {
			ResultSet patients = DBQueries.execQuery("SELECT PatientID FROM Patients WHERE FirstName LIKE '" + firstName + "' OR LastName LIKE '" + lastName + "'", conn);
			while(patients.next()) {
				int patID = patients.getInt("PatientID");
				String username = doctor.getUsername();
				ResultSet apps = DBQueries.execQuery("SELECT * FROM Appointments WHERE PatientID = " + patID + " AND Username = '" + username + "'", conn);
				while(apps.next()) {
					Timestamp startDate = apps.getTimestamp("StartDate");
					appointments.add(new Appointment(startDate, username));
				}
				
			}
		} finally {
			conn.close();
		}
		return appointments;
	}
	
	/**
	 * Returns a doctor's appointments by patient as an ArrayList of Appointments.
	 * @param username The username of a doctor.
	 * @param patient An instance of a particular Patient.
	 * @return An ArrayList of a doctor's appointments by patient.
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
 	public static ArrayList<Appointment> getDoctorAppointmentsByPatient(String username, Patient patient) throws CommunicationsException, SQLException {
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
	 * @param appointment An instance of a particular Appointment.
	 * @throws CommunicationsException when an error occurs whilst attempting connection.
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public static void deleteAppointment(Appointment appointment) throws CommunicationsException, SQLException  {
		appointment.removeAppointment();
	}
}