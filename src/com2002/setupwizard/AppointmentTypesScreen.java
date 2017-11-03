package com2002.setupwizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import com2002.DisplayFrame;
import com2002.utils.Database;

public class AppointmentTypesScreen implements Screen {
	
	final private static String[] APPOINTMENT_TYPES_LABELS = {"Appointment Types' Prices", "Checkup", "Cleaning", "Remedial", "Empty"};
	final private static String NEXT_BUTTON_LABEL = "Next";
	
	private JPanel appointmentTypesScreen;
	private List<JLabel> appointmentTypesLabels;
	private JButton appointmentTypesButton;
	private List<JPanel> appointmentTypesPanels;
	private List<JTextField> appointmentTypesFields;
	private DisplayFrame frame;
	
	public AppointmentTypesScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeAppointmentTypes();
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
	    		HealthPlansScreen healthPlansScreen = new HealthPlansScreen(frame);
		    	frame.setDisplayedPanel(healthPlansScreen.getPanel());
		    	frame.repaint();
	    	}
	    });
	}

	
	public JPanel getPanel() {
		return this.appointmentTypesScreen;
	}

}
