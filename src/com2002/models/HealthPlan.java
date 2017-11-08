package com2002.models;

import java.sql.Connection;
import java.sql.SQLException;

import com2002.models.DBQueries;
import com2002.utils.Database;

public class HealthPlan {

	private String name;
	private Double price;
	private int checkUpLevel;
	private int hygieneLevel;
	private int repairWorkLevel;
	
	/**
	 * This constructor should be called when creating a new health treatment plan 
	 * @param nme Name of the health plan 
	 * @param pri Price of the health plan
	 * @param chkUpLevel check up numbers of treatments of the health plan
	 * @param hygLevel hygiene number of treatments of the health plan
	 * @param rWorkLevel repair work number of treatments of the health plan 
	 */
	public HealthPlan(String nme, Double pri, int chkUpLevel, int hygLevel, int rWorkLevel){
		try {
			Connection conn = Database.getConnection();
			this.name = nme;
			this.price  = pri;
			this.checkUpLevel = chkUpLevel;
			this.hygieneLevel = hygLevel;
			this.repairWorkLevel = rWorkLevel;
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
	
	/**
	 * Checks whether HealthPlan table contains a particular HealthPlan.
	 * @param nme Name of the HealthPlan.
	 * @return True if the HealthPlan already exists.
	 */
	private boolean dbHasHealthPlan(String nme) {
		String found_healthP = DBQueries.getData("Name", "HealthPlans", "Name", nme);
		return found_healthP == nme;
	}
	
	/**
	 * Method for printing error message to the console.
	 * @param method The method from which the error has occurred.
	 */
	private void printError(String method) {
		System.out.println("Something went wrong with updating the " + method + ". "
				+ "The staff member may have not been initialised properly "
				+ "(some instance variables might be null).");
	}
	
	/**
	 * Returns a Name of a particular HealthPlan.
	 * @return name The name of the HealthPlan.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Updates the Name of a HealthPlan to a given name.
	 * @param newName The new name of a HealthPlan.
	 */
	protected void setTreatmentName(String newName) {
		try {
			DBQueries.execUpdate("UPDATE HealthPlans SET Name = '" + newName 
					+ "' WHERE Name = '" + name + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("treatment name");
			return;
		}
		name = newName;
	}

	/**
	 * Returns a Price of a particular HealthPlan.
	 * @return price The price of a HealthPlan.
	 */
	public Double getPrice() {
		return this.price;
	}
	
	/**
	 * Updates the Price of a HealthPlan to a given value.
	 * @param newPrice The new price of a HealthPlan.
	 */
	protected void setPrice(double newPrice) {
		try {
			DBQueries.execUpdate("UPDATE HealthPlans SET Price = '" + newPrice 
					+ "' WHERE Name = '" + name + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("price");
			return;
		}
		price = newPrice;
	}
	
	/**
	 * Returns a Number of possible check up appointments of a HealthPlan.
	 * @return checkUpLevel The check up level of a HealthPlan.
	 */
	public int getCheckUpLevel() {
		return this.checkUpLevel;
	}
	
	/**
	 * Updates the checkUpLevel of a HealthPlan to a given value.
	 * @param newCheckUpLevel The new check up level of a HealthPlan.
	 */
	protected void setCheckUpLevel(int newCheckUpLevel) {
		try {
			DBQueries.execUpdate("UPDATE HealthPlans SET CheckUpLevel = '" + newCheckUpLevel
					+ "' WHERE Name = '" + name + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("checkup");
			return;
		}
		checkUpLevel = newCheckUpLevel;
	}
	
	/**
	 * Returns a Number of possible hygiene appointments of a HealthPlan
	 * @return hygieneLevel The hygiene level of a HealthPlan.
	 */
	public int getHygieneLevel() {
		return this.hygieneLevel;
	}
	
	/**
	 * Updates the Hygiene level of an HealthPlan to a given value.
	 * @param newHygieneLevel The new hygiene level of a HealthPlan.
	 */
	protected void setHygieneLevel(int newHygieneLevel) {
		try {
			DBQueries.execUpdate("UPDATE HealthPlans SET HygieneLevel = '" + newHygieneLevel
					+ "' WHERE Name = '" + name + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("hygiene");
			return;
		}
		hygieneLevel = newHygieneLevel;
	}

	/**
	 * Returns a Number of possible repair work appointments of a HealthPlan
	 * @return repairWorkLevel The repair work level of a HealthPlan.
	 */
	public int getRepairWorkLevel() {
		return this.repairWorkLevel;
	}
	
	
	/**
	 * Updates the Repair Work Level of a HealthPlan to a given value.
	 * @param newRepairLevel The new Repair level of a HealthPlan.
	 */
	protected void setRepairWorkLevel(int newRepairLevel) {
		try {
			DBQueries.execUpdate("UPDATE HealthPlans SET RepairLevel = '" + newRepairLevel
					+ "' WHERE Name = '" + name + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("repair");
			return;
		}
		repairWorkLevel = newRepairLevel;
	}






	

	
	
	
}