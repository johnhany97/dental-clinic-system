package com2002;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class SetupWizard {
	
	final private static String[] EMPLOYEE_LABELS = {"Employees", "First Name", "Last Name", "Username", "Password", "Role"};
	final private static String[] TREATMENTS_LABELS = {"Treatments", "Name", "Price"};
	final private static String[] HEALTH_PLAN_LABELS = {"Health Plans", "Name", "Price", "Checkup Level", "Hygiene Level", "Repair Level"};
	final private static String NEXT_BUTTON_LABEL = "Next";
	final private static String WELCOME_TITLE = "Welcome";
	final private static String START_APP_LABEL = "Start using Application";
	final private static String ADD_MORE_BUTTON_LABEL = "Add";
	final private static String CONTINUE_BUTTON_LABEL = "Continue";
	
	//Instance variables
	private JPanel welcomeScreen;
	private JLabel welcomeLabel;
	private JButton welcomeButton;
	//EmployeesScreen
	private JPanel employeesScreen;
	private List<JLabel> employeesLabels;
	private List<JPanel> employeesPanels;
    private List<Object> employeesTextFields;
    private List<JList> employeesRolesLists;
	private JButton employeesButton;
	//TreatmentsScreen
	private JPanel treatmentsScreen;
	private JLabel[] treatmentsLabels;
	private JButton[] treatmentsButtons;
	//HealthPlansScreen
	private JPanel healthPlansScreen;
	private JLabel[] healthPlansLabels;
	private JButton[] healthPlansButtons;
	//Thanks
	private JPanel thanksScreen;
	private JLabel thanksLabel;
	private JButton thanksButton;
	private DisplayFrame frame;
	
	public SetupWizard(DisplayFrame frame) {
		this.frame = frame;
		initPanels();
	}
	
	public JPanel initialPanel() {
		return this.welcomeScreen;
	}
	
	private void initPanels() {
		initializeWelcome();
		initializeEmployees();
//		initializeTreatments();
//		initializeHealthPlans();
//		initializeThanks();
	}
	
	private void initializeThanks() {
		
	}

	private void initializeHealthPlans() {
		
	}

	private void initializeTreatments() {
		
	}

	private void initializeEmployees() {
		this.employeesScreen = new JPanel();
		this.employeesScreen.setLayout(new BorderLayout());
		//Title
		this.employeesLabels = new ArrayList<JLabel>();
		this.employeesLabels.add(new JLabel(EMPLOYEE_LABELS[0], SwingConstants.CENTER));
		this.employeesLabels.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.employeesScreen.add(this.employeesLabels.get(0), BorderLayout.NORTH);
		//Texts and inputs
		//We need three sets (For secretary, doctor & Hygienist)
		this.employeesPanels = new ArrayList<JPanel>();
	    this.employeesTextFields = new ArrayList<Object>();
	    this.employeesRolesLists = new ArrayList<JList>();
	    for (int i = 0; i < 3; i++) {
	    	addSetOfTextFieldsAndLabels();
	    }
	    JPanel centerPanel = new JPanel();
	    centerPanel.setLayout(new FlowLayout());
	    for (int i = 0; i < this.employeesPanels.size(); i++) {
	    	centerPanel.add(this.employeesPanels.get(i));
	    }
	    this.employeesScreen.add(centerPanel, BorderLayout.CENTER);
	    //Next Button
	    this.employeesButton = new JButton(NEXT_BUTTON_LABEL);
	    this.employeesScreen.add(this.employeesButton, BorderLayout.SOUTH);
	    this.employeesButton.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.employeesButton.addActionListener(new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent arg0) {
	        //Take you to next panel and hide this one
	    	frame.setDisplayedPanel(treatmentsScreen);
	    	frame.repaint();
	      }
	    });
	}
	
	private void addSetOfTextFieldsAndLabels() {
		this.employeesPanels.add(new JPanel());
		int panelIndex = this.employeesPanels.size() - 1;
		this.employeesPanels.get(panelIndex).setLayout(new BoxLayout(this.employeesPanels.get(panelIndex), BoxLayout.PAGE_AXIS));

		for (int i = 1; i < EMPLOYEE_LABELS.length; i++) { //Start from 1 because 0 is title of screen
			this.employeesLabels.add(new JLabel(EMPLOYEE_LABELS[i], SwingConstants.LEFT));
			int index = this.employeesLabels.size() - 1;
			this.employeesLabels.get(index).setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			if (EMPLOYEE_LABELS[i].equals("Password")) {
				JPasswordField passwordField = new JPasswordField(8);
				this.employeesTextFields.add(passwordField);
				passwordField.setEchoChar('#');
			} else if (EMPLOYEE_LABELS[i].equals("Role")) {
				DefaultListModel employeeRoles = new DefaultListModel();
				employeeRoles.addElement("Secretary");
				employeeRoles.addElement("Dentist");
				employeeRoles.addElement("Hygienist");
				this.employeesRolesLists.add(new JList(employeeRoles));
				int rolesIndex = this.employeesRolesLists.size() - 1;
				this.employeesRolesLists.get(rolesIndex).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				this.employeesRolesLists.get(rolesIndex).setSelectedIndex(0);
				this.employeesRolesLists.get(rolesIndex).setVisibleRowCount(3);
			} else {
				this.employeesTextFields.add(new JTextField(8));
			}
			this.employeesPanels.get(panelIndex).add(this.employeesLabels.get(index));
			if (EMPLOYEE_LABELS[i].equals("Password")) {
				this.employeesPanels.get(panelIndex).add((JPasswordField) this.employeesTextFields.get(this.employeesTextFields.size() - 1));
			} else if (EMPLOYEE_LABELS[i].equals("Role")) {
				this.employeesPanels.get(panelIndex).add(this.employeesRolesLists.get(this.employeesRolesLists.size() - 1));
			} else {
				this.employeesPanels.get(panelIndex).add((JTextField) this.employeesTextFields.get(this.employeesTextFields.size() - 1));
			}
		}
		
	}

	private void initializeWelcome() {
		this.welcomeScreen = new JPanel();
	    this.welcomeScreen.setLayout(new BorderLayout());
		//Title
		this.welcomeLabel = new JLabel(WELCOME_TITLE, SwingConstants.CENTER);
	    this.welcomeLabel.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE * 2));
	    System.out.println(DisplayFrame.FONT_SIZE * 2);
	    this.welcomeScreen.add(this.welcomeLabel, BorderLayout.NORTH);
	    //Button
	    this.welcomeButton = new JButton(CONTINUE_BUTTON_LABEL);
	    this.welcomeScreen.add(this.welcomeButton, BorderLayout.SOUTH);
	    this.welcomeButton.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.welcomeButton.addActionListener(new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent arg0) {
	        //Take you to next panel and hide this one
	    	frame.setDisplayedPanel(employeesScreen);
	    	frame.repaint();
	      }
	    });
	}
}
