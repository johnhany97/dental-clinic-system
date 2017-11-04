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
	
	final private static String[] HEALTH_PLANS_LABELS = {"Health Plans", "Name", "Price", "Checkup Level", "Hygiene Level", "Repair Level"};
	final private static String NEXT_BUTTON_LABEL = "Next";
	final private static String ADD_MORE_BUTTON_LABEL = "Add";
	
	private JPanel healthPlansScreen;
	private List<JLabel> healthPlansLabels;
	private List<JPanel> healthPlansPanels;
	private List<JTextField> healthPlansFields;
	private List<JButton> healthPlansButtons;
	private DisplayFrame frame;
	
	public HealthPlansScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeHealthPlans();
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
				ThanksScreen thanksScreen = new ThanksScreen(frame);
				frame.setDisplayedPanel(thanksScreen.getPanel());
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
	
	public JPanel getPanel() {
		return this.healthPlansScreen;
	}
	
}
