/**
 * AppointmentTypesScreen Class
 * 
 * This is the class representing appointment types setting up
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
import javax.swing.text.NumberFormatter;

import com.mysql.jdbc.CommunicationsException;

import com2002.interfaces.Screen;
import com2002.utils.Database;
import com2002.views.DisplayFrame;

public class AppointmentTypesScreen implements Screen {

	/** Constant representing all of the input labels and the title **/
	final private static String[] LABELS = {"Appointment Types' Prices", "Checkup", "Cleaning", "Remedial", "Empty"};

	/** Constant representing the button's label **/
	final private static String NEXT_BUTTON_LABEL = "Next";

	//Instance variables
	private JPanel screen;
	private List<JLabel> labels;
	private JButton nextButton;
	private List<JPanel> panels;
	private List<JTextField> fields;
	private DisplayFrame frame;

	/**
	 * Constructor
	 * 
	 * Used to create an instance of this class
	 * @param frame the frame in which this panel will be shown
	 */
	public AppointmentTypesScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeAppointmentTypes();
	}

	/**
	 * Function used to initialize the screen and all of it's components
	 */
	private void initializeAppointmentTypes() {
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		//Title
		this.labels = new ArrayList<JLabel>();
		this.labels.add(new JLabel(LABELS[0], SwingConstants.CENTER));
		this.labels.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.screen.add(this.labels.get(0), BorderLayout.NORTH);
		//Add the 4 labels with their price fields
		this.panels = new ArrayList<JPanel>();
		this.panels.add(new JPanel());
		this.panels.get(0).setLayout(new BoxLayout(this.panels.get(0), BoxLayout.PAGE_AXIS));
		this.fields = new ArrayList<JTextField>();
		NumberFormat numFormat = new DecimalFormat("#0.0"); //Format of data in price textfield
		NumberFormatter  numFormatter  = new NumberFormatter(numFormat);
		for (int i = 1; i < LABELS.length; i++) {
			//Create a panel
			this.panels.add(new JPanel());
			int index = this.panels.size() - 1;
			this.panels.get(index).setLayout(new FlowLayout());
			//Create label
			this.labels.add(new JLabel(LABELS[i], SwingConstants.CENTER));
			int labelIndex = this.labels.size() - 1;
			this.labels.get(labelIndex).setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			//Create textfield
			this.fields.add(new JFormattedTextField(numFormatter));
			int fieldIndex = this.fields.size() - 1; //it's index;
			this.fields.get(fieldIndex).setColumns(20); //size (width)
			//add label and textfield to panel'
			this.panels.get(index).add(this.labels.get(labelIndex));
			this.panels.get(index).add(this.fields.get(fieldIndex));
			//add panel to bigger panel
			this.panels.get(0).add(this.panels.get(index));
		}
		this.screen.add(this.panels.get(0), BorderLayout.CENTER);
		//Next button
	    this.nextButton = new JButton(NEXT_BUTTON_LABEL);
	    this.screen.add(this.nextButton, BorderLayout.SOUTH);
	    this.nextButton.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.nextButton.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent arg0) {
	    		//Save data
	    		Connection conn = null;
	    		try {
		    		conn = Database.getConnection();
					Statement stmt = conn.createStatement();
					for (int i = 1; i < LABELS.length; i++) {
						String name = LABELS[i];
						String price = fields.get(i - 1).getText();
						String sqlQuery = "INSERT INTO AppointmentTypes VALUES ('" + name + "', '" + price + "')";
						stmt.executeUpdate(sqlQuery);
					}
		    		//Next screen
		    		HealthPlansScreen healthPlansScreen = new HealthPlansScreen(frame);
			    	frame.setDisplayedPanel(healthPlansScreen.getPanel());
			    	frame.repaint();
	    		} catch (CommunicationsException e) {
	    			JOptionPane.showMessageDialog(frame,
	    				    "Not connected to internet",
	    				    "Error",
	    				    JOptionPane.ERROR_MESSAGE);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(frame,
							e.getMessage(),
							"Error",
							JOptionPane.ERROR_MESSAGE);
				} finally {
					if (conn != null) Database.closeDb(conn);
				}
	    	}
	    });
	}

	/**
	 * Function used to return this screen's JPanel
	 * @return JPanel this class's panel
	 */
	public JPanel getPanel() {
		return this.screen;
	}
}
