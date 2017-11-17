/**
 * Database Tables Class
 * 
 * This is used to initialize database to specific schema.
 * Used in setup
 * @author John Ayad
 */
package com2002.utils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseTables {
	
	public static void main(String[] args) {
		//Used in development only
		try {
			setup();
			populateTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Setup Function
	 * 
	 * Used to clear database from all previous tables (relating to this app)
	 * Then it creates new tables (empty) in the db
	 * @throws SQLException if an error occurred whilst attempting connection
	 */
	public static void setup() throws SQLException {
		dropTables();
		createTables();
	}
	
	public static void populateTables() {
		Connection conn = null;
		try {
			conn = Database.getConnection();
	
			Statement stmt = conn.createStatement();
			
			String treatments1 = "INSERT INTO Treatments VALUES (\"Treatment1\", 123.2)";
			String treatments2 = "INSERT INTO Treatments VALUES (\"Treatment2\", 1231.1)";
			stmt.executeUpdate(treatments1);
			stmt.executeUpdate(treatments2);
			
			String appointmentsTypes1 = "INSERT INTO AppointmentTypes VALUES (\"Checkup\", 123)";
			String appointmentsTypes2 = "INSERT INTO AppointmentTypes VALUES (\"Remedial\", 132)";
			String appointmentsTypes3 = "INSERT INTO AppointmentTypes VALUES (\"Cleaning\", 12)";
			String appointmentsTypes4 = "INSERT INTO AppointmentTypes VALUES (\"Empty\", 0)";
			stmt.executeUpdate(appointmentsTypes1);
			stmt.executeUpdate(appointmentsTypes2);
			stmt.executeUpdate(appointmentsTypes3);
			stmt.executeUpdate(appointmentsTypes4);
			
			String employee1 = "INSERT INTO Employees VALUES (\"D\", \"the amazing\", \"user1\", \"123123\", \"Dentist\")";
			String employee2 = "INSERT INTO Employees VALUES (\"H\", \"the cleaner\", \"user2\", \"123123\", \"Hygienist\")";
			String employee3 = "INSERT INTO Employees VALUES (\"S\", \"the organized\", \"user3\", \"123123\", \"Secretary\")";
			stmt.executeUpdate(employee1);
			stmt.executeUpdate(employee2);
			stmt.executeUpdate(employee3);
	
			String address1 = "INSERT INTO Address VALUES (\"Flat C43F\", \"80 Hoyle Street\", \"Sheffield\", \"Sheffield\", \"S3 7LG\")";
			String address2 = "INSERT INTO Address VALUES (\"Apt. E42F\", \"20 Crazy Street\", \"Sheffield\", \"Sheffield\", \"S3 7LS\")";
			stmt.executeUpdate(address1);
			stmt.executeUpdate(address2);
			
			String healthPlan1 = "INSERT INTO HealthPlans VALUES (\"hp1\", 123.2, 1, 2, 3)";
			String healthPlan2 = "INSERT INTO HealthPlans VALUES (\"hp2\", 122, 2, 3, 4)";
			stmt.executeUpdate(healthPlan1);
			stmt.executeUpdate(healthPlan2);
			
			String patient1 = "INSERT INTO Patients VALUES (1, \"Mr.\", \"la\", \"la\", '1997-02-12', \"02748593488\", \"Flat C43F\", \"S3 7LG\")";
			String patient2 = "INSERT INTO Patients VALUES (2, \"Dr.\", \"as\", \"as\", '1993-06-13', \"08648148426\", \"Apt. E42F\", \"S3 7LS\")";
			stmt.executeUpdate(patient1);
			stmt.executeUpdate(patient2);
			
			String appointment1 = "INSERT INTO Appointments (StartDate, EndDate, Username, Type, TotalAppointments, CurrentAppointment) VALUES ('2017-11-16 01:00:00', '2017-11-16 02:00:00', 'user1', 'Empty', 1, 1)";
			String appointment2 = "INSERT INTO Appointments VALUES ('2017-11-16 2:0:0', '2017-11-16 3:0:0', \"user1\", \"Remedial\", 2, \"a\", 1, 1)";
			String appointment3 = "INSERT INTO Appointments VALUES ('2017-11-16 3:0:0', '2017-11-16 4:0:0', \"user1\", \"Cleaning\", 1, \"b\", 1, 1)";
			String appointment4 = "INSERT INTO Appointments VALUES ('2017-11-16 4:0:0', '2017-11-16 5:0:0', \"user1\", \"Remedial\", 2, \"c\", 1, 1)";
			String appointment5 = "INSERT INTO Appointments VALUES ('2017-11-16 5:0:0', '2017-11-16 6:0:0', \"user1\", \"Cleaning\", 1, \"d\", 1, 1)";
			String appointment6 = "INSERT INTO Appointments VALUES ('2017-11-16 6:0:0', '2017-11-16 7:0:0', \"user1\", \"Checkup\", 2, \"e\", 1, 1)";
			String appointment7 = "INSERT INTO Appointments VALUES ('2017-11-16 7:0:0', '2017-11-16 8:0:0', \"user1\", \"Cleaning\", 1, \"f\", 1, 1)";
			String appointment8 = "INSERT INTO Appointments VALUES ('2017-11-16 8:0:0', '2017-11-16 9:0:0', \"user1\", \"Checkup\", 2, \"g\", 1, 1)";
			String appointment9 = "INSERT INTO Appointments VALUES ('2017-11-16 9:0:0', '2017-11-16 10:0:0', \"user1\", \"Remedial\", 1, \"h\", 1, 1)";
			String appointment10 = "INSERT INTO Appointments VALUES ('2017-11-16 10:0:0', '2017-11-16 11:0:0', \"user1\", \"Checkup\", 2, \"i\", 1, 1)";
			String appointment11 = "INSERT INTO Appointments VALUES ('2017-11-16 1:0:0', '2017-11-16 2:0:0', \"user2\", \"Checkup\", 1, \"j\", 1, 1)";
			String appointment12 = "INSERT INTO Appointments VALUES ('2017-11-16 2:0:0', '2017-11-16 3:0:0', \"user2\", \"Checkup\", 2, \"k\", 1, 1)";
			String appointment13 = "INSERT INTO Appointments VALUES ('2017-11-16 3:0:0', '2017-11-16 4:0:0', \"user2\", \"Checkup\", 1, \"l\", 1, 1)";
			String appointment14 = "INSERT INTO Appointments VALUES ('2017-11-16 4:0:0', '2017-11-16 5:0:0', \"user2\", \"Checkup\", 2, \"m\", 1, 1)";
			String appointment15 = "INSERT INTO Appointments VALUES ('2017-11-16 5:0:0', '2017-11-16 6:0:0', \"user2\", \"Checkup\", 1, \"n\", 1, 1)";
			String appointment16 = "INSERT INTO Appointments VALUES ('2017-11-16 6:0:0', '2017-11-16 7:0:0', \"user2\", \"Checkup\", 2, \"o\", 1, 1)";
			stmt.executeUpdate(appointment1);
			stmt.executeUpdate(appointment2);
			stmt.executeUpdate(appointment3);
			stmt.executeUpdate(appointment4);
			stmt.executeUpdate(appointment5);
			stmt.executeUpdate(appointment6);
			stmt.executeUpdate(appointment7);
			stmt.executeUpdate(appointment8);
			stmt.executeUpdate(appointment9);
			stmt.executeUpdate(appointment10);
			stmt.executeUpdate(appointment11);
			stmt.executeUpdate(appointment12);
			stmt.executeUpdate(appointment13);
			stmt.executeUpdate(appointment14);
			stmt.executeUpdate(appointment15);
			stmt.executeUpdate(appointment16);
			
			String patientHealthPlan1 = "INSERT INTO PatientHealthPlan VALUES (1, \"hp1\", 0, 0, 0, '2017-11-13')";
			stmt.executeUpdate(patientHealthPlan1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.closeDb(conn);
		}
	}

	/**
	 * createTables Function
	 * 
	 * This function creates tables in the Database as per the application's
	 * default database schema
	 * @throws SQLException if an error occurred whilst attempting connection
	 */
	private static void createTables() throws SQLException {
		//connect
		Connection conn = Database.getConnection();
		//tables schema
		String treatmentsTable = "CREATE TABLE `Treatments` (\r\n" + 
				"	`Name` VARCHAR(30) NOT NULL,\r\n" + 
				"	`Price` DOUBLE,\r\n" + 
				"	PRIMARY KEY(`Name`)\r\n" + 
				")";
		String appointmentsTable = "CREATE TABLE `Appointments` (\r\n" + 
				"	`StartDate`	TIMESTAMP NOT NULL,\r\n" +  //YYYY-MM-DD HH:MM:SS
				"	`EndDate`	TIMESTAMP NOT NULL,\r\n" + 
				"	`Username`	VARCHAR(15) NOT NULL,\r\n" + 
				"	`Type`	VARCHAR(30) NOT NULL,\r\n" + 
				"	`PatientID`	INTEGER,\r\n" + 
				"	`Notes`	VARCHAR(1000),\r\n" + 
				"	`TotalAppointments`	INTEGER,\r\n" + 
				"	`CurrentAppointment`	INTEGER,\r\n" + 
				"	PRIMARY KEY(`StartDate`,`Username`),\r\n" + 
				"	FOREIGN KEY(`Username`) REFERENCES Employees(Username),\r\n" + 
				"	FOREIGN KEY(`PatientID`) REFERENCES Patients(PatientID),\r\n" + 
				"   FOREIGN KEY(`Type`) REFERENCES AppointmentTypes(Name)" +
				")";
		String appointmentTreatmentTable = "CREATE TABLE `AppointmentTreatment` (\r\n" +
				"   `StartDate`     TIMESTAMP NOT NULL,\r\n" +
				"   `Username`      VARCHAR(15) NOT NULL,\r\n" +
				"   `TreatmentName` VARCHAR(30) NOT NULL,\r\n" +
				"   FOREIGN KEY(`StartDate`, `Username`) References Appointments(StartDate, Username),\r\n" +
				"   FOREIGN KEY(`TreatmentName`) References Treatments(Name)\r\n" +
				")";
		String appointmentTypesTable = "CREATE TABLE `AppointmentTypes` (\r\n" +
				"   `Name`      VARCHAR(30) NOT NULL,\r\n" +
				"   `Price`     DOUBLE,\r\n" +
				"   PRIMARY KEY(`Name`)\r\n" +
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
				"	`PatientID`	INTEGER,\r\n" + 
				"   `Title`     VARCHAR(4),\r\n" +
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
		try { //Run the above statements
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(treatmentsTable);
			stmt.executeUpdate(appointmentTypesTable);
			stmt.executeUpdate(employeesTable);
			stmt.executeUpdate(addressTable);
			stmt.executeUpdate(healthPlansTable);
			stmt.executeUpdate(patientsTable);
			stmt.executeUpdate(appointmentsTable);
			stmt.executeUpdate(patientHealthPlanTable);
			stmt.executeUpdate(appointmentTreatmentTable);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Database.closeDb(conn);
		}
	}
	
	/**
	 * dropTables Function
	 * 
	 * This is used to delete all tables relevant to this app from the database
	 * 
	 * @throws SQLException if an error occurred whilst attempting connection to the database
	 */
	private static void dropTables() throws SQLException {
		Connection conn = Database.getConnection();
		Statement stmt = conn.createStatement();

		try { //order is important for tables (reverse the creation order)
			String sqlString = "DROP TABLE IF EXISTS AppointmentTreatment, PatientHealthPlan, Appointments, Patients, HealthPlans, Address, Employees, AppointmentTypes, Treatments";
            stmt.executeUpdate(sqlString);
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            Database.closeDb(conn);
        }
	}
}
