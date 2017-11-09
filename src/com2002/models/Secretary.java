package com2002.models;

import java.sql.SQLException;
import java.time.LocalDate;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;


public class Secretary extends Staff {

	//Schedule schedule = new Schedule();
	
	public Secretary(String username, String password) throws SQLException {
		super(username, password);
	}
	
	public Secretary(String firstName, String lastName, String username, String password) throws MySQLIntegrityConstraintViolationException, SQLException {
		super(firstName, lastName, username, password, Role.SECRETARY);
	}
	
	protected Address registerAddress(String houseNumber, String streetName, String district, String city, String postcode) {
		Address address = new Address(houseNumber, streetName, district, city, postcode);
		return address;
	}
	
	protected Patient registerPatient(String firstName, String lastName, LocalDate dob, String phoneNumber, String houseNumber, String postcode) 
			throws MySQLIntegrityConstraintViolationException, CommunicationsException, SQLException {
		Patient patient = new Patient(firstName, lastName, dob, phoneNumber, houseNumber, postcode);
		return patient;
	}
	
	protected Usage subscribePatient(Patient patient, HealthPlan healthPlan) {
		int patientID = patient.getPatientID();
		String healthPlanName = healthPlan.getName();
		return new Usage(patientID, healthPlanName);
	}
	
	protected void unsubscribePatient(Patient patient) {
		int patientID = patient.getPatientID();
	}
	
	/*
	protected [Appointment] viewSchedule() {
		return schedule.getAppointments();
	}
	*/
	
}
