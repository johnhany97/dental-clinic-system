package com2002.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com2002.models.Address;
import com2002.models.DBQueries;
import com2002.models.Doctor;
import com2002.models.HealthPlan;
import com2002.models.Patient;
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
			DBQueries.execUpdate("DELETE FROM Patients");
			DBQueries.execUpdate("DELETE FROM Address");
			DBQueries.execUpdate("DELETE FROM HealthPlans");
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
	
	// tests constructors for creating new type of address member
		@Test
		public void addressConstructNew() {
			try {
				Address address1 = new Address("57", "Mulgrave road", "Middlesex", "London", "W5 1LF");
				assertTrue("House number set to " + address1.getHouseNumber() + ", should be 57.", 
						address1.getHouseNumber().equals("57"));
				assertTrue("Street name set to " + address1.getStreetName() + ", should be Mulgrave road.", 
						address1.getStreetName().equals("Mulgrave road"));
				assertTrue("District set to " + address1.getDistrict() + ", should be middlesex.", 
						address1.getDistrict().equals("Middlesex"));
				assertTrue("City set to " + address1.getCity() + ", should be London.", 
						address1.getCity().equals("London"));
				assertTrue("Postcode set to " + address1.getPostcode() + ", should be London.", 
						address1.getPostcode().equals("W5 1LF"));
			} catch (SQLException e) {
				e.printStackTrace();
				fail("Exception thrown: " + e.getMessage());
			}
		}
		
		// tests constructor for searching existing entry in address
		@Test
		public void addressConstructExisting() {
			try {
				DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave Road', 'Middlesex', 'London', 'W5 1LF')");
				Address addressD = new Address("57", "W5 1LF");
				assertTrue("House number set to " + addressD.getHouseNumber() + ", should be 57.", 
						addressD.getHouseNumber().equals("57"));
				assertTrue("Street name set to " + addressD.getStreetName() + ", should be Mulgrave Road.", 
						addressD.getStreetName().equals("Mulgrave Road"));
				assertTrue("District set to " + addressD.getDistrict() + ", should be Middlesex.", 
						addressD.getDistrict().equals("Middlesex"));
				assertTrue("City set to " + addressD.getCity() + ", should be London.", 
						addressD.getCity().equals("London"));
				assertTrue("Postcode set to " + addressD.getPostcode() + ", should be W5 1LF.", 
						addressD.getPostcode().equals("W5 1LF"));
			} catch (SQLException e) {
				e.printStackTrace();
				fail("Exception thrown: " + e.getMessage());
			}
		}
		
		// tests address set methods
		@Test
		public void addressSetMethods() {
			try {
				DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
				Address addressDS = new Address("57", "W5 1LF");
				addressDS.setHouseNumber("67");
				addressDS.setStreetName("Lynwood Road");
				addressDS.setDistrict("South Yorkshire");
				addressDS.setCity("Sheffield");
				addressDS.setPostcode("S10 3AN");
				assertTrue("House number set to " + addressDS.getHouseNumber() + ", should be 67.", 
						addressDS.getHouseNumber().equals("67"));
				assertTrue("Street name set to " + addressDS.getStreetName() + ", should be Lynwood Road.", 
						addressDS.getStreetName().equals("Lynwood Road"));
				assertTrue("District set to " + addressDS.getDistrict() + ", should be South Yorkshire.", 
						addressDS.getDistrict().equals("South Yorkshire"));
				assertTrue("City set to " + addressDS.getCity() + ", should be Sheffield.", 
						addressDS.getCity().equals("Sheffield"));
				assertTrue("Postcode set to " + addressDS.getPostcode() + ", should be S10 3AN.", 
						addressDS.getPostcode().equals("S10 3AN"));
			} catch (SQLException e) {
				e.printStackTrace();
				fail("Exception thrown: " + e.getMessage());
			}
		}
		
		// tests constructors for creating new type of a Health Plan
		@Test
		public void healthPlanConstructNew() {
			try {
				HealthPlan healthPlan1 = new HealthPlan("NHS free plan", 0.00,  2, 2, 6);
				assertTrue("HealthPlan name set to " + healthPlan1.getName() + ", should be NHS free plan.", 
						healthPlan1.getName().equals("NHS free plan"));
				assertTrue("HealthPlan price set to " + healthPlan1.getPrice() + ", should be 0.00.", 
						healthPlan1.getPrice().equals(0.00));
				assertTrue("HealthPlan check up level set to " + healthPlan1.getCheckUpLevel() + ", should be 2.", 
						healthPlan1.getCheckUpLevel() == 2);
				assertTrue("HealthPlan hygiene level set to " + healthPlan1.getHygieneLevel() + ", should be 2.", 
						healthPlan1.getHygieneLevel() == 2);
				assertTrue("HealthPlan repair level set to " + healthPlan1.getRepairLevel() + ", should be 6.", 
						healthPlan1.getRepairLevel() == 6);			
				} catch (SQLException e) {
					e.printStackTrace();
					fail("Exception thrown: " + e.getMessage());
				}
		}
		

		// tests constructor for searching existing health plan
		@Test
		public void HealthPlanConstructExisting() {
			try {
				DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
				HealthPlan healthPlanD = new HealthPlan("NHS free plan");
				assertTrue("HealthPlan name set to " + healthPlanD.getName() + ", should be NHS free plan.", 
						healthPlanD.getName().equals("NHS free plan"));
				assertTrue("HealthPlan price set to " + healthPlanD.getPrice() + ", should be 0.00.", 
						healthPlanD.getPrice().equals(0.00));
				assertTrue("HealthPlan check up level set to " + healthPlanD.getCheckUpLevel() + ", should be 2.", 
						healthPlanD.getCheckUpLevel() == 2);
				assertTrue("HealthPlan hygiene level set to " + healthPlanD.getHygieneLevel() + ", should be 2.", 
						healthPlanD.getHygieneLevel() == 2);
				assertTrue("HealthPlan repair level set to " + healthPlanD.getRepairLevel() + ", should be 6.", 
						healthPlanD.getRepairLevel() == 6);			
			} catch (SQLException e) {
				e.printStackTrace();
				fail("Exception thrown: " + e.getMessage());
			}
		}
		
		// tests HealthPlan set methods
		@Test
		public void healthPlanSetMethods() {
			try {
				DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
				HealthPlan healthPlanDS = new HealthPlan("NHS free plan");
				healthPlanDS.setName("The maintenance plan");
				healthPlanDS.setPrice(15.00);
				healthPlanDS.setCheckUpLevel(2);
				healthPlanDS.setHygieneLevel(2);
				healthPlanDS.setRepairLevel(0);
				assertTrue("HealthPlan name set to " + healthPlanDS.getName() + ", should be The maintenance plan.", 
						healthPlanDS.getName().equals("The maintenance plan"));
				assertTrue("HealthPlan price set to " + healthPlanDS.getPrice() + ", should be 15.00.", 
						healthPlanDS.getPrice().equals(15.00));
				assertTrue("HealthPlan check up level set to " + healthPlanDS.getCheckUpLevel() + ", should be 2.", 
						healthPlanDS.getCheckUpLevel() == 2);
				assertTrue("HealthPlan hygiene level set to " + healthPlanDS.getHygieneLevel() + ", should be 2.", 
						healthPlanDS.getHygieneLevel() == 2);
				assertTrue("HealthPlan repair level set to " + healthPlanDS.getRepairLevel() + ", should be 0.", 
						healthPlanDS.getRepairLevel() == 0);				
			} catch (SQLException e) {
				e.printStackTrace();
				fail("Exception thrown: " + e.getMessage());
			}
		}
		
		// tests constructors for creating new type of Patient
		@Test
		public void PatientConstructNew() {
			try {
				DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
				Patient patient1 = new Patient("Nur", "Magid", LocalDate.of(1997, 05, 18) , "07543867024", "57", "W5 1LF");
				assertTrue("Patient name set to " + patient1.getFirstName() + ", should be Nur.", 
					patient1.getFirstName().equals("Nur"));
				assertTrue("Patient name set to " + patient1.getLastName() + ", should be Magid.", 
					patient1.getLastName().equals("Magid"));
				assertTrue("Patient set to " + patient1.getDateOfBirth() + ", should be 1997, 05, 18.", 
					patient1.getDateOfBirth().equals(LocalDate.of(1997, 05, 18)));
				assertTrue("Patient phone number set to " + patient1.getPhoneNumber() + ", should be 07543867024.", 
					patient1.getPhoneNumber().equals("07543867024"));
				assertTrue("Patient house number set to " + patient1.getHouseNumber() + ", should be 57.", 
					patient1.getHouseNumber().equals("57"));;	
				assertTrue("Patient phone number set to " + patient1.getPostcode() + ", should be W5 1LF.", 
					patient1.getPostcode().equals("W5 1LF"));
				} catch (SQLException e) {
					e.printStackTrace();
					fail("Exception thrown: " + e.getMessage());
			}		
		}
		
		// tests constructor for searching existing entry in address
		@Test
		public void patientConstructExisting() {
			try {
				DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave Road', 'Middlesex', 'London', 'W5 1LF')");
				DBQueries.execUpdate("INSERT INTO Patients VALUES (1, 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
				Patient patientD = new Patient("Nur", "57", "W5 1LF");
				assertTrue("Patient name set to " + patientD.getFirstName() + ", should be Nur.", 
					patientD.getFirstName().equals("Nur"));
				assertTrue("Patient name set to " + patientD.getLastName() + ", should be Magid.", 
					patientD.getLastName().equals("Magid"));
				assertTrue("Patient set to " + patientD.getDateOfBirth() + ", should be 1997, 05, 18.", 
					patientD.getDateOfBirth().equals(LocalDate.of(1997, 05, 18)));
				assertTrue("Patient phone number set to " + patientD.getPhoneNumber() + ", should be 07543867024.", 
					patientD.getPhoneNumber().equals("07543867024"));
				assertTrue("Patient house number set to " + patientD.getHouseNumber() + ", should be 57.", 
					patientD.getHouseNumber().equals("57"));;	
				assertTrue("Patient phone number set to " + patientD.getPostcode() + ", should be W5 1LF.", 
					patientD.getPostcode().equals("W5 1LF"));
			} catch (SQLException e) {
				e.printStackTrace();
				fail("Exception thrown: " + e.getMessage());
			}
		}
		
}
