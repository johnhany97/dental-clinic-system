package com2002;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com2002.interfaces.Screen;
import com2002.models.Staff;

public class Login implements Screen {
	
	final private String TITLE = "Welcome";
	final private String[] LABELS = {"Username", "Password"};
	final private String LOGIN_LABEL = "Login";

	private JPanel screen;
	private List<JLabel> labels;
	private JButton loginButton;
	private List<Object> fields;
	private DisplayFrame frame;
	private JPanel middlePanel;
	
	public Login(DisplayFrame frame) {
		this.frame = frame;
		initializeLogin();
	}
	
	private void initializeLogin() {
		//Main panel
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		//Title
		this.labels = new ArrayList<JLabel>();
		this.labels.add(new JLabel(TITLE, SwingConstants.CENTER));
		this.labels.get(0).setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE * 2));
		this.screen.add(this.labels.get(0), BorderLayout.NORTH);
		//Middle panel
		this.middlePanel = new JPanel();
		this.middlePanel.setLayout(new GridLayout(2, 2));
		this.fields = new ArrayList<Object>();
		//Username & Password fields and labels
		for (int i = 0; i < LABELS.length; i++) {
			//label
			this.labels.add(new JLabel(LABELS[i], SwingConstants.CENTER));
			this.labels.get(i + 1).setFont(new Font("Sans Serif", Font.PLAIN, 
					DisplayFrame.FONT_SIZE / 2));
			//Add to middlePanel
			this.middlePanel.add(this.labels.get(i + 1));
			//field
			if (LABELS[i] == "Password") {
				JPasswordField passwordField = new JPasswordField(8);
				passwordField.setMaximumSize(passwordField.getPreferredSize()); //set max size
				passwordField.setEchoChar('#');
				this.fields.add(passwordField);
			} else {
				JTextField textField = new JTextField(8);
				textField.setMaximumSize(textField.getPreferredSize());
				this.fields.add(textField);
			}
			//Add to middlePanel
			this.middlePanel.add((Component) this.fields.get(i));
		}
		this.screen.add(this.middlePanel, BorderLayout.CENTER);
		//Login Button
		this.loginButton = new JButton(LOGIN_LABEL);
		this.loginButton.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Check username and password in db by creating an instance of staff
				String name = ((JTextField) fields.get(0)).getText();
				String password = String.valueOf(((JPasswordField) fields.get(1)).getPassword());
				//TODO: Attempt doctor and attempt secretary.. 
				//if not existing, give error alert message
				//if exists, take to appropriate view
			}
		});
		this.screen.add(this.loginButton, BorderLayout.SOUTH);
	}
	
	public JPanel getPanel() {
		return this.screen;
	}
}
