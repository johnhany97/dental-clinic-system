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

	final private static String WELCOME_TITLE = "Welcome";
	final private static String CONTINUE_BUTTON_LABEL = "Continue";
	
	//Instance variables
	private JPanel welcomeScreen;
	private JLabel welcomeLabel;
	private JButton welcomeButton;
	private DisplayFrame frame;
	
	public WelcomeScreen(DisplayFrame frame) {
		this.frame = frame;
		initializeWelcome();
	}

	private void initializeWelcome() {
		this.welcomeScreen = new JPanel();
	    this.welcomeScreen.setLayout(new BorderLayout());
		//Title
		this.welcomeLabel = new JLabel(WELCOME_TITLE, SwingConstants.CENTER);
	    this.welcomeLabel.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE * 2));
	    this.welcomeScreen.add(this.welcomeLabel, BorderLayout.NORTH);
	    //Button
	    this.welcomeButton = new JButton(CONTINUE_BUTTON_LABEL);
	    this.welcomeScreen.add(this.welcomeButton, BorderLayout.SOUTH);
	    this.welcomeButton.setFont(new Font("Sans Serif", Font.PLAIN,
	            DisplayFrame.FONT_SIZE));
	    this.welcomeButton.addActionListener(new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent arg0) {
	    	  //Take you to next panel and hide this one
	    	  EmployeesScreen employeesScreen = new EmployeesScreen(frame);
	    	  frame.setDisplayedPanel(employeesScreen.getPanel());
	    	  frame.repaint();
	      }
	    });
	}
	
	public JPanel getPanel() {
		return this.welcomeScreen;
	}
}
