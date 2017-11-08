package com2002.models;

import java.sql.SQLException;
import java.time.LocalDate;
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
	
	protected Patient registerPatient(String firstName, String lastName, LocalDate dob, String phoneNumber, String houseNumber, String postcode) {
		Patient patient = new Patient(firstName, lastName, dob, phoneNumber, houseNumber, postcode);
		return patient;
	}
	
	/*
	protected void subscribePatient(Patient patient) {
		patient.subscribe(patient);
	}
	
	protected void unsubscribePatient(Patient patient) {
		patient.unsubscribe(patient);
	}
	
	protected [Appointment] viewSchedule() {
		return schedule.getAppointments();
	}
	*/
	
}
