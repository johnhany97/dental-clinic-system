package com2002;

import javax.swing.JPanel;

import com2002.interfaces.Screen;
import com2002.models.Doctor;

public class DoctorView implements Screen {

	private JPanel screen;
	private Doctor doctor;
	private DisplayFrame frame;
	
	public DoctorView(DisplayFrame frame, Doctor doctor) {
		this.frame = frame;
		this.doctor = doctor;
		initializeScreen();
	}

	private void initializeScreen() {
		this.screen = new JPanel();
	}
	
	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
