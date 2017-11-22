package com2002.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com2002.interfaces.Screen;
import com2002.models.Appointment;
import com2002.models.Doctor;
import com2002.models.Patient;
import com2002.models.Schedule;
import com2002.models.Secretary;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class SecretaryView implements Screen {
	
	private JPanel screen;
	private Secretary secretary;
	private DisplayFrame frame;
	//left panel
	private JPanel leftScreen;
	private JPanel appointmentsScreen;
	private JScrollPane appointmentsScrollPane;
	private List<JPanel> appointmentCards;
	//right panel
	private JPanel rightScreen;
	private JTabbedPane tabbedPane;
	private List<JTextField> patientsTabInputs;
	
	public SecretaryView(DisplayFrame frame, Secretary secretary) {
		this.frame = frame;
		this.secretary = secretary;
		this.screen = new JPanel();
		this.screen.setLayout(new GridLayout(1, 2));
		initializeScreen();
	}

	private void initializeScreen() {
		try {
			List<Appointment> todayAppointments;
			//get today's appointments
			Calendar calendar = Calendar.getInstance();
			Date now = calendar.getTime();
			todayAppointments = Schedule.getAppointmentsByDay(now);
			this.frame.setFrameSize(DisplayFrame.DEFAULT_NUM, DisplayFrame.DEFAULT_NUM * 2);
			this.frame.centerFrame();
			//Left screen
			this.leftScreen = new JPanel();
			this.leftScreen.setLayout(new BorderLayout());
			this.leftScreen.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			//Title
			JLabel title = new JLabel("Appointments", SwingConstants.CENTER);
			title.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE));
			this.leftScreen.add(title, BorderLayout.NORTH);
			//Center of it is the appointments part
			this.appointmentsScreen = new JPanel();
			this.appointmentsScrollPane = new JScrollPane(this.appointmentsScreen);
			this.appointmentsScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.black)));
			//add it to left screen
			this.leftScreen.add(this.appointmentsScrollPane, BorderLayout.CENTER);
			this.appointmentsScreen.setLayout(new GridLayout(0,2));
			this.appointmentCards = new ArrayList<JPanel>();
			if (todayAppointments.size() > 0) {
				for (int i = 0; i < todayAppointments.size(); i++) {
					addAppointment(todayAppointments.get(i));
				}
			} else {
				//No appointments for today
				this.appointmentsScreen.setLayout(new BorderLayout());
				JLabel imgLabel = new JLabel(new ImageIcon(((new ImageIcon("resources/pictures/none_found.png")).getImage()).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH)), SwingConstants.CENTER);
				this.appointmentsScreen.add(imgLabel, BorderLayout.CENTER);
			}
			//Date picker and new appointment
			JPanel bottomLeftPanel = new JPanel();
			bottomLeftPanel.setLayout(new BorderLayout());
			//date picker
			UtilDateModel model = new UtilDateModel();
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);

			//set by default today
			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			model.setSelected(true);
			model.addPropertyChangeListener(new PropertyChangeListener() {
	            public void propertyChange(PropertyChangeEvent e) {
	            	if (e.getPropertyName().equals("value")) {
						try {
							//get requested day
							Date selectedDate = (Date) datePicker.getModel().getValue();
							List<Appointment> appointmentList = Schedule.getAppointmentsByDay(selectedDate);
							appointmentCards.clear();
							appointmentsScreen.removeAll();
							appointmentsScreen.setLayout(new GridLayout(0,2));
							if (appointmentList.size() > 0) {
								for (int i = 0; i < appointmentList.size(); i++) {
									addAppointment(appointmentList.get(i));
								}
							} else {
								//No appointments for today
								appointmentsScreen.setLayout(new BorderLayout());
								JLabel imgLabel = new JLabel(new ImageIcon(((new ImageIcon("resources/pictures/none_found.png")).getImage()).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH)), SwingConstants.CENTER);
								appointmentsScreen.add(imgLabel, BorderLayout.CENTER);
							}
							frame.revalidate();
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(frame,
								    "Database error. Check your internet connnection.",
								    "Error fetching appointments",
								    JOptionPane.ERROR_MESSAGE);
						}
	            	}
	            }
			});
			//button
			JButton newAppointmentButton = new JButton("New Appointment");
			newAppointmentButton.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE /2));
			bottomLeftPanel.add(datePicker,  BorderLayout.WEST);
			bottomLeftPanel.add(newAppointmentButton, BorderLayout.EAST);
			bottomLeftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			this.leftScreen.add(bottomLeftPanel, BorderLayout.SOUTH);
			//right screen
			this.rightScreen = new JPanel();
			this.rightScreen.setLayout(new BorderLayout());
			this.tabbedPane = new JTabbedPane();
			this.tabbedPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			this.rightScreen.add(tabbedPane, BorderLayout.CENTER);
			this.tabbedPane.setFont(new Font("Sans Serif", Font.PLAIN,
				    	DisplayFrame.FONT_SIZE / 2));
			//register tab
			JPanel registerTab = new JPanel();
			this.tabbedPane.addTab("Register", registerTab);
			//patients tab
			JPanel patientsTab = new JPanel();
			patientsTab.setLayout(new BorderLayout());
			patientsTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			JPanel patientsTabInputs = new JPanel();
			patientsTabInputs.setLayout(new GridLayout(2, 2));
			JPanel patientsInputsAndButtons = new JPanel();
			patientsInputsAndButtons.setLayout(new FlowLayout());
			
			patientsTab.add(patientsInputsAndButtons, BorderLayout.NORTH);
			//inputs
			this.patientsTabInputs = new ArrayList<JTextField>();
			JTextField firstName = new JTextField();
			firstName.setToolTipText("Patient First Name");
			firstName.setFont(new Font("Sans Serif", Font.PLAIN,
				    	DisplayFrame.FONT_SIZE / 2));
			this.patientsTabInputs.add(firstName);
			JTextField lastName = new JTextField();
			lastName.setToolTipText("Patient Last Name");
			lastName.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
			this.patientsTabInputs.add(lastName);
			JTextField houseNumber = new JTextField();
			houseNumber.setToolTipText("House number");
			houseNumber.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
			this.patientsTabInputs.add(houseNumber);
			JTextField postCode = new JTextField();
			postCode.setToolTipText("Post code");
			postCode.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
			this.patientsTabInputs.add(postCode);
			//add them to the tab
			JLabel label1 = new JLabel("First name:");
			label1.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			patientsTabInputs.add(label1);
			patientsTabInputs.add(firstName);
			JLabel label2 = new JLabel("Last name:");
			label2.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			patientsTabInputs.add(label2);
			patientsTabInputs.add(lastName);
			JLabel label3 = new JLabel("House Number:");
			label3.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			patientsTabInputs.add(label3);
			patientsTabInputs.add(houseNumber);
			JLabel label4 = new JLabel("Postcode:");
			label4.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			patientsTabInputs.add(label4);
			patientsTabInputs.add(postCode);
			patientsInputsAndButtons.add(patientsTabInputs);
			//button
			JButton searchPatientsButton = new JButton("Search");
			searchPatientsButton.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			searchPatientsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Search and return list of patients
					
				}
			});
			patientsInputsAndButtons.add(searchPatientsButton);
			//actual patients
			JTable patientsTable = new JTable();
			JScrollPane patientsScrollPane = new JScrollPane(patientsTable);
			patientsTable.setFillsViewportHeight(true);
			patientsTab.add(patientsScrollPane, BorderLayout.CENTER);
			this.tabbedPane.addTab("Patients", patientsTab);
			//addresses tab
			JPanel addressesTab = new JPanel();
			this.tabbedPane.addTab("Addresses", addressesTab);
			//Add both to main screen
			this.screen.add(this.leftScreen);
			this.screen.add(this.rightScreen);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
				    "Database error. Check your internet connnection.",
				    "Error fetching appointments",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void addAppointment(Appointment app) {
		try {
			this.appointmentCards.add(new JPanel());
			int index = this.appointmentCards.size() - 1;
			this.appointmentCards.get(index).setLayout(new BorderLayout());
			this.appointmentCards.get(index).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.black)));
			//Get Appointment details
			Patient patient = app.getPatient();
			String appointmentType = app.getAppointmentType();
			String patientName = patient.getTitle() + " " + patient.getFirstName() + " " + patient.getLastName();
			if (appointmentType.equals("Empty")) {
				patientName = "Empty Appointment";
			}
			Doctor doctor = app.getDoctor();
			String docName = doctor.getFirstName() + " " + doctor.getLastName();
			String appointmentStatus = "Single Appointment";
			if (app.getTotalAppointments() > 1) {
				appointmentStatus = "Appointment " + app.getCurrentAppointment() + " of " + app.getTotalAppointments();
			}
			LocalDateTime startTime = app.getStartTime().toLocalDateTime();
			String startString = String.valueOf(startTime.getHour()) + ":" + String.valueOf(startTime.getMinute());
			LocalDateTime endTime = app.getEndTime().toLocalDateTime();
			String endString = String.valueOf(endTime.getHour()) + ":" + String.valueOf(endTime.getMinute());
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
			JLabel end = new JLabel("End", SwingConstants.CENTER);
			end.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE / 3));
			leftSection.add(end);
			JLabel endT = new JLabel(endString, SwingConstants.CENTER);
			endT.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			leftSection.add(endT);
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
			JButton detailsButton = new JButton("Details");
			detailsButton.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 3));
			bottomRightSection.add(detailsButton);
			//TODO: Action listener
			JButton patientInfoButton = new JButton("Patient Info");
			patientInfoButton.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 3));
			bottomRightSection.add(patientInfoButton);
			bottomRightSection.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			//TODO: Action listener
			JPanel rightSection = new JPanel();
			rightSection.setLayout(new BoxLayout(rightSection, BoxLayout.Y_AXIS));
			rightSection.add(topRightSection);
			rightSection.add(bottomRightSection);
			this.appointmentCards.get(index).add(rightSection, BorderLayout.CENTER);
			this.appointmentsScreen.add(this.appointmentCards.get(index));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
				    "Database error. Check your internet connnection.",
				    "Error fetching patient",
				    JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
