package com2002.models;

import java.sql.SQLException;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class Doctor extends Staff {
	
	public Doctor(String username) throws SQLException {
		super(username);
	}

	public Doctor(String username, String password) throws SQLException {
		super(username, password);
	}
	
	public Doctor(String firstName, String lastName, String username, String password, Role role) throws MySQLIntegrityConstraintViolationException, SQLException {
		super(firstName, lastName, username, password, role);
	}

}
