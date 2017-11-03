package com2002.setupwizard;

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
import com2002.DisplayFrame;
import com2002.Login;

public class ThanksScreen implements Screen {
	
	final private static String START_APP_LABEL = "Start using Application";
	final private static String THANKS_TITLE = "<html><center>We're done!<br>Thanks :)</center></html>";

	private JPanel thanksScreen;
	private JLabel thanksLabel;
	private JButton thanksButton;
	private DisplayFrame frame;
	
	public ThanksScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeThanks();
	}
	
	private void initializeThanks() {
		this.thanksScreen = new JPanel();
		this.thanksScreen.setLayout(new BorderLayout());
		//Title
		this.thanksLabel = new JLabel(THANKS_TITLE, SwingConstants.CENTER);
		this.thanksLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE * 2));
		this.thanksScreen.add(this.thanksLabel, BorderLayout.NORTH);
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
		    	  Login loginScreen = new Login();
		    	  frame.setDisplayedPanel(loginScreen.getPanel());
		      }
	    });
	    this.thanksScreen.add(this.thanksButton, BorderLayout.SOUTH);
	}
	
	public JPanel getPanel() {
		return this.thanksScreen;
	}
}
