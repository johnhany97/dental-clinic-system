package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import com2002.utils.Database;

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

	public static ArrayList<Doctor> getAll() throws SQLException {
		Connection conn = null;
		ArrayList<Doctor> list = new ArrayList<Doctor>();
		try {
			conn = Database.getConnection();
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Employees", conn);
			while (rs.next()) {
				if (!rs.getString("Role").equals("Secretary")) {
					list.add(new Doctor(rs.getString("Username")));
				}
			}
		} finally {
			if (conn != null) conn.close();
		}
		return list;
	}
}
