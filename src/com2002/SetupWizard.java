/**
 * SetupWizard Class
 * 
 * Class that starts the Setup Wizard process
 * @author John Ayad
 */
package com2002;

import javax.swing.JPanel;

import com2002.views.DisplayFrame;
import com2002.views.setupwizard.WelcomeScreen;

public class SetupWizard {
	
	//Instance variables
	WelcomeScreen welcomeScreen;
	DisplayFrame frame;
	
	/**
	 * Constructor
	 * 
	 * Creates a new instance of the setup wizard
	 * @param frame frame in which wizard is to be shown
	 */
	public SetupWizard(DisplayFrame frame) {
		this.frame = frame;
		this.welcomeScreen = new WelcomeScreen(frame);
	}

	/**
	 * Function to return the first screen's JPanel
	 * @return
	 */
	public JPanel initialPanel() {
		return this.welcomeScreen.getPanel();
	}
}
