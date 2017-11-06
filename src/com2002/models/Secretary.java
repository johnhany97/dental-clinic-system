package com2002.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com2002.utils.Database;

public class Secretary extends Staff {

	//Schedule schedule = new Schedule();
	
	public Secretary(String userN, String pass) throws SQLException {
		super(userN, pass);
		// TODO Auto-generated constructor stub
	}
	
	public Secretary(String firstName, String lastName, String userN, String pass) {
		super(firstName, lastName, userN, pass, Role.SECRETARY);
	}
	
	protected void registerAddress(String houseNumber, String streetName, String district, String city, String postcode) {
		Address address = new Address(houseNumber, streetName, district, city, postcode);
	}
	
	protected void registerPatient(String firstName, String lastName, LocalDate dob, String phoneNumber, String houseNumber, String postcode) {
		Patient patient = new Patient(firstName, lastName, dob, phoneNumber, houseNumber, postcode);
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
