package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com2002.models.DBQueries;
import com2002.utils.Database;

public class HealthPlan {

	private String name;
	private int price;
	private int checkUpLevel;
	private int hygieneLevel;
	private int repairWorkLevel;
	
	public HealthPlan(String nme, int pri, int chkUpLevel, int hygLevel, int rWorkLevel){
		try {
			Connection conn = Database.getConnection();
			name = nme;
			price  = pri;
			checkUpLevel = chkUpLevel;
			hygieneLevel = hygLevel;
			repairWorkLevel = rWorkLevel;
			if(!dbHasHealthPlan(nme)){
				DBQueries.execUpdate("INSERT INTO HealthPlans Values('" + nme + "', '" + pri + "', '" + chkUpLevel + "', '" 
					+ hygLevel + "', '" + rWorkLevel + "')");
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private boolean dbHasHealthPlan(String nme) {
		String found_healthP = getData("Name", "HealthPlans", "Name", nme);
		return found_healthP == nme;
	}
	
	private static String getData(String returnCol, String table, String selectCol, String selectData) {
		String data = "";
		try {
			Connection conn = Database.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT " + returnCol + " FROM " + table 
					+ " WHERE " + selectCol + " = '" + selectData + "'");
			if(rs.next()) {
				data = rs.getString(returnCol);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public int getCheckUpLevel() {
		return checkUpLevel;
	}

	public int getHygieneLevel() {
		return hygieneLevel;
	}

	public int getRepairWorkLevel() {
		return repairWorkLevel;
	}





	

	
	
	
}