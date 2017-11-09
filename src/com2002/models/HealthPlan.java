package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import com2002.models.DBQueries;
import com2002.utils.Database;


public class HealthPlan {

	private String name;
	private Double price;
	private int checkUpLevel;
	private int hygieneLevel;
	private int repairLevel;
	
	/**
	 * This constructor should be called when creating a new health treatment plan 
	 * @param name Name of the health plan 
	 * @param price Price of the health plan
	 * @param checkUpLevel check up numbers of treatments of the health plan
	 * @param hygieneLevel hygiene number of treatments of the health plan
	 * @param repairLevel repair work number of treatments of the health plan 
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws MySQLIntegrityConstraintViolationException if health plan name already exists
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	public HealthPlan(String name, Double price, int checkUpLevel, int hygieneLevel, int repairLevel) throws CommunicationsException, MySQLIntegrityConstraintViolationException, SQLException{
		this.name = name;
		this.price  = price;
		this.checkUpLevel = checkUpLevel;
		this.hygieneLevel = hygieneLevel;
		this.repairLevel = repairLevel;
		if(!dbHasHealthPlan(name)){
			DBQueries.execUpdate("INSERT INTO HealthPlans Values('" + name + "', '" + price + "', '" + checkUpLevel + "', '" 
				+ hygieneLevel + "', '" + repairLevel + "')");
		} else {
			throw new MySQLIntegrityConstraintViolationException("A HealthPlan with name " + name + " already exists.");
		}
	}
	
	/**
	 * This constructor should be called when finding a treatment plan
	 * @param name Name of treatment to be searched
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.	 */
	public HealthPlan(String name) throws CommunicationsException, SQLException {
		Connection conn = Database.getConnection();
		try {
			ResultSet rs = DBQueries.execQuery("SELECT * FROM HealthPlans WHERE  name = '" 
				+ name + "'", conn);
			if(rs.next()) {
				this.name = rs.getString("Name");
				this.price = rs.getDouble("Price");
				this.checkUpLevel = rs.getInt("CheckUpLevel");
				this.hygieneLevel = rs.getInt("HygieneLevel");
				this.repairLevel = rs.getInt("RepairLevel");
			}
		} finally {
			conn.close();
		}
	}
	
	/**
	 * Checks whether HealthPlan table contains a particular HealthPlan.
	 * @param name Name of the HealthPlan.
	 * @return True if the HealthPlan already exists.
	 */
	private boolean dbHasHealthPlan(String name) {
		String found_healthP = DBQueries.getData("Name", "HealthPlans", "Name", name);
		return found_healthP == name;
	}
	
	/**
	 * Returns a name of a particular health plan.
	 * @return name The name of the health plan.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Updates the name of a health plan to a given name.
	 * @param name The new name of a health plan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 * @throws MySQLIntegrityConstraintViolationException if health plan name already exists
	 */
	protected void setName(String name) throws CommunicationsException, SQLException, MySQLIntegrityConstraintViolationException {
		DBQueries.execUpdate("UPDATE HealthPlans SET Name = '" + name 
			+ "' WHERE Name = '" + this.name + "'");
		this.name = name;
	}

	/**
	 * Returns a price of a particular health plan.
	 * @return price The price of a health plan.
	 */
	public Double getPrice() {
		return this.price;
	}
	
	/**
	 * Updates the price of a health plan to a given value.
	 * @param price The new price of a HealthPlan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	protected void setPrice(double price) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE HealthPlans SET Price = '" + price 
			+ "' WHERE Name = '" + this.name + "'");
		this.price = price;
	}
	
	/**
	 * Returns a number of possible check up appointments of a HealthPlan.
	 * @return checkUpLevel The check up level of a HealthPlan.
	 */
	public int getCheckUpLevel() {
		return this.checkUpLevel;
	}
	
	/**
	 * Updates the checkUpLevel of a HealthPlan to a given value.
	 * @param checkUpLevel The new check up level of a HealthPlan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	protected void setCheckUpLevel(int checkUpLevel) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE HealthPlans SET CheckUpLevel = '" + checkUpLevel
			+ "' WHERE Name = '" + this.name + "'");
		this.checkUpLevel = checkUpLevel;
	}
	
	/**
	 * Returns a number of possible hygiene appointments of a health plan
	 * @return hygieneLevel The hygiene level of a health plan.
	 */
	public int getHygieneLevel() {
		return this.hygieneLevel;
	}
	
	/**
	 * Updates the hygiene level of an health plan to a given value.
	 * @param hygieneLevel The new hygiene level of a health plan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	protected void setHygieneLevel(int hygieneLevel) throws CommunicationsException, SQLException {
		DBQueries.execUpdate("UPDATE HealthPlans SET HygieneLevel = '" + hygieneLevel
			+ "' WHERE Name = '" + this.name + "'");
		this.hygieneLevel = hygieneLevel;
	}

	/**
	 * Returns a number of possible repair work appointments of a health plan
	 * @return repairLevel The repair work level of a health plan.
	 */
	public int getRepairLevel() {
		return this.repairLevel;
	}
	
	/**
	 * Updates the repair level of a health plan to a given value.
	 * @param repairLevel The new repair level of a health plan.
	 * @throws CommunicationsException when an error occurs whilst attempting connection
	 * @throws SQLException for any other error, could be incorrect parameters.
	 */
	protected void setRepairLevel(int repairLevel) throws CommunicationsException, SQLException  {
		DBQueries.execUpdate("UPDATE HealthPlans SET RepairLevel = '" + repairLevel
			+ "' WHERE Name = '" + this.name + "'");
		this.repairLevel = repairLevel;
	}
	
	public static void main(String[] args) {
		//HealthPlan nhs  = new HealthPlan("NHS free plan", 0.00,  2, 2, 6);
		//HealthPlan maintenance  = new HealthPlan("The maintenance plan", 15.00,  2, 2, 0);
		//HealthPlan oral  = new HealthPlan("The oral health plan", 21.00,  2, 4, 0);
		//HealthPlan dental  = new HealthPlan("The health plan", 26.00,  0, 0, 0);
		//HealthPlan find = new HealthPlan("NHS free plan");
		
		//dental.setName("The dental health plan");
		//dental.setPrice(36.00);
		//dental.setCheckUpLevel(2);
		//dental.setHygieneLevel(2);
		//dental.setRepairLevel(2);

		//System.out.println(nhs.getName() + nhs.getPrice() + nhs.getCheckUpLevel() + nhs.getHygieneLevel() + nhs.getRepairLevel());
		//System.out.println(maintenance.getName() + maintenance.getPrice() + maintenance.getCheckUpLevel() + maintenance.getHygieneLevel() + maintenance.getRepairLevel());
		//System.out.println(oral.getName() + oral.getPrice() + oral.getCheckUpLevel() + oral.getHygieneLevel() + oral.getRepairLevel());
		//System.out.println(dental.getName() + dental.getPrice() + dental.getCheckUpLevel() + dental.getHygieneLevel() + dental.getRepairLevel());
		//System.out.println(find.getName() + find.getPrice() + find.getCheckUpLevel() + find.getHygieneLevel() + find.getRepairLevel());
	}
	
}