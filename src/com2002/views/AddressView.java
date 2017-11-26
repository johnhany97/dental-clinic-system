/**
 * AddressView class
 * 
 * Class that represents a view that shows an address' details
 * @author John Ayad
 */
package com2002.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.CommunicationsException;

import com2002.interfaces.Screen;
import com2002.models.Address;
import com2002.models.DBQueries;
import com2002.models.Patient;

public class AddressView implements Screen {
	
	//Instance variables
	private JPanel screen;
	private DisplayFrame frame;
	private Address address;
	private JLabel title;
	private DefaultTableModel patientsTableModel;
	private JTable patientsTable;
	
	/**
	 * Constructor that instantiates an instance of this class
	 * 
	 * @param frame DisplayFrame to show view in
	 * @param address Address to show details of
	 */
	public AddressView(DisplayFrame frame, Address address) {
		this.frame = frame;
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.address = address;
		try {
			initialize();
		} catch (CommunicationsException e) {
			JOptionPane.showMessageDialog(frame,
				    "Not connected to internet",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
				    e.getMessage(),
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Initialize Function
	 * 
	 * Function used to initialize View's components and actionlistener
	 * @throws Exception
	 */
	@SuppressWarnings({ "serial" })
	private void initialize() throws Exception {
		//Main Screen
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		//Title
		this.title = new JLabel("Address details", SwingConstants.CENTER);
		this.title.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.screen.add(this.title, BorderLayout.NORTH);
		//Details of given address
		String houseNumber = this.address.getHouseNumber();
		String streetName = this.address.getStreetName();
		String district = this.address.getDistrict();
		String city = this.address.getCity();
		String postcode = this.address.getPostcode();
		//Labels
		JLabel hNumTitle = new JLabel("House Number: ", SwingConstants.CENTER);
		JLabel sNameTitle = new JLabel("Street Name: ", SwingConstants.CENTER);
		JLabel disTitle = new JLabel("District: ", SwingConstants.CENTER);
		JLabel cityTitle = new JLabel("City: ", SwingConstants.CENTER);
		JLabel postTitle = new JLabel("Postcode: ", SwingConstants.CENTER);
		hNumTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		sNameTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		disTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		cityTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		postTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		//Actual data
		JLabel hNumLabel = new JLabel(houseNumber, SwingConstants.CENTER);
		JLabel sNameLabel = new JLabel(streetName, SwingConstants.CENTER);
		JLabel disLabel = new JLabel(district, SwingConstants.CENTER);
		JLabel cityLabel = new JLabel(city, SwingConstants.CENTER);
		JLabel postLabel = new JLabel(postcode, SwingConstants.CENTER);
		hNumLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		sNameLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		disLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		cityLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		postLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		//Data in the center
		JPanel center = new JPanel();
		center.setLayout(new GridLayout(0, 1));
		JPanel upperPart = new JPanel();
		upperPart.setLayout(new GridLayout(0, 2));
		//Add components to panels
		upperPart.add(hNumTitle);
		upperPart.add(hNumLabel);
		upperPart.add(sNameTitle);
		upperPart.add(sNameLabel);
		upperPart.add(disTitle);
		upperPart.add(disLabel);
		upperPart.add(cityTitle);
		upperPart.add(cityLabel);
		upperPart.add(postTitle);
		upperPart.add(postLabel);
		center.add(upperPart);
		//List of patients
		ArrayList<Patient> list = DBQueries.getPatientsByAddress(houseNumber, postcode);
		Object[][] listToUse = patientListConverter(list);
		String[] columnTitlesPatients = {"ID", "Title", "First Name", "Last Name", "Date Of Birth","Telephone", "Actions"};
		this.patientsTableModel = new DefaultTableModel(listToUse, columnTitlesPatients) {
			@Override
			public boolean isCellEditable(int row, int col) {
				//only the last column
				return col == 6;
			}
		};
		this.patientsTable = new JTable(this.patientsTableModel);
		this.patientsTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
		this.patientsTable.getColumn("Actions").setCellEditor(
		        new ButtonEditor(new JCheckBox(), this.frame));
		JScrollPane patientsScrollPane = new JScrollPane(patientsTable);
		this.patientsTable.setFillsViewportHeight(true);
		JPanel lowerPart = new JPanel();
		lowerPart.setLayout(new BorderLayout());
		JLabel patientsLabel = new JLabel("Patients in this address", SwingConstants.CENTER);
		patientsLabel.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE));
		lowerPart.add(patientsLabel, BorderLayout.NORTH);
		lowerPart.add(patientsScrollPane, BorderLayout.CENTER);
		center.add(lowerPart);
		this.screen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.screen.add(center, BorderLayout.CENTER);
	}
	
	/** 
	 * Function used to convert an ArrayList of patients to a 2D array
	 * needed by JTable component
	 * @param givenList ArrayList of patients
	 * @return 2D array of patient's details as strings
	 */
	private Object[][] patientListConverter(ArrayList<Patient> givenList) {
		Object[][] patientArr = new Object[givenList.size()][7];
		for (int i = 0; i < givenList.size(); i++) {
			patientArr[i] = new Object[7];
			patientArr[i][0] = String.valueOf(givenList.get(i).getPatientID());
			patientArr[i][1] = givenList.get(i).getTitle();
			patientArr[i][2] = givenList.get(i).getFirstName();
			patientArr[i][3] = givenList.get(i).getLastName();
			patientArr[i][4] = givenList.get(i).getDateOfBirth().toString();
			patientArr[i][5] = givenList.get(i).getPhoneNumber();
			patientArr[i][6] = "View";
		}
		return patientArr;
	}

	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
