/**
 * Application class
 * 
 * Class that starts the actual system
 * @author John Ayad
 */
package com2002;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com2002.utils.Database;
import com2002.utils.DatabaseTables;
import com2002.views.DisplayFrame;
import com2002.views.LoginView;

public class Application {
	
	//Instance variables
	private static DisplayFrame window;

	/**
	 * isSetup Function
	 * 
	 * Function used by application to check if the db has the required databases to run
	 * @return Boolean representing whether the DB is setup or not
	 */
	private static Boolean isSetup() {
		boolean flag = true;
		Connection conn = null;
	    try {
	    	conn = Database.getConnection();
	    	for (int i = 0; i < Database.TABLE_NAMES.length && flag; i++) {
	    		flag = flag && Database.dbHasTable(conn, Database.TABLE_NAMES[i]);
	    	}
			conn.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(window,
				    e.getMessage(),
				    "Error checking database",
				    JOptionPane.ERROR_MESSAGE);
		} finally {
		}
	    return flag;
	}
	
	/**
	 * Entry point for the program
	 *
	 * Checks if the db is setup, if it is, directs user to Login screen
	 * Otherwise, directs user to SetupWizard
	 */
	public static void main(String[] args) {
		window = new DisplayFrame();
		if (isSetup()) {
			//We're all setup.. go to login screen
			if (args.length != 0 && args[0].equals("dev")) { //Dev state, re-initialize the db everytime
				try {
					DatabaseTables.setup();
					DatabaseTables.populateTables();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(window,
						    e.getMessage(),
						    "Error setting up DB",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
			//LoginView instance
			LoginView loginScreen = new LoginView(window);
			window.setDisplayedPanel(loginScreen.getPanel());
		} else {
			//We need to setup the entire project
			try {
				DatabaseTables.setup();
				SetupWizard setupWizard = new SetupWizard(window);
				window.setDisplayedPanel(setupWizard.initialPanel());
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(window,
					    e.getMessage(),
					    "Error setting up DB",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
