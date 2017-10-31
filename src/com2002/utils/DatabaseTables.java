package com2002.utils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseTables {
	
	public static void setup() throws SQLException {
		dropTables();
		createTables();
	}

	public static void createTables() throws SQLException {
		Connection conn = Database.getConnection();

		String treatmentsTable = "CREATE TABLE `Treatments` (" + 
				"	`Name` VARCHAR(30) NOT NULL," + 
				"	`Price` DOUBLE," + 
				"	PRIMARY KEY(`Name`)" + 
				")";
		String appointmentsTable = "CREATE TABLE `Appointments` (\r\n" + 
				"	`StartDate`	DATE NOT NULL,\r\n" + 
				"	`EndDate`	DATE NOT NULL,\r\n" + 
				"	`Username`	VARCHAR(15) NOT NULL,\r\n" + 
				"	`PatientID`	INTEGER NOT NULL,\r\n" + 
				"	`Notes`	VARCHAR(1000),\r\n" + 
				"	`TreatmentName`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`TotalAppointments`	INTEGER,\r\n" + 
				"	`CurrentAppointment`	INTEGER,\r\n" + 
				"	PRIMARY KEY(`StartDate`,`Username`),\r\n" + 
				"	FOREIGN KEY(`Username`) REFERENCES Employees(Username),\r\n" + 
				"	FOREIGN KEY(`PatientID`) REFERENCES Patients(PatientID),\r\n" + 
				"	FOREIGN KEY(`TreatmentName`) REFERENCES Treatments(Name)\r\n" + 
				")";
		String employeesTable = "CREATE TABLE `Employees` (\r\n" + 
				"	`FirstName`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`LastName`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`Username`	VARCHAR(15) NOT NULL,\r\n" + 
				"	`Password`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`Role`	VARCHAR(30) NOT NULL,\r\n" + 
				"	PRIMARY KEY(`Username`)\r\n" + 
				")";
		String healthPlansTable = "CREATE TABLE `HealthPlans` (\r\n" + 
				"	`Name`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`Price`	REAL,\r\n" + 
				"	`CheckUpLevel`	INTEGER NOT NULL,\r\n" + 
				"	`HygieneLevel`	INTEGER NOT NULL,\r\n" + 
				"	`RepairLevel`	INTEGER NOT NULL,\r\n" + 
				"	PRIMARY KEY(`Name`)\r\n" + 
				")";
		String patientHealthPlanTable = "CREATE TABLE `PatientHealthPlan` (\r\n" + 
				"	`PatientID`	INTEGER NOT NULL,\r\n" + 
				"	`HealthPlanName`	VARCHAR(30),\r\n" + 
				"	`CheckUpUsed`	INTEGER DEFAULT 0,\r\n" + 
				"	`HygieneUsed`	INTEGER DEFAULT 0,\r\n" + 
				"	`RepairUsed`	INTEGER DEFAULT 0,\r\n" + 
				"	`DateJoined`	DATE NOT NULL,\r\n" + 
				"	PRIMARY KEY(`PatientID`,`HealthPlanName`),\r\n" + 
				"	FOREIGN KEY(`PatientID`) REFERENCES Patients(PatientID),\r\n" + 
				"	FOREIGN KEY(`HealthPlanName`) REFERENCES HealthPlans(Name)\r\n" + 
				")";
		String patientsTable = "CREATE TABLE `Patients` (\r\n" + 
				"	`PatientID`	INTEGER NOT NULL,\r\n" + 
				"	`FirstName`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`LastName`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`DateOfBirth`	DATE NOT NULL,\r\n" + 
				"	`PhoneNumber`	VARCHAR(20),\r\n" + 
				"	`HouseNumber`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`Postcode`	VARCHAR(8) NOT NULL,\r\n" + 
				"   PRIMARY KEY (`PatientID`),\r\n" +
				"	FOREIGN KEY(`HouseNumber`, `Postcode`) REFERENCES Address(HouseNumber, Postcode)\r\n" + 
				");";
		String addressTable = "CREATE TABLE `Address` (\r\n" + 
				"	`HouseNumber`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`StreetName`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`District`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`City`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`Postcode`	VARCHAR(8) NOT NULL,\r\n" + 
				"	PRIMARY KEY(`HouseNumber`,`Postcode`)\r\n" + 
				")";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(treatmentsTable);
			stmt.executeUpdate(employeesTable);
			stmt.executeUpdate(addressTable);
			stmt.executeUpdate(healthPlansTable);
			stmt.executeUpdate(patientsTable);
			stmt.executeUpdate(appointmentsTable);
			stmt.executeUpdate(patientHealthPlanTable);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.closeDb(conn);
		}
	}
	
	public static void populateTables() {
		
	}
	
	public static void dropTables() throws SQLException {
		Connection conn = Database.getConnection();
		Statement stmt = conn.createStatement();;

		try {
			String sqlString = "DROP TABLE IF EXISTS Treatments, Appointments, Employees, HealthPlans, PatientHealthPlan, Patients, Address";
            stmt.executeUpdate(sqlString);
        } catch(SQLException e) {
            System.out.println(e.toString());
        } finally {
            Database.closeDb(conn);
        }
	}
	
	public static void main(String[] args) {
		//THIS IS TEMPORARY.. .SHOULD BE MOVED ELSEWHERE
		try {
			setup();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
