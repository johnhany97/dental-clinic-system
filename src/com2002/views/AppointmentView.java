package com2002.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import com2002.interfaces.Screen;
import com2002.models.Appointment;
import com2002.models.DBQueries;
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
	private List<JCheckBox> treatmentsInput;

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
			JOptionPane.showMessageDialog(frame,
				    "Error fetching previous appointments.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void initialize() throws CommunicationsException, SQLException {
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
		if (this.patient.getUsage() != null) {
			this.usage = this.patient.getUsage();
			nameString = this.usage.getHealthPlan().getName();
			HealthPlan hp = this.usage.getHealthPlan();
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
		//treatment check boxes
	    HashMap<String, Double> treatments = DBQueries.getTreatments();
	    this.treatmentsInput = new ArrayList<JCheckBox>();
	    for (Entry<String, Double> entry : treatments.entrySet()) {
	        String key = entry.getKey();
	        JCheckBox cb = new JCheckBox(key);
	        cb.setFont(new Font("Sans Serif", Font.PLAIN,
	        		DisplayFrame.FONT_SIZE / 2));
	        this.treatmentsInput.add(cb);
	    }
	    JPanel treatmentsPanel = new JPanel();
	    treatmentsPanel.setLayout(new GridLayout(0, 2));
	    JScrollPane treatmentsScrollPane = new JScrollPane(treatmentsPanel);
	    ArrayList<String> currentAppointmentTreatments = this.appointment.getTreatments();
	    for (int i = 0; i < this.treatmentsInput.size(); i++) {
	    	treatmentsPanel.add(this.treatmentsInput.get(i));
	    }
	    //make checkboxes selected if already set
	    for (int i = 0; i < currentAppointmentTreatments.size(); i++) {
	    	for (int j = 0; j < this.treatmentsInput.size(); j++) {
	    		if (currentAppointmentTreatments.get(i).equals(this.treatmentsInput.get(j).getText())) {
	    			this.treatmentsInput.get(j).setSelected(true);
	    		}
	    	}
	    }
	    JLabel treatmentsLabel = new JLabel("Treatments:");
	    treatmentsLabel.setFont(new Font("Sans Serif", Font.PLAIN,
	    		DisplayFrame.FONT_SIZE / 2));
	    this.leftPanel.add(treatmentsLabel);
	    this.leftPanel.add(treatmentsScrollPane);
	    //notes text area
		JLabel notesLabel = new JLabel("Notes:");
		notesLabel.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		this.leftPanel.add(notesLabel);
	    this.notesTextArea = new JTextArea();
	    if (this.appointment.getNotes() != null && this.appointment.getNotes() != "") {
	    	this.notesTextArea.setText(this.appointment.getNotes());
	    }
	    JScrollPane notesTextAreaScrollPane = new JScrollPane(this.notesTextArea);
	    this.leftPanel.add(notesTextAreaScrollPane);
	    this.leftPanel.add(Box.createVerticalStrut(20));
	    //button to finish appointment
	    JButton finishButton = new JButton("Save appointment");
	    finishButton.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
	    finishButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Obtain data to save
				String notes = notesTextArea.getText();
				ArrayList<String> selectedTreatments = new ArrayList<String>();
				for (int i = 0; i < treatmentsInput.size(); i++) {
					if (treatmentsInput.get(i).isSelected()) { //it is selected
						selectedTreatments.add(treatmentsInput.get(i).getText());
					}
				}
				// Save note if any
				try {
					//remove all treatments
					appointment.removeAllTreatments();
					if (notes != null && notes != "") {
						appointment.setNotes(notes);
					}
					try {
						// Save list of treatments if any
						if (selectedTreatments.size() > 0) {
							appointment.addTreatments(selectedTreatments);
						}
						JOptionPane.showMessageDialog (null, "Successfully saved appointment details", "Success!", JOptionPane.INFORMATION_MESSAGE);	
						//Close this window
						frame.dispose();
					} catch (SQLException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frame,
							    "Error saving treatments",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(frame,
						    "Error saving notes",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
	    });
		this.leftPanel.add(finishButton);
		//add panels to each other
		this.leftPanel.setMaximumSize(new Dimension(frame.getWidth() / 2, frame.getHeight()));
		this.panel.add(this.leftPanel, BorderLayout.CENTER);
		//right panel
		this.rightPanel = new JPanel();
		this.rightPanel.setLayout(new BoxLayout(this.rightPanel, BoxLayout.Y_AXIS));
		this.rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		//label
		JLabel rightPanelLabel = new JLabel("Other Appointments", SwingConstants.LEFT);
		rightPanelLabel.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE));
		this.rightPanel.add(rightPanelLabel);
		//top section with the selected appointment's details
		this.activePreviousAppointment = new JPanel();
		this.activePreviousAppointment.setLayout(new BoxLayout(this.activePreviousAppointment, BoxLayout.Y_AXIS));
		this.activePreviousAppointmentLabels = new ArrayList<JLabel>();
		if (this.previousAppointments.size() > 0) { //there are previous appointments
			setOldAppointmentInView(this.previousAppointments.get(0));
			this.activePreviousAppointmentScrollPane = new JScrollPane(this.activePreviousAppointment);
			this.activePreviousAppointmentScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20), BorderFactory.createLineBorder(Color.black)));
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
		} else { //no previous appointments
			this.rightPanel.add(new JLabel("No previous appointment"));
		}
		this.panel.add(this.rightPanel, BorderLayout.EAST);
	}
	
	private void addAppointment(Appointment appointment) {
		try {
			this.appointmentCards.add(new JPanel());
			int index = this.appointmentCards.size() - 1;
			this.appointmentCards.get(index).setLayout(new BorderLayout());
			this.appointmentCards.get(index).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.black)));
			//Get Appointment details
			Patient patient;
			patient = appointment.getPatient();
			String appointmentType = appointment.getAppointmentType();
			String patientName = patient.getTitle() + " " + patient.getFirstName() + " " + patient.getLastName();
			if (appointmentType.equals("Empty")) {
				patientName = "Empty Appointment";
				
			}
			String docName = this.appointment.getDoctor().getFirstName() + " " + this.appointment.getDoctor().getLastName();
			String appointmentStatus = "Single Appointment";
			if (appointment.getTotalAppointments() > 1) {
				appointmentStatus = "Appointment " + appointment.getCurrentAppointment() + " of " + appointment.getTotalAppointments();
			}
			LocalDateTime startTime = appointment.getStartTime().toLocalDateTime();
			String startString = String.format("%tH:%tM", startTime, startTime);
			String startDayString = String.format("%tD", startTime);
			LocalDateTime endTime = appointment.getEndTime().toLocalDateTime();
			String endString = String.format("%tH:%tM", endTime, endTime);
			String endDayString = String.format("%tD", endTime);
			//Left section (contains time)
			JPanel leftSection = new JPanel();
			leftSection.setLayout(new BoxLayout(leftSection, BoxLayout.Y_AXIS));
			JLabel start = new JLabel("Start", SwingConstants.CENTER);
			start.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE / 3));
			leftSection.add(start);
			JLabel startT = new JLabel(startString, SwingConstants.CENTER);
			startT.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			leftSection.add(startT);
			JLabel startD = new JLabel(startDayString, SwingConstants.CENTER);
			startD.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 3));
			leftSection.add(startD);
			JLabel end = new JLabel("End", SwingConstants.CENTER);
			end.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE / 3));
			leftSection.add(end);
			JLabel endT = new JLabel(endString, SwingConstants.CENTER);
			endT.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			leftSection.add(endT);
			JLabel endD = new JLabel(endDayString, SwingConstants.CENTER);
			endD.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 3));
			leftSection.add(endD);
			leftSection.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			this.appointmentCards.get(index).add(leftSection, BorderLayout.WEST);
			//right section (rest of appointment details)
			JPanel topRightSection = new JPanel();
			topRightSection.setLayout(new BoxLayout(topRightSection, BoxLayout.Y_AXIS));
			topRightSection.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			JLabel patientT = new JLabel(patientName, SwingConstants.LEFT);
			patientT.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			topRightSection.add(patientT);
			topRightSection.setAlignmentX(Component.LEFT_ALIGNMENT);
			JLabel typeAndStatus = new JLabel(appointmentType + " | " + appointmentStatus);
			typeAndStatus.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 3));
			topRightSection.add(typeAndStatus);
			JLabel doctorT = new JLabel("Doctor: " + docName);
			doctorT.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 3));
			topRightSection.add(doctorT);
			JPanel bottomRightSection = new JPanel();
			bottomRightSection.setLayout(new FlowLayout());
			bottomRightSection.setAlignmentX(Component.LEFT_ALIGNMENT);
			//Buttons
			//Appointment Details Button
			JButton detailsButton = new JButton("View Above");
			detailsButton.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			bottomRightSection.add(detailsButton);
			detailsButton.putClientProperty("Appointment", appointment);
			detailsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						activePreviousAppointment.removeAll();
						activePreviousAppointment.repaint();
						Appointment apt = (Appointment) detailsButton.getClientProperty("Appointment");
						setOldAppointmentInView(apt);
						frame.revalidate();
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame,
							    "Error fetching appointment details",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			bottomRightSection.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			//TODO: Action listener
			JPanel rightSection = new JPanel();
			rightSection.setLayout(new BoxLayout(rightSection, BoxLayout.Y_AXIS));
			rightSection.add(topRightSection);
			rightSection.add(bottomRightSection);
			this.appointmentCards.get(index).add(rightSection, BorderLayout.CENTER);
			this.appointmentsPanel.add(this.appointmentCards.get(index));

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void setOldAppointmentInView(Appointment appointment) throws SQLException {
		//Get details from appointments
		String appointmentType = appointment.getAppointmentType();
		Timestamp startTimeTs = appointment.getStartTime();
		LocalDate startTime = startTimeTs.toLocalDateTime().toLocalDate();
		String appointmentDay = startTime.toString();
		String notes = appointment.getNotes();
		ArrayList<String> list = appointment.getTreatments();
		String listString = "None";
		if (list.size() > 0) {
			listString = "";
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					listString += list.get(i);
				} else {
					listString += ", " + list.get(i);
				}
			}
		}
		//Push each one of them in the appointments
		JLabel aptDay = new JLabel(appointmentDay);
		JLabel aptNotes = new JLabel(notes);
		JLabel aptType = new JLabel(appointmentType);
		JLabel aptTreatments = new JLabel(listString);
		aptDay.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		aptNotes.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		aptType.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		aptTreatments.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		//titles
		JLabel dayTitle = new JLabel("Day: ");
		JLabel notesTitle = new JLabel("Notes (if any): ");
		JLabel typeTitle = new JLabel("Appointment Type: ");
		JLabel treatmentsTitle = new JLabel("Treatments: ");
		dayTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		notesTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		typeTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		treatmentsTitle.setFont(new Font("Sans Serif", Font.BOLD,
				DisplayFrame.FONT_SIZE / 2));
		//Add them all
		for (int i = 0; i < 8; i++) {
			this.activePreviousAppointmentLabels.add(new JLabel());
		}
		this.activePreviousAppointmentLabels.set(0, dayTitle);
		this.activePreviousAppointmentLabels.set(1, aptDay);
		this.activePreviousAppointmentLabels.set(2, typeTitle);
		this.activePreviousAppointmentLabels.set(3, aptType);
		this.activePreviousAppointmentLabels.set(4, treatmentsTitle);
		this.activePreviousAppointmentLabels.set(5, aptTreatments);
		this.activePreviousAppointmentLabels.set(6, notesTitle);
		this.activePreviousAppointmentLabels.set(7, aptNotes);
		this.activePreviousAppointment.removeAll();
		this.activePreviousAppointment.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		for (int i = 0; i < 8; i++) {
			this.activePreviousAppointment.add(this.activePreviousAppointmentLabels.get(i));
		}
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
}
