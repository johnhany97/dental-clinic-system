package com2002.views;

import javax.swing.JPanel;

import com2002.interfaces.Screen;
import com2002.models.Appointment;

public class AppointmentView implements Screen {
	
	private JPanel panel;
	private DisplayFrame frame;
	private Appointment appointment;

	public AppointmentView(DisplayFrame frame, Appointment appointment) {
		this.frame = frame;
		this.appointment = appointment;
		initialize();
	}
	
	private void initialize() {
		
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
}
