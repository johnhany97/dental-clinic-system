package com2002.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import com2002.interfaces.Screen;
import com2002.models.Appointment;
import com2002.models.Doctor;
import com2002.models.HealthPlan;
import com2002.models.Patient;
import com2002.models.Schedule;
import com2002.models.Usage;

public class PatientView implements Screen {
	
	private JPanel screen;
	private DisplayFrame frame;
	private Patient patient;
	private ArrayList<JPanel> appointmentCards;
	private JPanel appointmentsPanel;
	private JLabel hpLabel;
	private JLabel checkupLabel;
	private JLabel repairLabel;
	private JLabel hygieneLabel;
	
	public PatientView(DisplayFrame frame, Patient patient) {
		this.frame = frame;
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.patient = patient;
		initialize();
	}
	
	private void initialize() {
		try {
			//Patient details
			String name = this.patient.getTitle() + " " + this.patient.getFirstName() + " " + this.patient.getLastName();
			String dob = this.patient.getDateOfBirth().toString();
			long years = ChronoUnit.YEARS.between(this.patient.getDateOfBirth(), LocalDate.now());
			String phoneNumber = this.patient.getPhoneNumber();
			//screen
			this.screen = new JPanel();
			this.screen.setLayout(new BorderLayout());
			this.screen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			//north
			JPanel northPanel = new JPanel();
			this.screen.add(northPanel, BorderLayout.NORTH);
			northPanel.setLayout(new GridLayout(0, 1));
			JLabel imgLabel = new JLabel(new ImageIcon(((new ImageIcon("resources/pictures/user.png")).getImage()).getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)), SwingConstants.CENTER);
			northPanel.add(imgLabel, BorderLayout.NORTH);
			JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
			nameLabel.setFont(new Font("Sans Serif", Font.BOLD, 
					DisplayFrame.FONT_SIZE));
			northPanel.add(nameLabel);
			//center panel
			JPanel centerPanelHolder = new JPanel();
			centerPanelHolder.setLayout(new GridLayout(0, 1));
			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
			centerPanelHolder.add(centerPanel);
			this.screen.add(centerPanelHolder, BorderLayout.CENTER);
			//patient details' labels
			JLabel dobLabel1 = new JLabel("Date Of Birth: ");
			dobLabel1.setFont(new Font("Sans Serif", Font.BOLD, 
					DisplayFrame.FONT_SIZE / 2));
			JLabel ageLabel1 = new JLabel("Age: ");
			ageLabel1.setFont(new Font("Sans Serif", Font.BOLD, 
					DisplayFrame.FONT_SIZE / 2));
			JLabel phoneNumberLabel1 = new JLabel("Phone Number: ");
			phoneNumberLabel1.setFont(new Font("Sans Serif", Font.BOLD, 
					DisplayFrame.FONT_SIZE / 2));
			JLabel dobLabel2 = new JLabel(dob);
			dobLabel2.setFont(new Font("Sans Serif", Font.PLAIN, 
					DisplayFrame.FONT_SIZE / 2));
			JLabel ageLabel2 = new JLabel(String.valueOf(years));
			ageLabel2.setFont(new Font("Sans Serif", Font.PLAIN, 
					DisplayFrame.FONT_SIZE / 2));
			JLabel phoneNumberLabel2 = new JLabel(phoneNumber);
			phoneNumberLabel2.setFont(new Font("Sans Serif", Font.PLAIN, 
					DisplayFrame.FONT_SIZE / 2));
			JPanel details = new JPanel();
			details.setLayout(new FlowLayout());
			details.add(dobLabel1);
			details.add(dobLabel2);
			details.add(ageLabel1);
			details.add(ageLabel2);
			details.add(phoneNumberLabel1);
			details.add(phoneNumberLabel2);
			centerPanel.add(details);
			//health plan
			//health plan
			JPanel hpPanel = new JPanel();
			hpPanel.setLayout(new GridLayout(0, 2));
			String nameString = "No Health Plan";
			String checkupString = "Checkup: 0 out of 0";
			String repairString = "Repair: 0 out of 0";
			String hygieneString = "Hygiene: 0 out of 0";
			JButton hpButton = new JButton("Subscribe");
			if (this.patient.getUsage() != null) {
				//hp name
				try {
					Usage usage = this.patient.getUsage();
					nameString = usage.getHealthPlan().getName();
					HealthPlan hp;
					hp = new HealthPlan(nameString);
					//usage
					int currentCheckup = usage.getCheckUpUsed();
					int totalCheckup = hp.getCheckUpLevel();
					checkupString = "<html><strong>Checkup:</strong> " + String.valueOf(currentCheckup) + " out of " + String.valueOf(totalCheckup) + "</html>";
					int currentRepair = usage.getRepairUsed();
					int totalRepair = hp.getRepairLevel();
					repairString = "<html><strong>Repair:</strong> " + String.valueOf(currentRepair) + " out of " + String.valueOf(totalRepair) + "</html>";
					int currentHygiene = usage.getHygieneUsed();
					int totalHygiene = hp.getHygieneLevel();
					hygieneString = "<html><strong>Hygiene:</strong> " + String.valueOf(currentHygiene) + " out of " + String.valueOf(totalHygiene) + "</html>";
					hpButton.setText("Unsubscribe");
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
		    hpPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), border));
		    this.hpLabel = new JLabel(nameString);
		    this.hpLabel.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE / 2));
		    hpPanel.add(this.hpLabel);
		    this.checkupLabel = new JLabel(checkupString);
		    this.checkupLabel.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
		    hpPanel.add(this.checkupLabel);
		    this.repairLabel = new JLabel(repairString);
		    this.repairLabel.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
		    hpPanel.add(this.repairLabel);
		    this.hygieneLabel = new JLabel(hygieneString);
		    this.hygieneLabel.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
		    hpPanel.add(this.hygieneLabel);
		    centerPanel.add(hpPanel);
		    //buttons with patient actions
		    JPanel buttonsPanel = new JPanel();
		    buttonsPanel.setLayout(new GridLayout(1, 0));
		    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		    centerPanel.add(buttonsPanel);
		    //subscribe/un-subscribe
		    hpButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (hpButton.getText().equals("Subscribe")) {
						//show options
						try {
							ArrayList<HealthPlan> healthPlans = HealthPlan.getAllHealthPlans();
							String[] names = new String[healthPlans.size()];
							for (int i = 0; i < names.length; i++) {
								names[i] = healthPlans.get(i).getName();
							}
						    String input = (String) JOptionPane.showInputDialog(null, "Choose:",
						        "Subscribe to health plan", JOptionPane.QUESTION_MESSAGE, null,
						        names, // Array of choices
						        names[0]); // Initial choice
						    if (input != null) {
								//Subscribe to the health plan chosen
							    patient.subscribePatient(input);
							    //Reload this window
							    HealthPlan hp = new HealthPlan(input);
							    hpLabel.setText(hp.getName());
							    checkupLabel.setText("<html><strong>Checkup:</strong> 0 out of " + String.valueOf(hp.getCheckUpLevel()) + "</html>");
							    repairLabel.setText("<html><strong>Repair:</strong> 0 out of " + String.valueOf(hp.getRepairLevel()) + "</html>");
							    hygieneLabel.setText("<html><strong>Hygiene:</strong> 0 out of " + String.valueOf(hp.getHygieneLevel()) + "</html>");
							    hpButton.setText("Unsubscribe");
							    frame.revalidate();
						    }
						} catch (SQLException e) {
							JOptionPane.showMessageDialog(frame,
								    "Database error.",
								    "Error while fetching healthplans from db",
								    JOptionPane.ERROR_MESSAGE);
						}
					} else {
						// unsubscribe and refresh
						int selectedOption = JOptionPane.showConfirmDialog(null, "Do you wanna unsubscribe patient?", "Choose", JOptionPane.YES_NO_OPTION); 
			    		if (selectedOption == JOptionPane.YES_OPTION) {
			    			try {
			    				//unsubscribe
								patient.unsubscribePatient();
								//refresh panel
								hpLabel.setText("No Health Plan");
								checkupLabel.setText("Checkup: 0 out of 0");
								repairLabel.setText("Repair: 0 out of 0");
								hygieneLabel.setText("Hygiene: 0 out of 0");
								hpButton.setText("Subscribe");
								frame.revalidate();
							} catch (SQLException e) {
								JOptionPane.showMessageDialog(frame,
									    "Database error.",
									    "Error while unsubscribing",
									    JOptionPane.ERROR_MESSAGE);
							}
			    		}
					}
				}
		    });
		    buttonsPanel.add(hpButton);
		    //book appointment
		    JButton bookAppointmentButton = new JButton("Book Appointment");
		    bookAppointmentButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
		    });
		    buttonsPanel.add(bookAppointmentButton);
		    //delete
		    JButton deleteButton = new JButton("Delete");
		    deleteButton.addActionListener(new ActionListener() {
		    	@Override
		    	public void actionPerformed(ActionEvent arg0) {
		    		int selectedOption = JOptionPane.showConfirmDialog(null, "Do you want to delete this patient?", "Choose", JOptionPane.YES_NO_OPTION); 
		    		if (selectedOption == JOptionPane.YES_OPTION) {
		    			try {
							Patient.deletePatient(patient.getPatientID());
						} catch (SQLException e) {
							JOptionPane.showMessageDialog(frame,
								    "Database error.",
								    "Error while deleting",
								    JOptionPane.ERROR_MESSAGE);
						}
		    			frame.dispose();
		    		}
		    	}
		    });
		    buttonsPanel.add(deleteButton);
		    //update details
		    JButton updateButton = new JButton("Update");
		    updateButton.addActionListener(new ActionListener() {
		    	@Override
		    	public void actionPerformed(ActionEvent arg0) {
		    		
		    	}
		    });
		    buttonsPanel.add(updateButton);
		    //appointments
		    this.appointmentsPanel = new JPanel();
		    this.appointmentsPanel.setLayout(new GridLayout(0, 2));
		    JScrollPane appointmentsScrollPane = new JScrollPane(appointmentsPanel);
		    appointmentsScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.BLACK)));
		    this.appointmentCards = new ArrayList<JPanel>();
		    ArrayList<Appointment> list;
			list = Schedule.getAppointmentsByPatient(this.patient.getPatientID());
		    for (int i = 0; i < list.size(); i++) {
		    	addAppointment(list.get(i));
		    }
		    centerPanelHolder.add(appointmentsScrollPane);
		} catch (CommunicationsException e) {
			JOptionPane.showMessageDialog(frame,
				    "Database error. Check your internet connnection.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
				    e.getMessage(),
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void addAppointment(Appointment appointment) {
		try {
			this.appointmentCards.add(new JPanel());
			int index = this.appointmentCards.size() - 1;
			this.appointmentCards.get(index).setLayout(new BorderLayout());
			this.appointmentCards.get(index).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.black)));
			//Get Appointment details
			String appointmentType = appointment.getAppointmentType();
			Doctor doctor = appointment.getDoctor();
			String docName = doctor.getFirstName() + " " + doctor.getLastName();
			String appointmentStatus = "Single Appointment";
			if (appointment.getTotalAppointments() > 1) {
				appointmentStatus = "Appointment " + appointment.getCurrentAppointment() + " of " + appointment.getTotalAppointments();
			}
			LocalDateTime startTime = appointment.getStartTime().toLocalDateTime();
			String startString = String.valueOf(startTime.getHour()) + ":" + String.valueOf(startTime.getMinute());
			LocalDateTime endTime = appointment.getEndTime().toLocalDateTime();
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
			JButton detailsButton = new JButton("View");
			detailsButton.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 3));
			bottomRightSection.add(detailsButton);
			//TODO: Action listener
			JButton payButton = new JButton("Pay");
			payButton.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 3));
			bottomRightSection.add(payButton);
			bottomRightSection.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			JButton deleteButton = new JButton("Delete");
			deleteButton.setFont(new Font("Sans Serif", Font.PLAIN, 
					DisplayFrame.FONT_SIZE / 3));
			deleteButton.putClientProperty("Appointment", appointment);
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Appointment appointment = (Appointment) deleteButton.getClientProperty("Appointment");
					int selectedOption = JOptionPane.showConfirmDialog(null, "Do you wanna delete this appointment?", "Choose", JOptionPane.YES_NO_OPTION); 
		    		if (selectedOption == JOptionPane.YES_OPTION) {
		    			try {
							appointment.removeAppointment();
							try {
								//get requested day
								List<Appointment> appointmentList = Schedule.getAppointmentsByPatient(patient.getPatientID());
								appointmentCards.clear();
								appointmentsPanel.removeAll();
								appointmentsPanel.repaint();
								appointmentsPanel.setLayout(new GridLayout(0,2));
								if (appointmentList.size() > 0) {
									for (int i = 0; i < appointmentList.size(); i++) {
										addAppointment(appointmentList.get(i));
									}
								} else {
									//No appointments for today
									appointmentsPanel.setLayout(new BorderLayout());
									JLabel imgLabel = new JLabel(new ImageIcon(((new ImageIcon("resources/pictures/none_found.png")).getImage()).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH)), SwingConstants.CENTER);
									appointmentsPanel.add(imgLabel, BorderLayout.CENTER);
								}
								frame.revalidate();
							} catch (CommunicationsException e) {
								JOptionPane.showMessageDialog(frame,
									    "Check internet connection",
									    "Error",
									    JOptionPane.ERROR_MESSAGE);
							} catch (SQLException e) {
								JOptionPane.showMessageDialog(frame,
									    e.getMessage(),
									    "Error fetching appointments",
									    JOptionPane.ERROR_MESSAGE);
							}
						} catch (SQLException e) {
							JOptionPane.showMessageDialog(frame,
								    e.getMessage(),
								    "Error deleting appointment",
								    JOptionPane.ERROR_MESSAGE);
						}
		    		}
				}
			});
			bottomRightSection.add(deleteButton);
			//TODO: Action listener
			JPanel rightSection = new JPanel();
			rightSection.setLayout(new BoxLayout(rightSection, BoxLayout.Y_AXIS));
			rightSection.add(topRightSection);
			rightSection.add(bottomRightSection);
			this.appointmentCards.get(index).add(rightSection, BorderLayout.CENTER);
			this.appointmentsPanel.add(this.appointmentCards.get(index));
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
