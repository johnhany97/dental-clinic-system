package com2002.models;

public class Doctor extends Staff {

	public Doctor(String userN, String pass) {
		super(userN, pass);
	}
	
	public Doctor(String firstName, String lastName, String userN, String pass, Role r) {
		super(firstName, lastName, userN, pass, r);
	}

}
