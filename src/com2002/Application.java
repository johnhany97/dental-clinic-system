package com2002;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Application {
	
	/** Defines name of file being used to monitor whether the application is initially setup or not **/
	public final static String FILE_NAME = "setupDone.txt";

	/** Defines what the file should read if system is already setup **/
	public final static String SETUP = "1";
	
	//Instance variables
	private static DisplayFrame window;

	private static Boolean isSetup() {
	    String data = "";
	    try {
			data = new String(Files.readAllBytes(Paths.get(FILE_NAME)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return data.equals(SETUP);
	}
	
	public static void main(String[] args) {
		window = new DisplayFrame();
		if (isSetup()) {
			//We're all setup.. go to login screen
			Login loginScreen = new Login(window);
			window.setDisplayedPanel(loginScreen.getPanel());
		} else {
			//We need to setup the entire project
			SetupWizard setupWizard = new SetupWizard(window);
			window.setDisplayedPanel(setupWizard.initialPanel());
		}		
	}
}
