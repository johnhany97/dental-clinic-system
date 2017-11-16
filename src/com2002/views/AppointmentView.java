package com2002.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com2002.interfaces.Screen;
import com2002.models.Appointment;
import com2002.models.Doctor;
import com2002.models.HealthPlan;
import com2002.models.Patient;
import com2002.models.Schedule;
import com2002.models.Usage;

public class AppointmentView implements Screen {
	
	private JPanel panel;
	private DisplayFrame frame;
	private Appointment appointment;
	private Patient patient;
	private Usage usage;
	private JPanel rightPanel;
	private JPanel leftPanel;
	private JPanel appointmentsPanel;
	private JScrollPane appointmentsScrollPane;
	private List<JPanel> appointmentCards;
	private ArrayList<Appointment> previousAppointments;
	private JTextArea notesTextArea;
	private JPanel activePreviousAppointment;
	private JScrollPane activePreviousAppointmentScrollPane;
	private List<JLabel> activePreviousAppointmentLabels;

	public AppointmentView(DisplayFrame frame, Appointment appointment) {
		try {
			this.frame = frame;
			this.frame.setFrameSize(DisplayFrame.DEFAULT_NUM, 7);
			this.frame.centerFrame();
			this.appointment = appointment;
			this.patient = appointment.getPatient();
			this.usage = new Usage(this.patient.getPatientID());
			this.previousAppointments = Schedule.getDoctorAppointmentsByPatient(this.appointment.getUsername(), this.patient);
			//Remove this appointment from list of previous appointments
			boolean flag = true;
			for (int i = 0; i < this.previousAppointments.size() && flag; i++) {
				if (this.appointment.equals(this.previousAppointments.get(i))) {
					this.previousAppointments.remove(i);
					flag = false;
				}
			}
			initialize();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
				    "Error connecting to the database. Check internet connection.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame,
				    "Error fetching previous appointments.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void initialize() {
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		//Left panel
		this.leftPanel = new JPanel();
		this.leftPanel.setLayout(new BoxLayout(this.leftPanel, BoxLayout.Y_AXIS));
		//Left panel consists of two sections
		//First: patient details
		JPanel patientDetails = new JPanel();
		this.leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		//name
		patientDetails.setLayout(new BoxLayout(patientDetails, BoxLayout.Y_AXIS));
		String fullName = this.patient.getTitle() + " " + this.patient.getFirstName() + " " + this.patient.getLastName();
		JLabel patientName = new JLabel(fullName, SwingConstants.LEFT);
		patientName.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE));
		patientDetails.add(patientName);
	    patientDetails.add(Box.createVerticalStrut(20));
		//appointment type
	    JLabel appointmentType = new JLabel(this.appointment.getAppointmentType(), SwingConstants.LEFT);
	    appointmentType.setFont(new Font("Sans Serif", Font.BOLD,
	    		DisplayFrame.FONT_SIZE / 2));
	    patientDetails.add(appointmentType);
	    patientDetails.add(Box.createVerticalStrut(20));
		//dob and age
		String dob = this.patient.getDateOfBirth().toString();
		long years = ChronoUnit.YEARS.between(this.patient.getDateOfBirth(), LocalDate.now());
		dob = String.valueOf(years) + " years old" +" | " + dob;
		JLabel dateOfBirthAndAge = new JLabel(dob, SwingConstants.LEFT);
		dateOfBirthAndAge.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		patientDetails.add(dateOfBirthAndAge);
	    patientDetails.add(Box.createVerticalStrut(20));
		//health plan
		JPanel hpPanel = new JPanel();
		hpPanel.setLayout(new BoxLayout(hpPanel, BoxLayout.Y_AXIS));
		String nameString = "No Health Plan";
		String checkupString = "Checkup: 0 out of 0";
		String repairString = "Repair: 0 out of 0";
		String hygieneString = "Hygiene: 0 out of 0";
		if (this.usage != null) {
			//hp name
			try {
				nameString = this.usage.getHealthPlanName();
				HealthPlan hp;
				hp = new HealthPlan(nameString);
				//usage
				int currentCheckup = this.usage.getCheckUpUsed();
				int totalCheckup = hp.getCheckUpLevel();
				checkupString = "<html><strong>Checkup:</strong> " + String.valueOf(currentCheckup) + " out of " + String.valueOf(totalCheckup) + "</html>";
				int currentRepair = this.usage.getRepairUsed();
				int totalRepair = hp.getRepairLevel();
				repairString = "<html><strong>Repair:</strong> " + String.valueOf(currentRepair) + " out of " + String.valueOf(totalRepair) + "</html>";
				int currentHygiene = this.usage.getHygieneUsed();
				int totalHygiene = hp.getHygieneLevel();
				hygieneString = "<html><strong>Hygiene:</strong> " + String.valueOf(currentHygiene) + " out of " + String.valueOf(totalHygiene) + "</html>";
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(frame,
					    "Error connecting to the database. Check internet connection.",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
		Border border = BorderFactory.createTitledBorder("Health plan");
		((TitledBorder) border).setTitleFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
	    hpPanel.setBorder(border);
	    JLabel hpLabel = new JLabel(nameString);
	    hpLabel.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
	    hpPanel.add(hpLabel);
	    JLabel checkupLabel = new JLabel(checkupString);
	    checkupLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
	    hpPanel.add(checkupLabel);
	    JLabel repairLabel = new JLabel(repairString);
	    repairLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
	    hpPanel.add(repairLabel);
	    JLabel hygieneLabel = new JLabel(hygieneString);
	    hygieneLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
	    hpPanel.add(hygieneLabel);
	    patientDetails.add(hpPanel);
		this.leftPanel.add(patientDetails);
	    this.leftPanel.add(Box.createVerticalStrut(20));
		this.leftPanel.add(new JSeparator());
	    this.leftPanel.add(Box.createVerticalStrut(20));
		//Second: this appointment's finishing section
		//treatment checkboxes
	    //notes text area
		JLabel notesLabel = new JLabel("Notes:");
		notesLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		this.leftPanel.add(notesLabel);
	    this.notesTextArea = new JTextArea();
	    JScrollPane notesTextAreaScrollPane = new JScrollPane(this.notesTextArea);
	    this.leftPanel.add(notesTextAreaScrollPane);
	    this.leftPanel.add(Box.createVerticalStrut(20));
	    //button to finish appointment
	    JButton finishButton = new JButton("Save appointment");
	    finishButton.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
	    finishButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO: Behavior on submit
			}
	    });
		this.leftPanel.add(finishButton);
		//add panels to each other
		this.leftPanel.setMaximumSize(new Dimension(frame.getWidth() / 2, Integer.MAX_VALUE));
		this.panel.add(this.leftPanel, BorderLayout.CENTER);
		//right panel
		this.rightPanel = new JPanel();
		this.rightPanel.setLayout(new BoxLayout(this.rightPanel, BoxLayout.Y_AXIS));
		this.rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		//label
		JLabel rightPanelLabel = new JLabel("Previous Appointments", SwingConstants.LEFT);
		rightPanelLabel.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE));
		this.rightPanel.add(rightPanelLabel);
		//top section with the selected appointment's details
		this.activePreviousAppointment = new JPanel();
		this.activePreviousAppointment.setLayout(new BoxLayout(this.activePreviousAppointment, BoxLayout.Y_AXIS));

		this.activePreviousAppointmentLabels = new ArrayList<JLabel>();
		setOldAppointmentInView(this.previousAppointments.get(0));
		this.activePreviousAppointmentScrollPane = new JScrollPane(this.activePreviousAppointment);
		this.rightPanel.add(this.activePreviousAppointmentScrollPane);
		//bottom section consists of list of all this patient's previous appointments if anys
		this.appointmentsPanel = new JPanel();
		this.appointmentsPanel.setLayout(new BoxLayout(this.appointmentsPanel, BoxLayout.Y_AXIS));
		//We want to be able to scroll through today's appointments
		this.appointmentsScrollPane = new JScrollPane(appointmentsPanel);
		this.appointmentsScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20), BorderFactory.createLineBorder(Color.black)));
		this.appointmentCards = new ArrayList<JPanel>();
		for (int i = 0; i < this.previousAppointments.size(); i++) {
			addAppointment(this.previousAppointments.get(i));
			
		}
		this.rightPanel.add(this.appointmentsScrollPane);
		this.panel.add(this.rightPanel, BorderLayout.EAST);
	}
	
	@SuppressWarnings("deprecation")
	private void addAppointment(Appointment appointment) {
		//init
		this.appointmentCards.add(new JPanel());
		int index = this.appointmentCards.size() - 1;
		String appointmentType = appointment.getAppointmentType();
		Timestamp startTimeTs = appointment.getStartTime();
		LocalDate startTime = startTimeTs.toLocalDateTime().toLocalDate();
		String appointmentDay = startTime.toString();
		//layout
		this.appointmentCards.get(index).setLayout(new BoxLayout(this.appointmentCards.get(index), BoxLayout.Y_AXIS));
		this.appointmentCards.get(index).setMaximumSize(new Dimension(Integer.MAX_VALUE, frame.getFrameHeightStep() / 2));
		// top content
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		JLabel appointmentTypeLabel = new JLabel(appointmentType);
		appointmentTypeLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		topPanel.add(appointmentTypeLabel);
		topPanel.add(Box.createHorizontalStrut(5));
		topPanel.add(new JSeparator(SwingConstants.VERTICAL));
		topPanel.add(Box.createHorizontalStrut(5));
		JLabel day = new JLabel(appointmentDay);
		day.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		topPanel.add(day);
		this.appointmentCards.get(index).add(topPanel);
		// bottom content
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		JButton moreDetailsButton = new JButton("More details");
		moreDetailsButton.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		moreDetailsButton.putClientProperty("Appointment", appointment);
		bottomPanel.add(moreDetailsButton);
		//event listener for the button
		moreDetailsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				activePreviousAppointment.removeAll();
				activePreviousAppointment.repaint();
				Appointment apt = (Appointment) moreDetailsButton.getClientProperty("Appointment");
				setOldAppointmentInView(apt);
				frame.revalidate();
			}
		});
		this.appointmentCards.get(index).add(bottomPanel);
		//add panel to main panel
		this.appointmentsPanel.add(this.appointmentCards.get(index));
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		separator.setBackground(Color.black);
		this.appointmentsPanel.add(separator);
	}
	
	private void setOldAppointmentInView(Appointment appointment) {
		//Get details from appointments
		String appointmentType = appointment.getAppointmentType();
		Timestamp startTimeTs = appointment.getStartTime();
		LocalDate startTime = startTimeTs.toLocalDateTime().toLocalDate();
		String appointmentDay = startTime.toString();
		String notes = appointment.getNotes();
		//Push each one of them in the appointments
		JLabel aptDay = new JLabel(appointmentDay);
		JLabel aptNotes = new JLabel(notes);
		JLabel aptType = new JLabel(appointmentType);
		aptDay.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		aptNotes.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		aptType.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		//titles
		JLabel dayTitle = new JLabel("Day: ");
		JLabel notesTitle = new JLabel("Notes (if any): ");
		JLabel typeTitle = new JLabel("Appointment Type: ");
		dayTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		notesTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		typeTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		//Add them all
		for (int i = 0; i < 6; i++) {
			this.activePreviousAppointmentLabels.add(new JLabel());
		}
		this.activePreviousAppointmentLabels.set(0, dayTitle);
		this.activePreviousAppointmentLabels.set(1, aptDay);
		this.activePreviousAppointmentLabels.set(2, typeTitle);
		this.activePreviousAppointmentLabels.set(3, aptType);
		this.activePreviousAppointmentLabels.set(4, notesTitle);
		this.activePreviousAppointmentLabels.set(5, aptNotes);
		this.activePreviousAppointment.removeAll();
		for (int i = 0; i < 6; i++) {
			this.activePreviousAppointment.add(this.activePreviousAppointmentLabels.get(i));
		}
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
}
