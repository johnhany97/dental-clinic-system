package com2002.tests;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com2002.utils.DatabaseTables;

public class ModelsTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DatabaseTables.setup();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DatabaseTables.setup();
	}

	@Test
	public void secretaryConstructNew() {
		fail("Not yet implemented");
	}
	
	@Test
	public void secretaryConstructExisting() {
		fail("Not yet implemented");
	}

}
