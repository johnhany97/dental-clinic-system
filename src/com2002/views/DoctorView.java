package com2002.views;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com2002.interfaces.Screen;
import com2002.models.Appointment;
import com2002.models.Doctor;
import com2002.models.Schedule;

public class DoctorView implements Screen {

	private JPanel screen;
	private Doctor doctor;
	private DisplayFrame frame;
	private JPanel appointmentsPanel;
	private JScrollPane appointmentsScrollPane;
	private List<Appointment> appointments;
	
	public DoctorView(DisplayFrame frame, Doctor doctor) {
		this.frame = frame;
		this.doctor = doctor;
		try {
			this.appointments = Schedule.getAppointmentsByDoctor(doctor);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
				    e.getMessage(),
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
		initializeScreen();
	}

	private void initializeScreen() {
		//Main panel
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		//AppointmentsPanel
		this.appointmentsPanel = new JPanel();
		this.appointmentsPanel.setLayout(new BoxLayout(this.appointmentsPanel, BoxLayout.Y_AXIS));
		//We want to be able to scroll through today's appointmnets
		this.appointmentsScrollPane = new JScrollPane(this.appointmentsPanel);
		//add it to the main panel
		this.screen.add(this.appointmentsScrollPane, BorderLayout.CENTER);
		//Add what we want to the appointmentsPanel (which is cards containing the appointments)
		//for each appointment, add event listener to take you to the appointmentsView
		//Above all that, add the date of the appointments
		//Two buttons in the bottom
	}
	
	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
