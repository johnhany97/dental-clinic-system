/**
 * TreatmentsScreen Class
 * 
 * This is the class representing treatments creation
 * in the setup wizard
 * @author John Ayad
 */
package com2002.views.setupwizard;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import com2002.interfaces.Screen;
import com2002.utils.Database;
import com2002.views.DisplayFrame;

public class TreatmentsScreen implements Screen {
	
	/** Constant representing this screen's title and labels **/
	final private static String[] LABELS = {"Treatments", "Name", "Price"};
	/** Constant representing the next button label **/
	final private static String NEXT_BUTTON_LABEL = "Next";
	/** Constant representing the add more button label **/
	final private static String ADD_BUTTON_LABEL = "Add";

	//Instance variables
	private JPanel screen;
	private List<JLabel> labels;
	private List<JButton> buttons;
	private List<JPanel> panels;
	private List<JTextField> fields;
	private DisplayFrame frame;

    /**
     * Constructor
     * 
     * Used to create an instance of this class and initialize it
     * @param frame DisplayFrame in which this is to be shown
     */
	public TreatmentsScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeTreatments();
	}

	/**
	 * Function used to initialize panel and components
	 */
	private void initializeTreatments() {
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		//Title
		this.labels = new ArrayList<JLabel>();
		this.labels.add(new JLabel(LABELS[0], SwingConstants.CENTER));
		this.labels.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.screen.add(this.labels.get(0), BorderLayout.NORTH);
		// Treatments labels and text fields
		this.panels = new ArrayList<JPanel>();
		this.panels.add(new JPanel());
		this.panels.get(0).setLayout(new BorderLayout());
		//West panel
		this.panels.add(new JPanel());
		this.panels.get(1).setLayout(new BoxLayout(this.panels.get(1), BoxLayout.PAGE_AXIS));
		this.panels.get(0).add(this.panels.get(1), BorderLayout.WEST);
		this.panels.get(1).setBorder(new EmptyBorder(30, 30, 30, 30));
		//East panel
		this.panels.add(new JPanel());
		this.panels.get(2).setLayout(new BoxLayout(this.panels.get(2), BoxLayout.PAGE_AXIS));
		this.panels.get(0).add(this.panels.get(2), BorderLayout.EAST);
		this.panels.get(2).setBorder(new EmptyBorder(30, 30, 30, 30));
		//labels
		for (int i = 1; i < LABELS.length; i++) {
			this.labels.add(new JLabel(LABELS[i], SwingConstants.CENTER));
			int index = this.labels.size() - 1;
			this.labels.get(index).setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			if (i == 1) { 
				this.panels.get(1).add(this.labels.get(i));
			} else {
				this.panels.get(2).add(this.labels.get(i));
			}
		}
		this.screen.add(this.panels.get(0), BorderLayout.CENTER);
		//add textfields
		this.fields = new ArrayList<JTextField>();
		addTextFieldsTreatments();
		//add Button
	    this.buttons = new ArrayList<JButton>();
	    JPanel southPanel = new JPanel();
	    southPanel.setLayout(new FlowLayout());
	    this.buttons.add(new JButton(ADD_BUTTON_LABEL));
	    this.buttons.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.buttons.get(0).addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent arg0) {
		    	  addTextFieldsTreatments();
		    	  frame.revalidate();
		      }
	    });
	    southPanel.add(this.buttons.get(0));
		//next button
	    this.buttons.add(new JButton(NEXT_BUTTON_LABEL));
	    southPanel.add(this.buttons.get(1));
	    this.buttons.get(1).setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.buttons.get(1).addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent arg0) {
	    		Connection conn = null;
	    		try {
		    		conn = Database.getConnection();
					Statement stmt = conn.createStatement();
					for (int i = 0; i < fields.size(); i+=2) {
						String name = fields.get(i).getText();
						String price = fields.get(i + 1).getText();
						String sqlQuery = "INSERT INTO Treatments VALUES ('" + name + "', '" + price + "')";
						stmt.executeUpdate(sqlQuery);
					}
		    		AppointmentTypesScreen appointmentTypesScreen = new AppointmentTypesScreen(frame);
			    	frame.setDisplayedPanel(appointmentTypesScreen.getPanel());
			    	frame.repaint();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(frame,
							"Error with the database statement execution.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					if (conn != null) Database.closeDb(conn);
				}
	    	}
	    });
	    this.screen.add(southPanel, BorderLayout.SOUTH);
	}

	/**
	 * Function used to add a set of text fields and labels to the panels list
	 */
	private void addTextFieldsTreatments() {
		//Name textbox
		this.fields.add(new JTextField(20)); //create one
		int index = this.fields.size() - 1; //get it's index
		this.panels.get(1).add(this.fields.get(index)); //add to panel
		this.fields.get(index).setMaximumSize(this.fields.get(index).getPreferredSize()); //set max size
		//Price textbox
		//steps to ensure type of data entered
		NumberFormat numFormat = new DecimalFormat("#0.00"); //Format of data in price textfield
		NumberFormatter  numFormatter  = new NumberFormatter(numFormat);
		this.fields.add(new JFormattedTextField(numFormatter));
		int index2 = this.fields.size() - 1; //it's index;
		this.fields.get(index2).setColumns(20); //size (width)
		this.panels.get(2).add(this.fields.get(index2)); //add to panel
		this.fields.get(index2).setMaximumSize(this.fields.get(index).getPreferredSize()); //max size
	}

	/**
	 * Function used to return panel representing this class
	 * @return JPanel showing its content
	 */
	public JPanel getPanel() {
		return this.screen;
	}
}
