package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com2002.utils.Database;

public class Address {
	
	private String houseNumber;
	private String streetName;
	private String district;
	private String city;
	private String postcode;
	
	/**
	 * This constructor should be called when creating a new address 
	 * @param hNumber House Number of the address
	 * @param sName Street Name of the address
	 * @param dstrt District of the address
	 * @param cty City of the address
	 * @param pcode Postcode of the address 
	 */
	public Address(String hNumber, String sName, String dstrt, String cty, String pcode){
		try {
			Connection conn = Database.getConnection();
			houseNumber = hNumber;
			streetName = sName;
			district = dstrt;
			city = cty;
			postcode = pcode;
			if(!dbHasAddress(houseNumber, postcode)){
				DBQueries.execUpdate("INSERT INTO Address Values('" + hNumber + "', '" + sName + "', '" + dstrt + "', '" 
					+ cty + "', '" + pcode + "')");
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * This constructor should be called when searching for an address.
	 * @param hNumber House Number of the address.
	 * @param pcode Postcode of the address 
	 */
	public Address(String hNumber, String pcode) {
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Address WHERE HouseNumber = '" 
					+ hNumber + "' AND Postcode = '" + pcode + "'", conn);
			if(rs.next()) {
				houseNumber = rs.getString("HouseNumber");
				streetName = rs.getString("StreetName");
				district = rs.getString("District");
				city = rs.getString("City");
				postcode = rs.getString("Postcode");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks whether Address table contains a specified address.
	 * @param hNumber House Number of the address.
	 * @param pcode Postcode of the address 
	 * @return True if the address already exists.
	 */
	private boolean dbHasAddress(String hNumber, String pcode) {
		String found_house = DBQueries.getData("HouseNumber", "Address", "HouseNumber", hNumber);
		String found_postcode = DBQueries.getData("Postcode", "Address", "Postcode", pcode);
		return found_house == hNumber && found_postcode == pcode;
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
	 * Returns a House Number of a particular address.
	 * @return houseNumber The house number of an address.
	 */
	public String getHouseNumber(){
		return this.houseNumber;
	}
	
	/**
	 * Updates the House Number of an address to a given value/name.
	 * @param newHNumber The new house number of an address.
	 */
	protected void setHouseNumber(String newHNumber) {
		try {
			DBQueries.execUpdate("UPDATE Address SET HouseNumber = '" + newHNumber 
					+ "' WHERE StreetName = '" + streetName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("house number");
			return;
		}
		houseNumber = newHNumber;
	}
	
	/**
	 * Returns a Street Name of a particular address.
	 * @return streetName The street name of an address.
	 */
	public String getStreetName(){
		return this.streetName;
	}
	
	/**
	 * Updates the Street Name of an address.
	 * @param newStreetName The new street name of an address.
	 */
	protected void setStreetName(String newStreetName) {
		try {
			DBQueries.execUpdate("UPDATE Address SET StreetName = '" + newStreetName 
					+ "' WHERE StreetName = '" + streetName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("streetName");
			return;
		}
		streetName = newStreetName;
	}
	
	/**
	 * Returns a District of a particular address.
	 * @return district The district of an address.
	 */
	public String getDistrict(){
		return this.district;
	}
	
	/**
	 * Updates the District of an address.
	 * @param newDistrict The new district of an address.
	 */
	protected void setNewDistrict(String newDistrict) {
		try {
			DBQueries.execUpdate("UPDATE Address SET District = '" + newDistrict
					+ "' WHERE StreetName = '" + streetName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("district");
			return;
		}
		district = newDistrict;
	}
	
	/**
	 * Returns a City of a particular address.
	 * @return city The city of an address.
	 */
	public String getCity(){
		return this.houseNumber;
	}
	
	/**
	 * Updates the city of an address.
	 * @param newCity The new city of an address.
	 */
	protected void setCity(String newCity) {
		try {
			DBQueries.execUpdate("UPDATE Address SET City = '" + newCity 
					+ "' WHERE StreetName = '" + streetName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("city");
			return;
		}
		city = newCity;
	}
	
	/**
	 * Returns a Postcode of a particular address.
	 * @return postcode The postcode of an address.
	 */
	public String getPostcode() {
		return this.postcode;
	}
	
	/**
	 * Updates the postcode of an address.
	 * @param newPcode The new postcode of an address.
	 */
	protected void setPostcode(String newPostcode) {
		try {
			DBQueries.execUpdate("UPDATE Address SET PostCode = '" + newPostcode 
					+ "' WHERE StreetName = '" + streetName + "' AND HouseNumber = '" + houseNumber + "' AND Postcode = '" + postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("postcode");
			return;
		}
		postcode = newPostcode;
	}
	
	public static void main(String[] args) {
		Address nur = new Address("57", "Mulgrave road", "middlesex", "London", "w5 1lf");
		System.out.println(nur.getHouseNumber() + nur.getStreetName() + nur.getDistrict() + nur.getCity() + nur.getPostcode());
	}
	
}