package com2002.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

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
	private JLabel title;
	private List<Appointment> appointments;
	private List<JPanel> appointmentCards;
	private JButton settingsButton;
	private JButton changeDayButton;
	
	public DoctorView(DisplayFrame frame, Doctor doctor) throws Exception {
		this.frame = frame;
		this.doctor = doctor;
		//Today
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		this.appointments = Schedule.getAppointmentsByDoctorAndDay(this.doctor, now); //Initially only for day 1
		initializeScreen();
	}

	private void initializeScreen() {
		frame.setFrameSize(DisplayFrame.DEFAULT_NUM, DisplayFrame.DEFAULT_NUM);
		frame.centerFrame();
		//Main panel
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		//title
		this.title = new JLabel("Today's Appointments", SwingConstants.CENTER);
		this.title.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		this.screen.add(this.title, BorderLayout.NORTH);
		//AppointmentsPanel
		this.appointmentsPanel = new JPanel();
		this.appointmentsPanel.setLayout(new BoxLayout(this.appointmentsPanel, BoxLayout.Y_AXIS));
		//We want to be able to scroll through today's appointments
		this.appointmentsScrollPane = new JScrollPane(this.appointmentsPanel);
		this.appointmentsScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20), BorderFactory.createLineBorder(Color.black)));
		//add it to the main panel
		this.screen.add(this.appointmentsScrollPane, BorderLayout.CENTER);
		this.appointmentCards = new ArrayList<JPanel>();
		for (int i = 0; i < this.appointments.size(); i++) {
			addAppointment(this.appointments.get(i));
		}
		//for each appointment, add event listener to take you to the appointmentsView
		//Two buttons in the bottom
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		this.changeDayButton = new JButton("Change day");
		this.changeDayButton.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		//add action listener to change day button
		buttonsPanel.add(this.changeDayButton);
		this.settingsButton = new JButton("Settings");
		this.settingsButton.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		//add action listener to settings button
		buttonsPanel.add(this.settingsButton);
		this.screen.add(buttonsPanel, BorderLayout.SOUTH);
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
			Timestamp endTimeTs = appointment.getEndTime();
			String endTime = String.valueOf(endTimeTs.getHours()) + ":" + String.valueOf(endTimeTs.getMinutes());
			String appointmentTime = startTime + " - " + endTime;
			//layout
			this.appointmentCards.get(index).setLayout(new BoxLayout(this.appointmentCards.get(index), BoxLayout.Y_AXIS));
			this.appointmentCards.get(index).setMaximumSize(new Dimension(Integer.MAX_VALUE, frame.getFrameHeightStep() / 2));
			// top content
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new FlowLayout());
			JLabel patientNameLabel = new JLabel(patientName);
			patientNameLabel.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			topPanel.add(patientNameLabel);
			topPanel.add(Box.createHorizontalStrut(5));
			topPanel.add(new JSeparator(SwingConstants.VERTICAL));
			topPanel.add(Box.createHorizontalStrut(5));
			JLabel appointmentTypeLabel = new JLabel(appointmentType);
			appointmentTypeLabel.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			topPanel.add(appointmentTypeLabel);
			topPanel.add(Box.createHorizontalStrut(5));
			topPanel.add(new JSeparator(SwingConstants.VERTICAL));
			topPanel.add(Box.createHorizontalStrut(5));
			JLabel appointmentTimeLabel = new JLabel(appointmentTime);
			appointmentTimeLabel.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			topPanel.add(appointmentTimeLabel);
			this.appointmentCards.get(index).add(topPanel);
			// bottom content
			JPanel bottomPanel = new JPanel();
			bottomPanel.setLayout(new FlowLayout());
			JButton startAppointment = new JButton("Start Appointment");
			startAppointment.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			bottomPanel.add(startAppointment);
			//event listener for the button
			startAppointment.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					DisplayFrame newFrame = new DisplayFrame();
					AppointmentView appointmentView = new AppointmentView(newFrame, appointment);
					newFrame.setDisplayedPanel(appointmentView.getPanel());
				}
			});
			this.appointmentCards.get(index).add(bottomPanel);
			//add panel to main panel
			this.appointmentsPanel.add(this.appointmentCards.get(index));
			JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
			separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
			separator.setBackground(Color.black);
			this.appointmentsPanel.add(separator);
			
		} catch (SQLException e) {
			e.printStackTrace();
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
