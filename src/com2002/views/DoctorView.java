/**
 * DoctorView class
 * 
 * Class that represents a doctor's view of the system
 * @author John Ayad
 */
package com2002.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import com2002.interfaces.Screen;
import com2002.models.Appointment;
import com2002.models.Doctor;
import com2002.models.Patient;
import com2002.models.Schedule;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class DoctorView implements Screen {

	// instance variables
	private JPanel screen;
	private Doctor doctor;
	private DisplayFrame frame;
	private JPanel appointmentsPanel;
	private JScrollPane appointmentsScrollPane;
	private JLabel title;
	private List<Appointment> appointments;
	private List<JPanel> appointmentCards;
	private JDatePickerImpl datePicker;

	/**
	 * Constructor used to instantiate a DoctorView
	 * 
	 * @param frame
	 *            DisplayFrame in which it is to be shown
	 * @param doctor
	 *            Doctor who is to see the view
	 * @throws Exception
	 *             in case of error while creating view
	 */
	public DoctorView(DisplayFrame frame, Doctor doctor) throws Exception {
		this.frame = frame;
		this.doctor = doctor;
		// Today
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		this.appointments = Schedule.getAppointmentsByDoctorAndDay(this.doctor, now); // Initially only for day 1
		initialize();
	}

	/**
	 * Function used to initialize all components and add their relative action
	 * listeners
	 */
	private void initialize() {
		// Main panel
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		// title
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		LocalDate localDate = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();
		String dayString = "Appointments - " + String.valueOf(day) + "/" + String.valueOf(month) + "/"
				+ String.valueOf(year);
		this.title = new JLabel(dayString, SwingConstants.CENTER);
		this.title.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE));
		this.screen.add(this.title, BorderLayout.NORTH);
		// AppointmentsPanel
		this.appointmentsPanel = new JPanel();
		this.appointmentsPanel.setLayout(new BoxLayout(this.appointmentsPanel, BoxLayout.Y_AXIS));
		// We want to be able to scroll through today's appointments
		this.appointmentsScrollPane = new JScrollPane(this.appointmentsPanel);
		this.appointmentsScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(10, 20, 10, 20), BorderFactory.createLineBorder(Color.black)));
		// add it to the main panel
		this.screen.add(this.appointmentsScrollPane, BorderLayout.CENTER);
		this.appointmentCards = new ArrayList<JPanel>();
		for (int i = 0; i < this.appointments.size(); i++) {
			addAppointment(this.appointments.get(i));
		}
		// Two buttons in the bottom
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
		// date picker
		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		this.datePicker = new JDatePickerImpl(datePanel);
		// set by default today
		JLabel label = new JLabel("Viewing day: ");
		label.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
		bottomPanel.add(label);
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		model.setSelected(true);
		bottomPanel.add(datePicker);
		// On date update
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getPropertyName().equals("value")) {
					try {
						// get requested day
						Date selectedDate = (Date) datePicker.getModel().getValue();
						// change list of appointments
						appointments = Schedule.getAppointmentsByDoctorAndDay(doctor, selectedDate);
						// change title
						LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						int year = localDate.getYear();
						int month = localDate.getMonthValue();
						int day = localDate.getDayOfMonth();
						String dayString = String.valueOf(day) + "/" + String.valueOf(month) + "/"
								+ String.valueOf(year);
						title.setText("Appointments - " + dayString);
						appointmentsPanel.removeAll();
						appointmentsPanel.repaint();
						appointmentCards.clear();
						for (int i = 0; i < appointments.size(); i++) {
							addAppointment(appointments.get(i));
						}
						frame.revalidate();
					} catch (CommunicationsException e1) {
						JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		this.screen.add(bottomPanel, BorderLayout.SOUTH);
		frame.setFrameSize(DisplayFrame.DEFAULT_NUM, DisplayFrame.DEFAULT_NUM);
		frame.centerFrame();
		// Menubar
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		JMenuItem logOutMenuItem = new JMenuItem("Log out");
		logOutMenuItem.setMnemonic(KeyEvent.VK_L);
		logOutMenuItem.setToolTipText("Log out");
		logOutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				DisplayFrame window = new DisplayFrame();
				// We're all setup.. go to login screen
				LoginView loginScreen = new LoginView(window);
				window.setDisplayedPanel(loginScreen.getPanel());
			}
		});
		file.add(logOutMenuItem);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setMnemonic(KeyEvent.VK_E);
		exitMenuItem.setToolTipText("Exit application");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		file.add(exitMenuItem);
		menuBar.add(file);
		frame.setJMenuBar(menuBar);
	}

	/**
	 * addAppointment function
	 * 
	 * Used to add an appointment in the Day View for a doctor
	 * 
	 * @param appointment
	 *            Appointment to add
	 */
	private void addAppointment(Appointment appointment) {
		try {
			this.appointmentCards.add(new JPanel());
			int index = this.appointmentCards.size() - 1;
			this.appointmentCards.get(index).setLayout(new BorderLayout());
			this.appointmentCards.get(index).setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.black)));
			// Get Appointment details
			Patient patient;
			patient = appointment.getPatient();
			String appointmentType = appointment.getAppointmentType();
			String patientName = patient.getTitle() + " " + patient.getFirstName() + " " + patient.getLastName();
			if (appointmentType.equals("Empty")) {
				patientName = "Empty Appointment";

			}
			String docName = this.doctor.getFirstName() + " " + this.doctor.getLastName();
			String appointmentStatus = "Single Appointment";
			if (appointment.getTotalAppointments() > 1) {
				appointmentStatus = "Appointment " + appointment.getCurrentAppointment() + " of "
						+ appointment.getTotalAppointments();
			}
			LocalDateTime startTime = appointment.getStartTime().toLocalDateTime();
			String startString = String.format("%tH:%tM", startTime, startTime);
			String startDayString = String.format("%tD", startTime);
			LocalDateTime endTime = appointment.getEndTime().toLocalDateTime();
			String endString = String.format("%tH:%tM", endTime, endTime);
			String endDayString = String.format("%tD", endTime);
			// Left section (contains time)
			JPanel leftSection = new JPanel();
			leftSection.setLayout(new BoxLayout(leftSection, BoxLayout.Y_AXIS));
			JLabel start = new JLabel("Start", SwingConstants.CENTER);
			start.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 3));
			leftSection.add(start);
			JLabel startT = new JLabel(startString, SwingConstants.CENTER);
			startT.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			leftSection.add(startT);
			JLabel startD = new JLabel(startDayString, SwingConstants.CENTER);
			startD.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 3));
			leftSection.add(startD);
			JLabel end = new JLabel("End", SwingConstants.CENTER);
			end.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 3));
			leftSection.add(end);
			JLabel endT = new JLabel(endString, SwingConstants.CENTER);
			endT.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			leftSection.add(endT);
			JLabel endD = new JLabel(endDayString, SwingConstants.CENTER);
			endD.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 3));
			leftSection.add(endD);
			leftSection.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			this.appointmentCards.get(index).add(leftSection, BorderLayout.WEST);
			// right section (rest of appointment details)
			JPanel topRightSection = new JPanel();
			topRightSection.setLayout(new BoxLayout(topRightSection, BoxLayout.Y_AXIS));
			topRightSection.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			JLabel patientT = new JLabel(patientName, SwingConstants.LEFT);
			patientT.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			topRightSection.add(patientT);
			topRightSection.setAlignmentX(Component.LEFT_ALIGNMENT);
			JLabel typeAndStatus = new JLabel(appointmentType + " | " + appointmentStatus);
			typeAndStatus.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 3));
			topRightSection.add(typeAndStatus);
			JLabel doctorT = new JLabel("Doctor: " + docName);
			doctorT.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 3));
			topRightSection.add(doctorT);
			JPanel bottomRightSection = new JPanel();
			bottomRightSection.setLayout(new FlowLayout());
			bottomRightSection.setAlignmentX(Component.LEFT_ALIGNMENT);
			// Buttons
			// Appointment Details Button
			JButton detailsButton = new JButton("View");
			detailsButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			bottomRightSection.add(detailsButton);
			detailsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					DisplayFrame newFrame = new DisplayFrame();
					newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					AppointmentView appointmentView = new AppointmentView(newFrame, appointment,
							AppointmentView.DOCTOR);
					newFrame.setDisplayedPanel(appointmentView.getPanel());
				}
			});
			// Patient Info Button
			JButton patientInfoButton = new JButton("Patient Info");
			patientInfoButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			patientInfoButton.putClientProperty("id", patient.getPatientID());
			patientInfoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						Patient patient = new Patient((int) patientInfoButton.getClientProperty("id"));
						DisplayFrame patientViewFrame = new DisplayFrame();
						PatientView patientView = new PatientView(patientViewFrame, patient, AppointmentView.DOCTOR);
						patientViewFrame.setDisplayedPanel(patientView.getPanel());
					} catch (CommunicationsException e) {
						JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching patient",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			JButton deleteButton = new JButton("Cancel");
			deleteButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			deleteButton.putClientProperty("Appointment", appointment);
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Appointment appointment = (Appointment) deleteButton.getClientProperty("Appointment");
					int selectedOption = JOptionPane.showConfirmDialog(null, "Do you wanna cancel this appointment?",
							"Choose", JOptionPane.YES_NO_OPTION);
					if (selectedOption == JOptionPane.YES_OPTION) {
						try {
							appointment.removeAppointment();
							// refresh
							// get day
							Date selectedDate = (Date) datePicker.getModel().getValue();
							// change list of appointments
							appointments = Schedule.getAppointmentsByDoctorAndDay(doctor, selectedDate);
							// change title
							LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
							int year = localDate.getYear();
							int month = localDate.getMonthValue();
							int day = localDate.getDayOfMonth();
							String dayString = String.valueOf(day) + "/" + String.valueOf(month) + "/"
									+ String.valueOf(year);
							title.setText("Appointments - " + dayString);
							appointmentsPanel.removeAll();
							appointmentsPanel.repaint();
							appointmentCards.clear();
							for (int i = 0; i < appointments.size(); i++) {
								addAppointment(appointments.get(i));
							}
							frame.revalidate();
						} catch (CommunicationsException e) {
							JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error",
									JOptionPane.ERROR_MESSAGE);
						} catch (SQLException e) {
							JOptionPane.showMessageDialog(frame, e.getMessage(), "Error deleting appointment",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
			bottomRightSection.add(patientInfoButton);
			bottomRightSection.add(deleteButton);
			if (appointment.getAppointmentType().equals("Empty")) {
				detailsButton.setEnabled(false);
				patientInfoButton.setEnabled(false);
			}
			bottomRightSection.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			// TODO: Action listener
			JPanel rightSection = new JPanel();
			rightSection.setLayout(new BoxLayout(rightSection, BoxLayout.Y_AXIS));
			rightSection.add(topRightSection);
			rightSection.add(bottomRightSection);
			this.appointmentCards.get(index).add(rightSection, BorderLayout.CENTER);
			this.appointmentsPanel.add(this.appointmentCards.get(index));
		} catch (CommunicationsException e) {
			JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
