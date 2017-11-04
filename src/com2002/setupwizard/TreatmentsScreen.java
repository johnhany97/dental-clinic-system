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
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import com2002.DisplayFrame;
import com2002.interfaces.Screen;
import com2002.utils.Database;

public class TreatmentsScreen implements Screen {
	
	final private static String[] TREATMENTS_LABELS = {"Treatments", "Name", "Price"};
	final private static String NEXT_BUTTON_LABEL = "Next";
	final private static String ADD_MORE_BUTTON_LABEL = "Add";

	private JPanel treatmentsScreen;
	private List<JLabel> treatmentsLabels;
	private List<JButton> treatmentsButtons;
	private List<JPanel> treatmentsPanels;
	private List<JTextField> treatmentsFields;
	private DisplayFrame frame;

	public TreatmentsScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeTreatments();
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
	    		AppointmentTypesScreen appointmentTypesScreen = new AppointmentTypesScreen(frame);
		    	frame.setDisplayedPanel(appointmentTypesScreen.getPanel());
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
	
	public JPanel getPanel() {
		return this.treatmentsScreen;
	}
}
