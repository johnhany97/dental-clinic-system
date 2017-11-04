package com2002.setupwizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com2002.DisplayFrame;
import com2002.interfaces.Screen;
import com2002.models.Doctor;
import com2002.models.Role;
import com2002.models.Secretary;
import com2002.models.Staff;

public class EmployeesScreen implements Screen {
	
	final private static String[] EMPLOYEE_LABELS = {"Employees", "First Name", "Last Name", "Username", "Password", "Role"};
	final private static String NEXT_BUTTON_LABEL = "Next";
	
	//Instance Variables
	private JPanel employeesScreen;
	private List<JLabel> employeesLabels;
	private List<JPanel> employeesPanels;
    private List<Object> employeesTextFields;
    private List<JList> employeesRolesLists;
	private JButton employeesButton;
    private DisplayFrame frame;

	public EmployeesScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeEmployees();
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
	    	  TreatmentsScreen treatmentsScreen = new TreatmentsScreen(frame);
	    	  frame.setDisplayedPanel(treatmentsScreen.getPanel());
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
	
	public JPanel getPanel() {
		return this.employeesScreen;
	}
	

}
