package com2002.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com2002.interfaces.Screen;
import com2002.models.Appointment;
import com2002.models.Doctor;
import com2002.models.Patient;
import com2002.models.Schedule;

public class DoctorView implements Screen {

	private JPanel screen;
	private Doctor doctor;
	private DisplayFrame frame;
	private JPanel appointmentsPanel;
	private JScrollPane appointmentsScrollPane;
	private List<Appointment> appointments;
	private List<JPanel> appointmentCards;
	
	public DoctorView(DisplayFrame frame, Doctor doctor) throws Exception {
		this.frame = frame;
		this.doctor = doctor;
		this.appointments = Schedule.getAppointmentsByDoctor(doctor);
		System.out.println(this.appointments);
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
		this.appointmentCards = new ArrayList<JPanel>();
		for (int i = 0; i < this.appointments.size(); i++) {
			addAppointment(this.appointments.get(i));
		}
		//for each appointment, add event listener to take you to the appointmentsView
		//Above all that, add the date of the appointments
		//Two buttons in the bottom
	}
	
	@SuppressWarnings("deprecation")
	private void addAppointment(Appointment appointment) {
		//init
		this.appointmentCards.add(new JPanel());
		int index = this.appointmentCards.size() - 1;
		Patient patient;
		try {
			patient = appointment.getPatient();
			String patientName = patient.getFirstName() + patient.getLastName();
			String appointmentType = appointment.getAppointmentType();
			Timestamp startTimeTs = appointment.getStartTime();
			String startTime = String.valueOf(startTimeTs.getHours()) + ":" + String.valueOf(startTimeTs.getMinutes());
//			String startTime = "Time";
			Timestamp endTimeTs = appointment.getEndTime();
			String endTime = String.valueOf(endTimeTs.getHours()) + ":" + String.valueOf(endTimeTs.getMinutes());
//			String endTime = "Time2";
			String appointmentTime = startTime + " - " + endTime;
			//set props
			//layout
			this.appointmentCards.get(index).setLayout(new FlowLayout());
			//background
//			this.appointmentCards.get(index).setBackground(Color.BLACK);
			// left content
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
			leftPanel.add(new JLabel(patientName));
			leftPanel.add(new JLabel(appointmentType));
			this.appointmentCards.get(index).add(leftPanel);
			// right content
			this.appointmentCards.get(index).add(new JLabel(appointmentTime));
			//event listener
			this.appointmentsPanel.add(this.appointmentCards.get(index));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
				    "Error connecting to the database. Check internet connection.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
