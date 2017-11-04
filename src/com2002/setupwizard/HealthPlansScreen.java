/**
 * HealthPlansScreen Class
 * 
 * This is the class representing health plans setting up
 * in the setup wizard
 * @author John Ayad
 */
package com2002.setupwizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import com2002.interfaces.Screen;
import com2002.models.HealthPlan;

public class HealthPlansScreen implements Screen {

	/** Constant representing labels and title of screen **/
	final private static String[] LABELS = {"Health Plans", "Name", "Price", "Checkup Level", "Hygiene Level", "Repair Level"};
	/** Constant representing label for next button **/
	final private static String NEXT_BUTTON_LABEL = "Next";
	/** Constant representing label for add more button **/
	final private static String ADD_BUTTON_LABEL = "Add";

	//instance variables
	private JPanel screen;
	private List<JLabel> labels;
	private List<JPanel> panels;
	private List<JTextField> fields;
	private List<JButton> buttons;
	private DisplayFrame frame;

	/**
	 * Constructor
	 * 
	 * Used to create and initialize an instance of this class
	 * @param frame DisplayFrame in which this class is to be shown
	 */
	public HealthPlansScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeHealthPlans();
	}

	/**
	 * Function used to initialize panel and it's components
	 */
	private void initializeHealthPlans() {
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		//Title
		this.labels = new ArrayList<JLabel>();
		this.labels.add(new JLabel(LABELS[0], SwingConstants.CENTER));
		this.labels.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.screen.add(this.labels.get(0), BorderLayout.NORTH);
		//Fields and their labels
		this.panels = new ArrayList<JPanel>();
		this.panels.add(new JPanel());
		this.panels.get(0).setLayout(new BoxLayout(this.panels.get(0), BoxLayout.PAGE_AXIS));
		this.fields = new ArrayList<JTextField>();
		addTextFieldsHealthPlans();
		this.screen.add(this.panels.get(0), BorderLayout.CENTER);
		//Buttons
		this.buttons = new ArrayList<JButton>();
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		//Add button
		this.buttons.add(new JButton(ADD_BUTTON_LABEL));
	    this.buttons.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.buttons.get(0).addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent arg0) {
		    	  addTextFieldsHealthPlans(); //Add more!
		    	  frame.revalidate();
		      }
	    });
		southPanel.add(this.buttons.get(0));
		//Next Button
		this.buttons.add(new JButton(NEXT_BUTTON_LABEL));
		this.buttons.get(1).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.buttons.get(1).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Store new stuff
				for (int i = 0; i < fields.size(); i+=5) {
					String name = fields.get(i).getText();
					String price = fields.get(i+1).getText();
					String checkUp = fields.get(i+2).getText();
					String hygiene = fields.get(i+3).getText();
					String repair = fields.get(i+4).getText();
					HealthPlan healthPlan = new HealthPlan(name, Double.valueOf(price), Integer.valueOf(checkUp), Integer.valueOf(hygiene), Integer.valueOf(repair));
				}
				//go to next screen
				ThanksScreen thanksScreen = new ThanksScreen(frame);
				frame.setDisplayedPanel(thanksScreen.getPanel());
			}
		});
		southPanel.add(this.buttons.get(1));
		this.screen.add(southPanel, BorderLayout.SOUTH);
	}

	/** 
	 * Function used to add a new set of inputs and labels
	 * for a health plan
	 */
	private void addTextFieldsHealthPlans() {
		NumberFormat numFormat = new DecimalFormat("#0.00"); //Format of data in textfield
		NumberFormatter numFormatter  = new NumberFormatter(numFormat);
		NumberFormat intFormat = new DecimalFormat("#0");
		NumberFormatter intFormatter = new NumberFormatter(intFormat);
		//Create the panel to store all of this
		this.panels.add(new JPanel());
		int panelIndex = this.panels.size() - 1;
		this.panels.get(panelIndex).setLayout(new FlowLayout());
		//Create fields with their labels
		for (int i = 1; i < LABELS.length; i++) {
			//Label
			this.labels.add(new JLabel(LABELS[i], SwingConstants.CENTER));
			int labelIndex = this.labels.size() - 1;
			this.labels.get(labelIndex).setFont(new Font("Sans Serif", Font.PLAIN, 
					DisplayFrame.FONT_SIZE / 2));
			//add it to panel
			this.panels.get(panelIndex).add(this.labels.get(labelIndex));
			//Field
			int fieldIndex;
			if (LABELS[i].equals("Name")) {
				//Normal textfield
				this.fields.add(new JTextField(8));
				fieldIndex = this.fields.size() - 1;
			} else if (LABELS[i].equals("Price")) {
				//Double
				this.fields.add(new JFormattedTextField(numFormatter));
				fieldIndex = this.fields.size() - 1;
				this.fields.get(fieldIndex).setColumns(10); //size (width)
			} else {
				//Integers
				this.fields.add(new JFormattedTextField(intFormatter));
				fieldIndex = this.fields.size() - 1;
				this.fields.get(fieldIndex).setColumns(10); //size (width)
			}
			//add it
			this.panels.get(panelIndex).add(this.fields.get(fieldIndex));
		}
		//Add panel to the bigger panel
		this.panels.get(0).add(this.panels.get(panelIndex));
	}

	/**
	 * Function used to return this screen's JPanel
	 * @return JPanel this class's panel
	 */
	public JPanel getPanel() {
		return this.screen;
	}	
}
