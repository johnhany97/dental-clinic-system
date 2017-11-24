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
	
	public static void main(String[] args) {
		window = new DisplayFrame();
		if (isSetup()) {
			//We're all setup.. go to login screen
			//DEV ONLY
			try {
				DatabaseTables.setup();
				DatabaseTables.populateTables();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//END DEV ONLY CODE
			LoginView loginScreen = new LoginView(window);
			window.setDisplayedPanel(loginScreen.getPanel());
		} else {
			//We need to setup the entire project
			try {
				DatabaseTables.setup();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SetupWizard setupWizard = new SetupWizard(window);
			window.setDisplayedPanel(setupWizard.initialPanel());
		}
	}
}
