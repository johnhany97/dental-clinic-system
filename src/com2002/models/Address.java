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
	 * @param houseNumber House Number of the address
	 * @param streetName Street Name of the address
	 * @param district District of the address
	 * @param city City of the address
	 * @param postcode Postcode of the address 
	 */
	public Address(String houseNumber, String streetName, String district, String city, String postcode){
		try {
			Connection conn = Database.getConnection();
			if(!dbHasAddress(houseNumber, postcode)){
				DBQueries.execUpdate("INSERT INTO Address Values('" + houseNumber + "', '" + streetName + "', '" + district + "', '" 
					+ city + "', '" + postcode + "')");
				this.houseNumber = houseNumber;
				this.streetName = streetName;
				this.district = district;
				this.city = city;
				this.postcode = postcode;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * This constructor should be called when searching for an address.
	 * @param houseNumber House Number of the address.
	 * @param postcode Postcode of the address 
	 */
	public Address(String houseNumber, String postcode) {
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Address WHERE HouseNumber = '" 
					+ houseNumber + "' AND Postcode = '" + postcode + "'", conn);
			if(rs.next()) {
				this.houseNumber = rs.getString("HouseNumber");
				this.streetName = rs.getString("StreetName");
				this.district = rs.getString("District");
				this.city = rs.getString("City");
				this.postcode = rs.getString("Postcode");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks whether Address table contains a specified address.
	 * @param houseNumber House Number of the address.
	 * @param postcode Postcode of the address 
	 * @return True if the address already exists.
	 */
	private boolean dbHasAddress(String houseNumber, String postcode) {
		String found_house = DBQueries.getData("HouseNumber", "Address", "HouseNumber", houseNumber);
		String found_postcode = DBQueries.getData("Postcode", "Address", "Postcode", postcode);
		return found_house == houseNumber && found_postcode == postcode;
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
	 * @param houseNumber The new house number of an address.
	 */
	public void setHouseNumber(String houseNumber) {
		try {
			DBQueries.execUpdate("UPDATE Address SET HouseNumber = '" + houseNumber 
					+ "' WHERE StreetName = '" + this.streetName + "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + this.postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("house number");
			return;
		}
		this.houseNumber = houseNumber;
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
	 * @param streetName The new street name of an address.
	 */
	public void setStreetName(String streetName) {
		try {
			DBQueries.execUpdate("UPDATE Address SET StreetName = '" + streetName 
					+ "' WHERE StreetName = '" + this.streetName + "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + this.postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("streetName");
			return;
		}
		this.streetName = streetName;
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
	 * @param district The new district of an address.
	 */
	public void setDistrict(String district) {
		try {
			DBQueries.execUpdate("UPDATE Address SET District = '" + district
					+ "' WHERE StreetName = '" + this.streetName + "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + this.postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("district");
			return;
		}
		this.district = district;
	}
	
	/**
	 * Returns a City of a particular address.
	 * @return city The city of an address.
	 */
	public String getCity(){
		return this.city;
	}
	
	/**
	 * Updates the city of an address.
	 * @param city The new city of an address.
	 */
	public void setCity(String city) {
		try {
			DBQueries.execUpdate("UPDATE Address SET City = '" + city 
					+ "' WHERE StreetName = '" + this.streetName + "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + this.postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("city");
			return;
		}
		this.city = city;
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
	 * @param postcode The new postcode of an address.
	 */
	public void setPostcode(String postcode) {
		try {
			DBQueries.execUpdate("UPDATE Address SET PostCode = '" + postcode 
					+ "' WHERE StreetName = '" + this.streetName + "' AND HouseNumber = '" + this.houseNumber + "' AND Postcode = '" + this.postcode +"'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("postcode");
			return;
		}
		this.postcode = postcode;
	}
	
	public static void main(String[] args) {
		Address nur = new Address("57", "Mulgrave road", "middlesex", "London", "w5 1lf");
		//nur.setHouseNumber("59");
		//nur.setStreetName("Lynwood Road");
		//nur.setDistrict("South Yorkshire");
		//nur.setCity("Sheffield");
		//nur.setPostcode("S10 3an");
		System.out.println(nur.getHouseNumber() + nur.getStreetName() + nur.getDistrict() + nur.getCity() + nur.getPostcode());
	}
	
}