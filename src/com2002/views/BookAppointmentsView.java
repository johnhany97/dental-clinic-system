/**
 * BookAppointmentsView class
 * 
 * Class that represents a view that is used to book appointments
 * @author John Ayad
 */
package com2002.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import com2002.interfaces.Screen;
import com2002.models.Appointment;
import com2002.models.AppointmentType;
import com2002.models.Doctor;
import com2002.models.Patient;
import lu.tudor.santec.jtimechooser.JTimeChooser;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class BookAppointmentsView implements Screen {

	// instance variables
	private JPanel screen;
	private DisplayFrame frame;
	private Patient patient;
	private ArrayList<Object> inputs;

	/**
	 * Constructor
	 * 
	 * Instantiates an instance of this view
	 * 
	 * @param frame
	 *            DisplayFrame in which class is to be viewed
	 * @param patient
	 *            Patient whom we are to book an appointment for
	 */
	public BookAppointmentsView(DisplayFrame frame, Patient patient) {
		this.frame = frame;
		this.patient = patient;
		initialize();
	}

	/**
	 * Function used to instantiate components and add their action listeners
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		try {
			this.screen = new JPanel();
			this.screen.setLayout(new BorderLayout());
			this.screen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			// title
			JLabel titleTab = new JLabel("Appointment Booking", SwingConstants.CENTER);
			titleTab.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE));
			this.screen.add(titleTab, BorderLayout.NORTH);
			// content
			JPanel content = new JPanel();
			content.setLayout(new GridLayout(0, 2));
			JPanel inputsAndButtons = new JPanel();
			inputsAndButtons.setLayout(new FlowLayout());
			JScrollPane inputsAndButtonsScrollPane = new JScrollPane(inputsAndButtons);
			this.screen.add(inputsAndButtonsScrollPane, BorderLayout.CENTER);
			// inputs
			this.inputs = new ArrayList<Object>();
			// StartDate
			// day
			UtilDateModel model = new UtilDateModel();
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			JDatePickerImpl startDate = new JDatePickerImpl(datePanel);
			Calendar cal = Calendar.getInstance();
			Date today = new Date();
			cal.setTime(new Date(today.getTime() + (1000 * 60 * 60 * 24)));
			model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			model.setSelected(true);
			this.inputs.add(startDate);
			// time
			JTimeChooser startTime = new JTimeChooser();
			this.inputs.add(startTime);
			// time
			JTimeChooser endTime = new JTimeChooser();
			this.inputs.add(endTime);
			// Doctor
			ArrayList<Doctor> allDoctors = Doctor.getAll();
			String[] doctorNames = new String[allDoctors.size()];
			for (int i = 0; i < allDoctors.size(); i++) {
				doctorNames[i] = "Dr. " + allDoctors.get(i).getFirstName() + " " + allDoctors.get(i).getLastName();
			}
			JComboBox doctorList = new JComboBox(doctorNames);
			doctorList.setSelectedIndex(0);
			doctorList.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			this.inputs.add(doctorList);
			// Types
			String[] types = { "Checkup", "Cleaning", "Remedial" };
			JComboBox typesList = new JComboBox(types);
			typesList.setSelectedIndex(0);
			typesList.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			this.inputs.add(typesList);
			// Patient
			NumberFormat numFormat = new DecimalFormat("#0"); // Format of data in phoneNumber
			// CurrentAppointments
			NumberFormatter numFormatter = new NumberFormatter(numFormat);
			JFormattedTextField currentAppointment = new JFormattedTextField(numFormatter);
			currentAppointment.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			currentAppointment.setText("0");
			this.inputs.add(currentAppointment);
			// TotalAppointments
			JFormattedTextField totalAppointments = new JFormattedTextField(numFormatter);
			totalAppointments.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			totalAppointments.setText("0");
			this.inputs.add(totalAppointments);
			// Add all of the above + labels to the tab's content panel
			// start day
			JLabel label1 = new JLabel("Start Day");
			label1.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label1);
			content.add(startDate);
			startDate.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// start time
			JLabel label2 = new JLabel("Start Time");
			label2.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label2);
			content.add(startTime);
			startTime.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// end time
			JLabel label4 = new JLabel("End Time");
			label4.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label4.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label4);
			content.add(endTime);
			endTime.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Doctor
			JLabel label5 = new JLabel("Doctor");
			label5.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label5.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label5);
			content.add(doctorList);
			doctorList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// type
			JLabel label6 = new JLabel("Appointment type");
			label6.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label6.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label6);
			content.add(typesList);
			typesList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Course treatment prompt
			JRadioButton option1 = new JRadioButton("Course Treatment");
			option1.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			option1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(option1);
			JRadioButton option2 = new JRadioButton("Single Appointment");
			option2.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			option2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(option2);
			ButtonGroup group = new ButtonGroup();
			group.add(option1);
			group.add(option2);
			option1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// enable the below two fields
					currentAppointment.setEnabled(true);
					totalAppointments.setEnabled(true);
				}
			});
			option2.setSelected(true);
			currentAppointment.setEnabled(false);
			totalAppointments.setEnabled(false);
			option2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Disable the below two fields
					currentAppointment.setEnabled(false);
					totalAppointments.setEnabled(false);
				}
			});
			// current appointment
			JLabel label7 = new JLabel("Current appointment Number");
			label7.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label7.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label7);
			content.add(currentAppointment);
			// total appointment
			JLabel label8 = new JLabel("Total number of appoinments");
			label8.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label8.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label8);
			content.add(totalAppointments);
			// add all to all
			inputsAndButtons.add(content);
			this.screen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Book button
			JButton bookButton = new JButton("Book Appointment");
			bookButton.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE));
			bookButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Attempt registration
					// Extract details
					Date selectedStartDay = (Date) startDate.getModel().getValue();
					String timeStartString = startTime.getFormatedTime();
					String timeEndString = endTime.getFormatedTime();
					String docName = (String) doctorList.getSelectedItem();
					String docUsername = "";
					String typeName = (String) typesList.getSelectedItem();
					boolean isCourseTreatment = option1.isSelected();
					int currentAppointmentNum = 0;
					int totalAppointmentsNum = 0;
					if (isCourseTreatment) {
						currentAppointmentNum = Integer.valueOf(currentAppointment.getText());
						totalAppointmentsNum = Integer.valueOf(totalAppointments.getText());
					}
					LocalDate localDate = selectedStartDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					String year = String.valueOf(localDate.getYear());
					String month = String.valueOf(localDate.getMonthValue());
					String day = String.valueOf(localDate.getDayOfMonth());
					try {
						Date workingDayStart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.parse(year + "-" + month + "-" + day + " 09:00:00");
						Date chosenStart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.parse(year + "-" + month + "-" + day + " " + timeStartString);
						Date workingDayEnd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.parse(year + "-" + month + "-" + day + " 17:00:00");
						Date chosenEnd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.parse(year + "-" + month + "-" + day + " " + timeEndString);
						if (chosenStart.before(workingDayStart) || chosenStart.after(workingDayEnd)
								|| chosenEnd.before(workingDayStart) || chosenEnd.after(workingDayEnd)) {
							JOptionPane.showMessageDialog(frame,
									"Appointment must be within allowed times (9AM to 5PM)", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							try {
								// attempt booking
								ArrayList<Doctor> listOfDoctors = Doctor.getAll();
								for (int i = 0; i < listOfDoctors.size(); i++) {
									String titleOfDoc = "Dr. " + listOfDoctors.get(i).getFirstName() + " "
											+ listOfDoctors.get(i).getLastName();
									if (titleOfDoc.equals(docName)) {
										docUsername = listOfDoctors.get(i).getUsername();
									}
								}
								Timestamp ts1 = Timestamp
										.valueOf(year + "-" + month + "-" + day + " " + timeStartString);
								Timestamp ts2 = Timestamp.valueOf(year + "-" + month + "-" + day + " " + timeEndString);
								switch (typeName) {
								case "Checkup":
									new Appointment(ts1, ts2, docUsername, patient.getPatientID(), "",
											AppointmentType.CHECKUP, totalAppointmentsNum, currentAppointmentNum);
									break;
								case "Remedial":
									new Appointment(ts1, ts2, docUsername, patient.getPatientID(), "",
											AppointmentType.REMEDIAL, totalAppointmentsNum, currentAppointmentNum);
									break;
								case "Cleaning":
									new Appointment(ts1, ts2, docUsername, patient.getPatientID(), "",
											AppointmentType.CLEANING, totalAppointmentsNum, currentAppointmentNum);
									break;
								}
								// refresh parent frame
								JOptionPane.showMessageDialog(null, "Successfully added appointment", "Success!",
										JOptionPane.INFORMATION_MESSAGE);
								frame.dispose();
							} catch (CommunicationsException e) {
								JOptionPane.showMessageDialog(frame, e.getMessage(), "Error connecting to internet",
										JOptionPane.ERROR_MESSAGE);
							} catch (SQLException e) {
								JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching data from db",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					} catch (ParseException e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			this.screen.add(bookButton, BorderLayout.SOUTH);
		} catch (CommunicationsException e) {
			JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching data from db",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
