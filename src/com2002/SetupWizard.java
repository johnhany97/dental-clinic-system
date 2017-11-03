package com2002;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import com2002.models.Doctor;
import com2002.models.HealthPlan;
import com2002.models.Role;
import com2002.models.Secretary;
import com2002.models.Staff;
import com2002.utils.Database;

public class SetupWizard {
	
	final private static String[] EMPLOYEE_LABELS = {"Employees", "First Name", "Last Name", "Username", "Password", "Role"};
	final private static String[] TREATMENTS_LABELS = {"Treatments", "Name", "Price"};
	final private static String[] APPOINTMENT_TYPES_LABELS = {"Appointment Types' Prices", "Checkup", "Cleaning", "Remedial", "Empty"};
	final private static String[] HEALTH_PLANS_LABELS = {"Health Plans", "Name", "Price", "Checkup Level", "Hygiene Level", "Repair Level"};
	final private static String NEXT_BUTTON_LABEL = "Next";
	final private static String WELCOME_TITLE = "Welcome";
	final private static String START_APP_LABEL = "Start using Application";
	final private static String ADD_MORE_BUTTON_LABEL = "Add";
	final private static String CONTINUE_BUTTON_LABEL = "Continue";
	final private static String THANKS_TITLE = "<html><center>We're done!<br>Thanks :)</center></html>";
	
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
	private List<JLabel> treatmentsLabels;
	private List<JButton> treatmentsButtons;
	private List<JPanel> treatmentsPanels;
	private List<JTextField> treatmentsFields;
	//AppointmentTypes
	private JPanel appointmentTypesScreen;
	private List<JLabel> appointmentTypesLabels;
	private JButton appointmentTypesButton;
	private List<JPanel> appointmentTypesPanels;
	private List<JTextField> appointmentTypesFields;
	//HealthPlansScreen
	private JPanel healthPlansScreen;
	private List<JLabel> healthPlansLabels;
	private List<JPanel> healthPlansPanels;
	private List<JTextField> healthPlansFields;
	private List<JButton> healthPlansButtons;
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
		initializeTreatments();
		initializeAppointmentTypes();
		initializeHealthPlans();
		initializeThanks();
	}
	
	private void initializeThanks() {
		this.thanksScreen = new JPanel();
		this.thanksScreen.setLayout(new BorderLayout());
		//Title
		this.thanksLabel = new JLabel(THANKS_TITLE, SwingConstants.CENTER);
		this.thanksLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE * 2));
		this.thanksScreen.add(this.thanksLabel, BorderLayout.NORTH);
		//Button
		this.thanksButton = new JButton(START_APP_LABEL);
		this.thanksButton.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.thanksButton.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent arg0) {
		    	  //update setupDone file
		    	  FileWriter fw;
		    	  try {
		    		  fw = new FileWriter(Application.FILE_NAME);
		    		  PrintWriter pw = new PrintWriter(fw);
			    	  pw.write(Application.SETUP);
			    	  pw.flush(); 
			    	  pw.close();
		    	  } catch (IOException e) {
		    		  // TODO Auto-generated catch block
		    		  e.printStackTrace();
		    	  }
		    	  //Take you to login screen
		    	  Login loginScreen = new Login();
		    	  frame.setDisplayedPanel(loginScreen.getPanel());
		      }
	    });
	    this.thanksScreen.add(this.thanksButton, BorderLayout.SOUTH);
	}

	private void initializeHealthPlans() {
		this.healthPlansScreen = new JPanel();
		this.healthPlansScreen.setLayout(new BorderLayout());
		//Title
		this.healthPlansLabels = new ArrayList<JLabel>();
		this.healthPlansLabels.add(new JLabel(HEALTH_PLANS_LABELS[0], SwingConstants.CENTER));
		this.healthPlansLabels.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.healthPlansScreen.add(this.healthPlansLabels.get(0), BorderLayout.NORTH);
		//Fields and their labels
		this.healthPlansPanels = new ArrayList<JPanel>();
		this.healthPlansPanels.add(new JPanel());
		this.healthPlansPanels.get(0).setLayout(new BoxLayout(this.healthPlansPanels.get(0), BoxLayout.PAGE_AXIS));
		this.healthPlansFields = new ArrayList<JTextField>();
		addTextFieldsHealthPlans();
		this.healthPlansScreen.add(this.healthPlansPanels.get(0), BorderLayout.CENTER);
		//Buttons
		this.healthPlansButtons = new ArrayList<JButton>();
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		//Add button
		this.healthPlansButtons.add(new JButton(ADD_MORE_BUTTON_LABEL));
	    this.healthPlansButtons.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.healthPlansButtons.get(0).addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent arg0) {
		    	  addTextFieldsHealthPlans();
		    	  frame.revalidate();
		      }
	    });
		southPanel.add(this.healthPlansButtons.get(0));
		//Next Button
		this.healthPlansButtons.add(new JButton(NEXT_BUTTON_LABEL));
		this.healthPlansButtons.get(1).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.healthPlansButtons.get(1).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Store new stuff
				for (int i = 0; i < healthPlansFields.size(); i+=5) {
					String name = healthPlansFields.get(i).getText();
					String price = healthPlansFields.get(i+1).getText();
					String checkUp = healthPlansFields.get(i+2).getText();
					String hygiene = healthPlansFields.get(i+3).getText();
					String repair = healthPlansFields.get(i+4).getText();
					HealthPlan healthPlan = new HealthPlan(name, Double.valueOf(price), Integer.valueOf(checkUp), Integer.valueOf(hygiene), Integer.valueOf(repair));
				}
				//go to next screen
				frame.setDisplayedPanel(thanksScreen);
			}
		});
		southPanel.add(this.healthPlansButtons.get(1));
		this.healthPlansScreen.add(southPanel, BorderLayout.SOUTH);
	}
	
	private void addTextFieldsHealthPlans() {
		NumberFormat numFormat = new DecimalFormat("#0.00"); //Format of data in textfield
		NumberFormatter numFormatter  = new NumberFormatter(numFormat);
		NumberFormat intFormat = new DecimalFormat("#0");
		NumberFormatter intFormatter = new NumberFormatter(intFormat);
		//Create the panel to store all of this
		this.healthPlansPanels.add(new JPanel());
		int panelIndex = this.healthPlansPanels.size() - 1;
		this.healthPlansPanels.get(panelIndex).setLayout(new FlowLayout());
		//Create fields with their labels
		for (int i = 1; i < HEALTH_PLANS_LABELS.length; i++) {
			//Label
			this.healthPlansLabels.add(new JLabel(HEALTH_PLANS_LABELS[i], SwingConstants.CENTER));
			int labelIndex = this.healthPlansLabels.size() - 1;
			this.healthPlansLabels.get(labelIndex).setFont(new Font("Sans Serif", Font.PLAIN, 
					DisplayFrame.FONT_SIZE / 2));
			//add it to panel
			this.healthPlansPanels.get(panelIndex).add(this.healthPlansLabels.get(labelIndex));
			//Field
			int fieldIndex;
			if (HEALTH_PLANS_LABELS[i].equals("Name")) {
				//Normal textfield
				this.healthPlansFields.add(new JTextField(8));
				fieldIndex = this.healthPlansFields.size() - 1;
			} else if (HEALTH_PLANS_LABELS[i].equals("Price")) {
				//Double
				this.healthPlansFields.add(new JFormattedTextField(numFormatter));
				fieldIndex = this.healthPlansFields.size() - 1;
				this.healthPlansFields.get(fieldIndex).setColumns(10); //size (width)
			} else {
				//Integers
				this.healthPlansFields.add(new JFormattedTextField(intFormatter));
				fieldIndex = this.healthPlansFields.size() - 1;
				this.healthPlansFields.get(fieldIndex).setColumns(10); //size (width)
			}
			//add it
			this.healthPlansPanels.get(panelIndex).add(this.healthPlansFields.get(fieldIndex));
		}
		//Add panel to the bigger panel
		this.healthPlansPanels.get(0).add(this.healthPlansPanels.get(panelIndex));
	}
	
	private void initializeAppointmentTypes() {
		this.appointmentTypesScreen = new JPanel();
		this.appointmentTypesScreen.setLayout(new BorderLayout());
		//Title
		this.appointmentTypesLabels = new ArrayList<JLabel>();
		this.appointmentTypesLabels.add(new JLabel(APPOINTMENT_TYPES_LABELS[0], SwingConstants.CENTER));
		this.appointmentTypesLabels.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.appointmentTypesScreen.add(this.appointmentTypesLabels.get(0), BorderLayout.NORTH);
		//Add the 4 labels with their price fields
		this.appointmentTypesPanels = new ArrayList<JPanel>();
		this.appointmentTypesPanels.add(new JPanel());
		this.appointmentTypesPanels.get(0).setLayout(new BoxLayout(this.appointmentTypesPanels.get(0), BoxLayout.PAGE_AXIS));
		this.appointmentTypesFields = new ArrayList<JTextField>();
		NumberFormat numFormat = new DecimalFormat("#0.0"); //Format of data in price textfield
		NumberFormatter  numFormatter  = new NumberFormatter(numFormat);
		for (int i = 1; i < APPOINTMENT_TYPES_LABELS.length; i++) {
			//Create a panel
			this.appointmentTypesPanels.add(new JPanel());
			int index = this.appointmentTypesPanels.size() - 1;
			this.appointmentTypesPanels.get(index).setLayout(new FlowLayout());
			//Create label
			this.appointmentTypesLabels.add(new JLabel(APPOINTMENT_TYPES_LABELS[i], SwingConstants.CENTER));
			int labelIndex = this.appointmentTypesLabels.size() - 1;
			this.appointmentTypesLabels.get(labelIndex).setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			//Create textfield
			this.appointmentTypesFields.add(new JFormattedTextField(numFormatter));
			int fieldIndex = this.appointmentTypesFields.size() - 1; //it's index;
			this.appointmentTypesFields.get(fieldIndex).setColumns(20); //size (width)
			//add label and textfield to panel'
			this.appointmentTypesPanels.get(index).add(this.appointmentTypesLabels.get(labelIndex));
			this.appointmentTypesPanels.get(index).add(this.appointmentTypesFields.get(fieldIndex));
			//add panel to bigger panel
			this.appointmentTypesPanels.get(0).add(this.appointmentTypesPanels.get(index));
		}
		this.appointmentTypesScreen.add(this.appointmentTypesPanels.get(0), BorderLayout.CENTER);
		//Next button
	    this.appointmentTypesButton = new JButton(NEXT_BUTTON_LABEL);
	    this.appointmentTypesScreen.add(this.appointmentTypesButton, BorderLayout.SOUTH);
	    this.appointmentTypesButton.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.appointmentTypesButton.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent arg0) {
	    		try {
		    		Connection conn = Database.getConnection();
					Statement stmt = conn.createStatement();
					for (int i = 1; i < APPOINTMENT_TYPES_LABELS.length; i++) {
						String name = APPOINTMENT_TYPES_LABELS[i];
						String price = appointmentTypesFields.get(i - 1).getText();
						String sqlQuery = "INSERT INTO AppointmentTypes VALUES ('" + name + "', '" + price + "')";
						stmt.executeUpdate(sqlQuery);
					}
					Database.closeDb(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    	frame.setDisplayedPanel(healthPlansScreen);
		    	frame.repaint();
	    	}
	    });
	}

	private void initializeTreatments() {
		this.treatmentsScreen = new JPanel();
		this.treatmentsScreen.setLayout(new BorderLayout());
		//Title
		this.treatmentsLabels = new ArrayList<JLabel>();
		this.treatmentsLabels.add(new JLabel(TREATMENTS_LABELS[0], SwingConstants.CENTER));
		this.treatmentsLabels.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.treatmentsScreen.add(this.treatmentsLabels.get(0), BorderLayout.NORTH);
		// Treatments labels and text fields
		this.treatmentsPanels = new ArrayList<JPanel>();
		this.treatmentsPanels.add(new JPanel());
		this.treatmentsPanels.get(0).setLayout(new BorderLayout());
		//West panel
		this.treatmentsPanels.add(new JPanel());
		this.treatmentsPanels.get(1).setLayout(new BoxLayout(this.treatmentsPanels.get(1), BoxLayout.PAGE_AXIS));
		this.treatmentsPanels.get(0).add(this.treatmentsPanels.get(1), BorderLayout.WEST);
		this.treatmentsPanels.get(1).setBorder(new EmptyBorder(30, 30, 30, 30));
		//East panel
		this.treatmentsPanels.add(new JPanel());
		this.treatmentsPanels.get(2).setLayout(new BoxLayout(this.treatmentsPanels.get(2), BoxLayout.PAGE_AXIS));
		this.treatmentsPanels.get(0).add(this.treatmentsPanels.get(2), BorderLayout.EAST);
		this.treatmentsPanels.get(2).setBorder(new EmptyBorder(30, 30, 30, 30));
		//labels
		for (int i = 1; i < TREATMENTS_LABELS.length; i++) {
			this.treatmentsLabels.add(new JLabel(TREATMENTS_LABELS[i], SwingConstants.CENTER));
			int index = this.treatmentsLabels.size() - 1;
			this.treatmentsLabels.get(index).setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			if (i == 1) { 
				this.treatmentsPanels.get(1).add(this.treatmentsLabels.get(i));
			} else {
				this.treatmentsPanels.get(2).add(this.treatmentsLabels.get(i));
			}
		}
		this.treatmentsScreen.add(this.treatmentsPanels.get(0), BorderLayout.CENTER);
		//add textfields
		this.treatmentsFields = new ArrayList<JTextField>();
		addTextFieldsTreatments();
		//add Button
	    this.treatmentsButtons = new ArrayList<JButton>();
	    JPanel southPanel = new JPanel();
	    southPanel.setLayout(new FlowLayout());
	    this.treatmentsButtons.add(new JButton(ADD_MORE_BUTTON_LABEL));
	    this.treatmentsButtons.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.treatmentsButtons.get(0).addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent arg0) {
		    	  addTextFieldsTreatments();
		    	  frame.revalidate();
		      }
	    });
	    southPanel.add(this.treatmentsButtons.get(0));
		//next button
	    this.treatmentsButtons.add(new JButton(NEXT_BUTTON_LABEL));
	    southPanel.add(this.treatmentsButtons.get(1));
	    this.treatmentsButtons.get(1).setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.treatmentsButtons.get(1).addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent arg0) {
	    		try {
		    		Connection conn = Database.getConnection();
					Statement stmt = conn.createStatement();
					for (int i = 0; i < treatmentsFields.size(); i+=2) {
						String name = treatmentsFields.get(i).getText();
						String price = treatmentsFields.get(i + 1).getText();
						String sqlQuery = "INSERT INTO Treatments VALUES ('" + name + "', '" + price + "')";
						stmt.executeUpdate(sqlQuery);
					}
					Database.closeDb(conn);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    	frame.setDisplayedPanel(appointmentTypesScreen);
		    	frame.repaint();
	    	}
	    });
	    this.treatmentsScreen.add(southPanel, BorderLayout.SOUTH);
	}

	private void addTextFieldsTreatments() {
		//Name textbox
		this.treatmentsFields.add(new JTextField(20)); //create one
		int index = this.treatmentsFields.size() - 1; //get it's index
		this.treatmentsPanels.get(1).add(this.treatmentsFields.get(index)); //add to panel
		this.treatmentsFields.get(index).setMaximumSize(this.treatmentsFields.get(index).getPreferredSize()); //set max size
		//Price textbox
		//steps to ensure type of data entered
		NumberFormat numFormat = new DecimalFormat("#0.00"); //Format of data in price textfield
		NumberFormatter  numFormatter  = new NumberFormatter(numFormat);
		this.treatmentsFields.add(new JFormattedTextField(numFormatter));
		int index2 = this.treatmentsFields.size() - 1; //it's index;
		this.treatmentsFields.get(index2).setColumns(20); //size (width)
		this.treatmentsPanels.get(2).add(this.treatmentsFields.get(index2)); //add to panel
		this.treatmentsFields.get(index2).setMaximumSize(this.treatmentsFields.get(index).getPreferredSize()); //max size
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
	    	  int index = 0;
	    	  for (int i = 0; i < 3; i++) {
	    		  String firstName = ((JTextField) employeesTextFields.get(index)).getText();
	    		  index++;
	    		  String lastName = ((JTextField) employeesTextFields.get(index)).getText();
	    		  index++;
	    		  String username = ((JTextField) employeesTextFields.get(index)).getText();
	    		  index++;
	    		  String password = String.valueOf(((JPasswordField) employeesTextFields.get(index)).getPassword());
	    		  index++;
	    		  String role = (String) employeesRolesLists.get(i).getSelectedValue();
	    		  Staff employee;
	    		  if (role.equals("Secretary")) {
	    			  employee = new Secretary(firstName, lastName, username, password);
	    		  } else {
	    			  Role r = role.equals("Dentist") ? Role.DENTIST : Role.HYGIENIST;
	    			  employee = new Doctor(firstName, lastName, username, password, r);
	    		  }
	    	  }
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
				DefaultListModel<String> employeeRoles = new DefaultListModel<String>();
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
