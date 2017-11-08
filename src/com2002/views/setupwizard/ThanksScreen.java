/**
 * AppointmentTypesScreen Class
 * 
 * This is the class representing appointment types setting up
 * in the setup wizard
 * @author John Ayad
 */
package com2002.views.setupwizard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com2002.Application;
import com2002.interfaces.Screen;
import com2002.views.DisplayFrame;
import com2002.views.LoginView;

public class ThanksScreen implements Screen {
	
	/** Constant representing Start App button label **/
	final private static String START_APP_LABEL = "Start using Application";
	/** Constant representing title of this screen **/
	final private static String THANKS_TITLE = "<html><center>We're done!<br>Thanks :)</center></html>";

	//Instance variables
	private JPanel screen;
	private JLabel title;
	private JButton thanksButton;
	private DisplayFrame frame;

	/**
	 * Constructor
	 * 
	 * Used to initialize and create a new instance of this class
	 * @param frame DisplayFrame in which this screen is to be shown
	 */
	public ThanksScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeThanks();
	}

	/**
	 * Function used to initialize panel and it's components
	 */
	private void initializeThanks() {
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		//Title
		this.title = new JLabel(THANKS_TITLE, SwingConstants.CENTER);
		this.title.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE * 2));
		this.screen.add(this.title, BorderLayout.NORTH);
		//Button
		this.thanksButton = new JButton(START_APP_LABEL);
		this.thanksButton.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.thanksButton.addActionListener(new ActionListener() {
		      @Override
		      public void actionPerformed(ActionEvent arg0) {
		    	  //update setupDone file
		    	  FileWriter fw;
		    	  try {
		    		  fw = new FileWriter(Application.FILE_NAME);
		    		  PrintWriter pw = new PrintWriter(fw);
			    	  pw.write(Application.SETUP);
			    	  pw.flush(); 
			    	  pw.close();
		    	  } catch (IOException e) {
		    		  // TODO Auto-generated catch block
		    		  e.printStackTrace();
		    	  }
		    	  //Take you to login screen
		    	  LoginView loginScreen = new LoginView(frame);
		    	  frame.setDisplayedPanel(loginScreen.getPanel());
		      }
	    });
	    this.screen.add(this.thanksButton, BorderLayout.SOUTH);
	}

	/**
	 * Function used to return this screen's JPanel
	 * @return JPanel this class's panel
	 */
	public JPanel getPanel() {
		return this.screen;
	}
}
