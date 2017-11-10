package com2002.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com2002.models.DBQueries;
import com2002.models.Doctor;
import com2002.models.Role;
import com2002.models.Secretary;
import com2002.models.Staff;
import com2002.utils.DatabaseTables;

public class ModelsTests {

	// drops and creates tables again
	@BeforeClass
	public static void setUpBeforeClass() {
		try {
			DatabaseTables.setup();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("JUnit setup failed.");
		}
	}

	// drops and creates tables again
	@AfterClass
	public static void tearDownAfterClass() {
		try {
			DatabaseTables.setup();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("JUnit tear down failed.");
		}
	}

	// clears the necessary tables before each test.
	// **Must add call to delete entries from table for any table you use in your tests**
	@Before
	public void clearTables() {
		try {
			DBQueries.execUpdate("DELETE FROM Employees");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("JUnit clear tables failed.");
		}
	}
	
	// tests constructors for creating new type of each staff member
	@Test
	public void staffConstructNew() {
		try {
			Staff secretary = new Secretary("Arthur", "Granacher", "secretary", "password");
			Staff dentist = new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			Staff hygienist = new Doctor("Arthur", "Granacher", "hygienist", "password", Role.HYGIENIST);
			assertTrue("First name set to " + secretary.getFirstName() + ", should be Arthur.", 
					secretary.getFirstName().equals("Arthur"));
			assertTrue("Last name set to " + secretary.getLastName() + ", should be Granacher.", 
					secretary.getLastName().equals("Granacher"));
			assertTrue("Username set to " + secretary.getUsername() + ", should be ayjee.", 
					secretary.getUsername().equals("secretary"));
			assertTrue("Role of Secretary was set to " + secretary.getRole() + ".", 
					secretary.getRole().equals("Secretary"));
			assertTrue("Role of Dentist was set to " + dentist.getRole() + ".", 
					dentist.getRole().equals("Dentist"));
			assertTrue("Role of Hygienist was set to " + hygienist.getRole() + ".", 
					hygienist.getRole().equals("Hygienist"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}
	
	// tests constructor for existing entry in database
	@Test
	public void staffConstructExisting() {
		try {
			DBQueries.execUpdate("INSERT INTO Employees VALUES ('Arthur', 'Granacher', 'ayjee', 'password', 'Dentist')");
			Staff dentist = new Doctor("ayjee", "password");
			assertTrue("First name set to " + dentist.getFirstName() + ", should be Arthur.", 
					dentist.getFirstName().equals("Arthur"));
			assertTrue("Last name set to " + dentist.getLastName() + ", should be Granacher.", 
					dentist.getLastName().equals("Granacher"));
			assertTrue("Username set to " + dentist.getUsername() + ", should be ayjee.", 
					dentist.getUsername().equals("ayjee"));
			assertTrue("Role of Secretary was set to " + dentist.getRole() + ".", 
					dentist.getRole().equals("Dentist"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}
	
	// tests staff set methods
	@Test
	public void staffSetMethods() {
		try {
			DBQueries.execUpdate("INSERT INTO Employees VALUES ('Arthur', 'Granacher', 'ayjee', 'password', 'Hygienist')");
			Staff hygienist = new Doctor("ayjee", "password");
			hygienist.setFirstName("NewFirst");
			hygienist.setLastName("NewLast");
			hygienist.setUsername("newuser");
			hygienist.setRole(Role.DENTIST);
			assertTrue("First name set to " + hygienist.getFirstName() + ", should be NewFirst.", 
					hygienist.getFirstName().equals("NewFirst"));
			assertTrue("Last name set to " + hygienist.getLastName() + ", should be NewLast.", 
					hygienist.getLastName().equals("NewLast"));
			assertTrue("Username set to " + hygienist.getUsername() + ", should be newuser.", 
					hygienist.getUsername().equals("newuser"));
			assertTrue("Role set to " + hygienist.getRole() + ", should be Dentist.", 
					hygienist.getRole().equals("Dentist"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}
	
}
