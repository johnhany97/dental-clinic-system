package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com2002.utils.Database;

public class Address {
	
	private String houseNumber;
	private String streetName;
	private String district;
	private String city;
	private String postcode;
	
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
					+ city + "', '" + pcode + "')");
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
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
	
	private boolean dbHasAddress(String hNumber, String pcode) {
		String found_house = getData("HouseNumber", "Address", "HouseNumber", hNumber);
		String found_postcode = getData("Postcode", "Address", "Postcode", pcode);
		return found_house == hNumber && found_postcode == pcode;
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
	public static void main(String[] args) {
		Address nur = new Address("hosue", "house road", "yorkshire", "sheffield", "s10 3an");
		System.out.println(nur.getHouseNumber() + nur.getStreetName() + nur.getDistrict() + nur.getCity() + nur.getPostcode());
	}
	public String getHouseNumber(){
		return this.houseNumber;
	}
	public String getStreetName(){
		return this.streetName;
	}
	public String getDistrict(){
		return this.district;
	}
	public String getCity(){
		return this.houseNumber;
	}
	public String getPostcode() {
		return this.postcode;
	}
}