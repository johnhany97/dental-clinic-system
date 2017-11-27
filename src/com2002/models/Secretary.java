package com2002.models;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class Secretary extends Staff {

	// Schedule schedule = new Schedule();

	public Secretary(String username, String password) throws SQLException {
		super(username, password);
	}

	public Secretary(String firstName, String lastName, String username, String password)
			throws MySQLIntegrityConstraintViolationException, SQLException {
		super(firstName, lastName, username, password, Role.SECRETARY);
	}

	/**
	 * Registers an address.
	 * 
	 * @param houseNumber
	 *            House Number of the address
	 * @param streetName
	 *            Street Name of the address
	 * @param district
	 *            District of the address
	 * @param city
	 *            City of the address
	 * @param postcode
	 *            Postcode of the address
	 */
	public Address registerAddress(String houseNumber, String streetName, String district, String city, String postcode)
			throws MySQLIntegrityConstraintViolationException, CommunicationsException, SQLException {
		Address address = new Address(houseNumber, streetName, district, city, postcode);
		return address;
	}

	/**
	 * Registers a patient.
	 * 
	 * @param firstName
	 *            First Name of the patient.
	 * @param lastName
	 *            Last Name of the patient.
	 * @param dateOfBirth
	 *            Date of Birth of the patient.
	 * @param phoneNumber
	 *            Phone Number of the patient
	 * @param houseNumber
	 *            House Number of the patient
	 * @param postcode
	 *            Postcode of the patient
	 */
	public Patient registerPatient(String title, String firstName, String lastName, LocalDate dob, String phoneNumber,
			String houseNumber, String postcode)
			throws MySQLIntegrityConstraintViolationException, CommunicationsException, SQLException {
		Patient patient = new Patient(title, firstName, lastName, dob, phoneNumber, houseNumber, postcode);
		return patient;
	}

	/**
	 * Subscribes a patient to a given health plan.
	 * 
	 * @param patient
	 *            The patient that you want to subscribe
	 * @param healthPlanName
	 *            The health plan that the patient wants to be subscribed too
	 * @throws MySQLIntegrityConstraintViolationException
	 *             when the patient is already subscribed
	 * @throws CommunicationsException
	 *             when an error occurs whilst attempting connection
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public void subscribePatient(Patient patient, String healthPlanName)
			throws MySQLIntegrityConstraintViolationException, CommunicationsException, SQLException {
		patient.subscribePatient(healthPlanName);
	}

	/**
	 * Unsubscribes a patient from a health plan.
	 * 
	 * @param patient
	 *            The patient that you want to unsubscribe
	 * @throws CommunicationsException
	 *             when an error occurs whilst attempting connection
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public void unsubscribePatient(Patient patient) throws CommunicationsException, SQLException {
		patient.unsubscribePatient();
	}

	/**
	 * Resets a patients health plan if a year has passed.
	 * 
	 * @param patient
	 *            The patient that you want to reset their subscription
	 * @throws CommunicationsException
	 *             when an error occurs whilst attempting connection
	 * @throws SQLException
	 *             for any other error, could be incorrect parameters.
	 */
	public void resetHealthPlan(Patient patient) throws CommunicationsException, SQLException {
		patient.resetHealthPlan();
	}

	/**
	 * Searches for patients of a particular address
	 * 
	 * @param houseNumber
	 *            The houseNumber of the address search
	 * @param postcode
	 *            The postcode of the address search
	 * @return
	 * @throws Check
	 *             exceptions from patients class
	 */
	public ArrayList<Patient> searchByAddress(String houseNumber, String postcode) throws Exception {
		return DBQueries.getPatientsByAddress(houseNumber, postcode);
	}

	/**
	 * Searches through all patients and returns arraylist of them (can return
	 * partial matches)
	 * 
	 * @param firstName
	 *            of patient
	 * @param lastName
	 *            of patient
	 * @param houseNumber
	 *            of patient
	 * @param postcode
	 *            of patient
	 * @return ArrayList of potential patients
	 * @throws Exception
	 *             if error occurred while searching through db
	 */
	public ArrayList<Patient> searchPatients(String firstName, String lastName, String houseNumber, String postcode)
			throws Exception {
		return DBQueries.searchPatients(firstName, lastName, houseNumber, postcode);
	}

	/**
	 * Searches through all addresses and returns arraylist of them (can return
	 * partial matches)
	 * 
	 * @param houseNumber
	 *            of address
	 * @param streetName
	 *            of address
	 * @param district
	 *            of address
	 * @param city
	 *            of address
	 * @param postcode
	 *            of address
	 * @return ArrayList of potential Addresses
	 * @throws SQLException
	 *             if error occured while searching through db
	 */
	public ArrayList<Address> searchAddresses(String houseNumber, String streetName, String district, String city,
			String postcode) throws SQLException {
		return DBQueries.searchAddresses(houseNumber, streetName, district, city, postcode);
	}
}
