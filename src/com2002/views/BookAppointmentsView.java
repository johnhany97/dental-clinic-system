package com2002.views;

import javax.swing.JPanel;

import com2002.interfaces.Screen;
import com2002.models.Patient;

public class BookAppointmentsView implements Screen {
	
	private JPanel screen;
	private DisplayFrame frame;
	private Patient patient;
	private DisplayFrame parentFrame;
	
	public BookAppointmentsView(DisplayFrame frame, Patient patient, DisplayFrame parentFrame) {
		this.frame = frame;
		this.patient = patient;
		this.parentFrame = parentFrame;
		initialize();
	}
	
	private void initialize() {
		this.screen = new JPanel();
		
	}

	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
