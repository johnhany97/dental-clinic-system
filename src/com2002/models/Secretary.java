package com2002.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	/*
	protected void registerPatient(String firstName, String lastName, String title, String phoneNumber, String address, Date dateOfBirth) {
		Patient patient = new Patient(firstName, lastName, title, phoneNumber, address, dateOfBirth);
	}
	
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
