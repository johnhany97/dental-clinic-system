package com2002.models;

import java.sql.SQLException;

public class Doctor extends Staff {

	public Doctor(String userN, String pass) throws SQLException {
		super(userN, pass);
	}
	
	public Doctor(String firstName, String lastName, String userN, String pass, Role r) {
		super(firstName, lastName, userN, pass, r);
	}

}
