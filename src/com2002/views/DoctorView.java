package com2002.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
import javax.swing.JSeparator;
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

	private JPanel screen;
	private Doctor doctor;
	private DisplayFrame frame;
	private JPanel appointmentsPanel;
	private JScrollPane appointmentsScrollPane;
	private JLabel title;
	private List<Appointment> appointments;
	private List<JPanel> appointmentCards;
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
		//Main panel
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
		//title
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		LocalDate localDate = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year  = localDate.getYear();
		int month = localDate.getMonthValue();
		int day   = localDate.getDayOfMonth();
		String dayString = "Appointments - " + String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
		this.title = new JLabel(dayString, SwingConstants.CENTER);
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
		//Two buttons in the bottom
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());
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
		bottomPanel.add(datePicker);
		this.changeDayButton = new JButton("Change day");
		this.changeDayButton.setFont(new Font("Sans Serif", Font.PLAIN,
				DisplayFrame.FONT_SIZE / 2));
		//add action listener to change day button
		this.changeDayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					//get requested day
					Date selectedDate = (Date) datePicker.getModel().getValue();
					//change list of appointments
					appointments = Schedule.getAppointmentsByDoctorAndDay(doctor, selectedDate);
					//change title
					LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					int year  = localDate.getYear();
					int month = localDate.getMonthValue();
					int day   = localDate.getDayOfMonth();
					String dayString = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
					title.setText("Appointments - " + dayString);
					appointmentsPanel.removeAll();
					appointmentsPanel.setLayout(new BoxLayout(appointmentsPanel, BoxLayout.Y_AXIS));
					for (int i = appointmentCards.size() - 1; i >= 0; i--) {
						appointmentCards.remove(i);
					}
					for  (int i = 0; i < appointments.size(); i++) {
						addAppointment(appointments.get(i));
					}
					frame.revalidate();
				} catch (CommunicationsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		bottomPanel.add(this.changeDayButton);
		this.screen.add(bottomPanel, BorderLayout.SOUTH);
		frame.setFrameSize(DisplayFrame.DEFAULT_NUM, DisplayFrame.DEFAULT_NUM);
		frame.centerFrame();
	    //Menubar
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
				//We're all setup.. go to login screen
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
	
	@SuppressWarnings("deprecation")
	private void addAppointment(Appointment appointment) {
		//init
		this.appointmentCards.add(new JPanel());
		int index = this.appointmentCards.size() - 1;
		Patient patient;
		try {
			patient = appointment.getPatient();
			String patientName = patient.getTitle() + " " + patient.getFirstName() + " " + patient.getLastName();
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
			if (appointment.getAppointmentType().equals("Empty")) {
				JLabel emptyLabel = new JLabel("Empty Appointment");
				emptyLabel.setFont(new Font("Sans Serif", Font.PLAIN, 
						DisplayFrame.FONT_SIZE / 2));
				topPanel.add(emptyLabel);
			} else {
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
			}
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
					newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					AppointmentView appointmentView = new AppointmentView(newFrame, appointment);
					newFrame.setDisplayedPanel(appointmentView.getPanel());
				}
			});
			if (appointment.getAppointmentType().equals("Empty")) {
				startAppointment.setEnabled(false);
			}
			this.appointmentCards.get(index).add(bottomPanel);
			//add panel to main panel
			this.appointmentsPanel.add(this.appointmentCards.get(index));
			JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
			separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
			separator.setBackground(Color.black);
			this.appointmentsPanel.add(separator);
			
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
