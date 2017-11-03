package com2002;

import javax.swing.JPanel;

import com2002.setupwizard.WelcomeScreen;

public class SetupWizard {
	
	//Instance variables
	WelcomeScreen welcomeScreen;
	DisplayFrame frame;
	
	public SetupWizard(DisplayFrame frame) {
		this.frame = frame;
		this.welcomeScreen = new WelcomeScreen(frame);
	}

	public JPanel initialPanel() {
		return this.welcomeScreen.getPanel();
	}
}
