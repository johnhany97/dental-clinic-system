/**
 * EmployeesScreen Class
 * 
 * This is the class representing employees registration
 * in the setup wizard
 * @author John Ayad
 */
package com2002.views.setupwizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com2002.interfaces.Screen;
import com2002.models.Doctor;
import com2002.models.Role;
import com2002.models.Secretary;
import com2002.models.Staff;
import com2002.views.DisplayFrame;

public class EmployeesScreen implements Screen {
	
	/** Constant representing title and labels used in class **/
	final private static String[] LABELS = {"Employees", "First Name", "Last Name", "Username", "Password", "Role"};
	/** Constant representing button label **/
	final private static String NEXT_BUTTON_LABEL = "Next";
	
	//Instance Variables
	private JPanel screen;
	private List<JLabel> labels;
	private List<JPanel> panels;
    private List<Object> textFields;
    private List<JList> rolesLists;
	private JButton nextButton;
    private DisplayFrame frame;

    /**
     * Constructor
     * 
     * Used to create an instance of this class and initialize it
     * @param frame DisplayFrame in which this is to be shown
     */
	public EmployeesScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeEmployees();
	}

	/**
	 * Function used to initialize panel and components
	 */
	private void initializeEmployees() {
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		//Title
		this.labels = new ArrayList<JLabel>();
		this.labels.add(new JLabel(LABELS[0], SwingConstants.CENTER));
		this.labels.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.screen.add(this.labels.get(0), BorderLayout.NORTH);
		//Texts and inputs
		//We need three sets (For secretary, doctor & hygienist)
		this.panels = new ArrayList<JPanel>();
	    this.textFields = new ArrayList<Object>(); 
	    this.rolesLists = new ArrayList<JList>();
	    for (int i = 0; i < 3; i++) {
	    	addSetOfTextFieldsAndLabels();
	    }
	    JPanel centerPanel = new JPanel();
	    centerPanel.setLayout(new FlowLayout());
	    for (int i = 0; i < this.panels.size(); i++) {
	    	centerPanel.add(this.panels.get(i));
	    }
	    this.screen.add(centerPanel, BorderLayout.CENTER);
	    //Next Button
	    this.nextButton = new JButton(NEXT_BUTTON_LABEL);
	    this.screen.add(this.nextButton, BorderLayout.SOUTH);
	    this.nextButton.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.nextButton.addActionListener(new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent arg0) {
	    	  //Save data in db
	    	  int index = 0;
	    	  for (int i = 0; i < 3; i++) {
	    		  String firstName = ((JTextField) textFields.get(index)).getText();
	    		  index++;
	    		  String lastName = ((JTextField) textFields.get(index)).getText();
	    		  index++;
	    		  String username = ((JTextField) textFields.get(index)).getText();
	    		  index++;
	    		  String password = String.valueOf(((JPasswordField) textFields.get(index)).getPassword());
	    		  index++;
	    		  String role = (String) rolesLists.get(i).getSelectedValue();
	    		  Staff employee;
	    		  if (role.equals("Secretary")) {
	    			  try {
	    				  employee = new Secretary(firstName, lastName, username, password);
				    	  //Next screen
				    	  TreatmentsScreen treatmentsScreen = new TreatmentsScreen(frame);
				    	  frame.setDisplayedPanel(treatmentsScreen.getPanel());
				    	  frame.repaint();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame,
								"Error with the database statement execution.",
								"Error",
								JOptionPane.ERROR_MESSAGE);
					}
	    		  } else {
	    			  Role r = role.equals("Dentist") ? Role.DENTIST : Role.HYGIENIST;
	    			  try {
	    				  employee = new Doctor(firstName, lastName, username, password, r);
				    	  //Next screen
				    	  TreatmentsScreen treatmentsScreen = new TreatmentsScreen(frame);
				    	  frame.setDisplayedPanel(treatmentsScreen.getPanel());
				    	  frame.repaint();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame,
								"Error with the database statement execution.",
								"Error",
								JOptionPane.ERROR_MESSAGE);
					}
	    		  }
	    	  }
	      }
	    });
	}
	
	/**
	 * Function used to add fields and labels for each employee (3 is the current number)
	 */
	private void addSetOfTextFieldsAndLabels() {
		//Add a new panel
		this.panels.add(new JPanel());
		//Get it's index
		int panelIndex = this.panels.size() - 1;
		//Set layout
		this.panels.get(panelIndex).setLayout(new BoxLayout(this.panels.get(panelIndex), BoxLayout.PAGE_AXIS));
		
		for (int i = 1; i < LABELS.length; i++) { //Start from 1 because 0 is title of screen
			//Label of text field
			this.labels.add(new JLabel(LABELS[i], SwingConstants.LEFT));
			int index = this.labels.size() - 1;
			this.labels.get(index).setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			if (LABELS[i].equals("Password")) { //Password has hidden content
				JPasswordField passwordField = new JPasswordField(8);
				this.textFields.add(passwordField);
				passwordField.setEchoChar('#');
			} else if (LABELS[i].equals("Role")) { //role shows a list of options
				DefaultListModel<String> employeeRoles = new DefaultListModel<String>();
				employeeRoles.addElement("Secretary");
				employeeRoles.addElement("Dentist");
				employeeRoles.addElement("Hygienist");
				this.rolesLists.add(new JList(employeeRoles));
				int rolesIndex = this.rolesLists.size() - 1;
				this.rolesLists.get(rolesIndex).setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				this.rolesLists.get(rolesIndex).setSelectedIndex(0);
				this.rolesLists.get(rolesIndex).setVisibleRowCount(3);
			} else { //just a normal textfield
				this.textFields.add(new JTextField(8));
			}
			this.panels.get(panelIndex).add(this.labels.get(index));
			if (LABELS[i].equals("Password")) {
				this.panels.get(panelIndex).add((JPasswordField) this.textFields.get(this.textFields.size() - 1));
			} else if (LABELS[i].equals("Role")) {
				this.panels.get(panelIndex).add(this.rolesLists.get(this.rolesLists.size() - 1));
			} else {
				this.panels.get(panelIndex).add((JTextField) this.textFields.get(this.textFields.size() - 1));
			}
		}
	}
	
	/**
	 * Function used to return panel representing this class
	 * @return JPanel showing its content
	 */
	public JPanel getPanel() {
		return this.screen;
	}
	

}
