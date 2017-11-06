/**
 * WelcommeScreen Class
 * 
 * This is the class representing welcome screen
 * in the setup wizard
 * @author John Ayad
 */
package com2002.setupwizard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com2002.DisplayFrame;
import com2002.interfaces.Screen;

public class WelcomeScreen implements Screen {

	/** Constant representing the title of the welcome screen **/
	final private static String WELCOME_TITLE = "Welcome";
	/** Constant representing the button label **/
	final private static String CONTINUE_BUTTON_LABEL = "Continue";

	//Instance variables
	private JPanel screen;
	private JLabel label;
	private JButton nextButton;
	private DisplayFrame frame;

    /**
     * Constructor
     * 
     * Used to create an instance of this class and initialize it
     * @param frame DisplayFrame in which this is to be shown
     */
	public WelcomeScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeWelcome();
	}

	/**
	 * Function used to initialize panel and components
	 */
	private void initializeWelcome() {
		this.screen = new JPanel();
	    this.screen.setLayout(new BorderLayout());
		//Title
		this.label = new JLabel(WELCOME_TITLE, SwingConstants.CENTER);
	    this.label.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE * 2));
	    this.screen.add(this.label, BorderLayout.NORTH);
	    //Button
	    this.nextButton = new JButton(CONTINUE_BUTTON_LABEL);
	    this.screen.add(this.nextButton, BorderLayout.SOUTH);
	    this.nextButton.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.nextButton.addActionListener(new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent arg0) {
	    	  //Take you to next panel and hide this one
	    	  EmployeesScreen employeesScreen = new EmployeesScreen(frame);
	    	  frame.setDisplayedPanel(employeesScreen.getPanel());
	    	  frame.repaint();
	      }
	    });
	}

	/**
	 * Function used to return panel representing this class
	 * @return JPanel showing its content
	 */
	public JPanel getPanel() {
		return this.screen;
	}
}
