/**
 * SecretaryView class
 * 
 * Class that represents a Secretary's view
 * @author John Ayad
 */
package com2002.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.NumberFormatter;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import com2002.interfaces.Screen;
import com2002.models.Address;
import com2002.models.Appointment;
import com2002.models.AppointmentType;
import com2002.models.DBQueries;
import com2002.models.Doctor;
import com2002.models.HealthPlan;
import com2002.models.Patient;
import com2002.models.Schedule;
import com2002.models.Secretary;
import com2002.models.Usage;
import lu.tudor.santec.jtimechooser.JTimeChooser;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class SecretaryView implements Screen {

	// instance variables
	private JPanel screen;
	private Secretary secretary;
	private DisplayFrame frame;
	// left panel
	private JPanel leftScreen;
	private JTabbedPane leftTabbedPane;
	// dentist tab
	private JPanel dentistAppointmentsScreen;
	private JScrollPane dentistAppointmentsScrollPane;
	private List<JPanel> dentistAppointmentCards;
	private JDatePickerImpl dentistDatePicker;
	private ArrayList<JPanel> dentistAppointmentsCardsColumn;
	private ArrayList<Appointment> dentistAppointmentsList;
	private JPanel dentistTab;
	// hygienist tab
	private JPanel hygienistAppointmentsScreen;
	private JScrollPane hygienistAppointmentsScrollPane;
	private List<JPanel> hygienistAppointmentCards;
	private JDatePickerImpl hygienistDatePicker;
	private ArrayList<JPanel> hygienistAppointmentsCardsColumn;
	private ArrayList<Appointment> hygienistAppointmentsList;
	private JPanel hygienistTab;
	// right panel
	private JPanel rightScreen;
	private JTabbedPane rightTabbedPane;
	// patients tab
	private List<JTextField> patientsTabInputs;
	private Object[][] patientsList;
	private JTable patientsTable;
	private DefaultTableModel patientsTableModel;
	// addresses tab
	private List<JTextField> addressesTabInputs;
	private Object[][] addressesList;
	private JTable addressesTable;
	private DefaultTableModel addressesTableModel;
	// register tab
	private List<Object> registerTabInputs;
	private JPanel registerTab;
	// booking tab
	private JPanel bookingTab;
	private List<Object> bookingTabInputs;

	/**
	 * Constructor used to initialize instance of the class
	 * 
	 * @param frame
	 *            DisplayFrame in which view is to be shown
	 * @param secretary
	 *            Secretary who is logged in
	 */
	public SecretaryView(DisplayFrame frame, Secretary secretary) {
		this.frame = frame;
		this.secretary = secretary;
		this.screen = new JPanel();
		this.screen.setLayout(new GridLayout(1, 2));
		initializeScreen();
	}

	/**
	 * Function used to initialize both sections of the view
	 */
	private void initializeScreen() {
		// left screen
		initializeLeftScreen();
		// right screen
		initializeRightScreen();
		// Add both to main screen
		this.screen.add(this.leftScreen);
		this.screen.add(this.rightScreen);
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
		JMenuItem healthPlan = new JMenu("Health Plans");
		JMenuItem refreshAllItem = new JMenuItem("Refresh all");
		refreshAllItem.setMnemonic(KeyEvent.VK_R);
		refreshAllItem.setToolTipText("Refresh all health plans as per today");
		refreshAllItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					// Get all patients with usages
					ArrayList<Usage> list = Usage.getAll();
					int countReset = 0;
					int countMonth = 0;
					for (int i = 0; i < list.size(); i++) {
						YearMonth m1 = YearMonth.from(list.get(i).getDateJoined());
						YearMonth m2 = YearMonth.from(LocalDateTime.now());
						String months = String.valueOf(m1.until(m2, ChronoUnit.MONTHS));
						if (Integer.valueOf(months) > list.get(i).getPaymentsIssued()) {
							// Assign invoices
							list.get(i).setPaymentsIssued(Integer.valueOf(months));
							DBQueries.execUpdate("INSERT INTO Payments (PatientID, AmountDue) VALUE('"
									+ list.get(i).getPatientID() + "', '"
									+ Double.valueOf(months) * list.get(i).getHealthPlan().getPrice() + "')");
							countMonth++;
						}
						if (list.get(i).resetHealthPlan()) {
							list.get(i).setPaymentsIssued(0);
							countReset++;
						}
					}
					if (countReset != 0) {
						JOptionPane.showMessageDialog(null,
								"Successfully reset health plans for " + countReset + " patients", "Success!",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "No health plans needed resetting", "Success!",
								JOptionPane.INFORMATION_MESSAGE);
					}
					if (countMonth != 0) {
						JOptionPane.showMessageDialog(null,
								"Successfully issued invoices for " + countMonth + " patients", "Success!",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "No patients needed any new invoices", "Success!",
								JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(frame, e.getMessage(), "Error refreshing health plans",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		healthPlan.add(refreshAllItem);
		menuBar.add(file);
		menuBar.add(healthPlan);
		frame.setJMenuBar(menuBar);
	}

	/**
	 * Function used to initialize left section of view
	 */
	private void initializeLeftScreen() {
		// get today's appointments
		this.frame.setFrameSize(DisplayFrame.DEFAULT_NUM, DisplayFrame.DEFAULT_NUM * 2);
		this.frame.centerFrame();
		// Left screen
		this.leftScreen = new JPanel();
		this.leftScreen.setLayout(new BorderLayout());
		this.leftScreen
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		// Title
		this.leftTabbedPane = new JTabbedPane();
		this.leftScreen.add(this.leftTabbedPane, BorderLayout.CENTER);
		this.leftTabbedPane.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
		// dentist tab
		initializeDentistTab();
		// hygienist tab
		initializeHygienistTab();
	}

	/**
	 * Function used to initialize dentist's appointments tab in left section
	 */
	private void initializeDentistTab() {
		try {
			// dentist tab
			this.dentistTab = new JPanel();
			this.dentistTab.setLayout(new BorderLayout());
			this.leftTabbedPane.addTab("Dentist", this.dentistTab);
			JLabel titleTab = new JLabel("Dentist Appointments", SwingConstants.CENTER);
			titleTab.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE));
			// searching
			JLabel patientFirstNameLabel = new JLabel("Patient First Name:", SwingConstants.CENTER);
			patientFirstNameLabel.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			JTextField patientFirstNameField = new JTextField(10);
			patientFirstNameField.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			JLabel patientLastNameLabel = new JLabel("Patient Last Name:", SwingConstants.CENTER);
			patientLastNameLabel.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			JTextField patientLastNameField = new JTextField(10);
			patientLastNameField.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			JButton searchButton = new JButton("Search");
			searchButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			searchButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// What are we searching for?
					String partialPatientFirstName = patientFirstNameField.getText();
					String partialPatientLastName = patientLastNameField.getText();
					try {
						// Search for it
						String docUsername = DBQueries.getData("Username", "Employees", "Role", "Dentist");
						// use this list to refresh our tab
						try {
							// new starting date
							Date selectedDate = (Date) dentistDatePicker.getModel().getValue();
							dentistAppointmentsScreen.removeAll();
							dentistAppointmentsCardsColumn.clear();
							dentistAppointmentCards.clear();
							dentistAppointmentsList.clear();
							frame.repaint();
							dentistTab.repaint();
							String dayNames[] = new DateFormatSymbols().getWeekdays();
							for (int i = 0; i < 7; i++) {
								// initialize appointments list
								dentistAppointmentsList = Schedule.getAppointmentsByDocAndNameAndDate(
										new Doctor(docUsername), partialPatientFirstName, partialPatientLastName,
										new Date(selectedDate.getTime() + ((1000 * 60 * 60 * 24) * i)));
								// sort them
								Collections.sort(dentistAppointmentsList, new Comparator<Appointment>() {
									@Override
									public int compare(Appointment o1, Appointment o2) {
										return o1.getStartTime().compareTo(o2.getStartTime());
									}
								});
								// set time of calendar to obtain day name and day number
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(new Date(selectedDate.getTime() + ((1000 * 60 * 60 * 24) * i)));
								// title of each day's column
								JLabel dayName = new JLabel(dayNames[calendar.get(Calendar.DAY_OF_WEEK)] + " "
										+ calendar.get(Calendar.DAY_OF_MONTH), SwingConstants.CENTER);
								dayName.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE));
								// actual appointments if any
								dentistAppointmentsCardsColumn.add(new JPanel());
								dentistAppointmentsCardsColumn.get(i)
										.setBorder(BorderFactory.createLineBorder(Color.BLACK));
								if (dentistAppointmentsList.size() > 0) {
									dentistAppointmentsCardsColumn.get(i).setLayout(
											new BoxLayout(dentistAppointmentsCardsColumn.get(i), BoxLayout.Y_AXIS));
									JPanel dayPanel = new JPanel();
									dayPanel.setLayout(new BorderLayout());
									dayPanel.add(dayName, BorderLayout.CENTER);
									dentistAppointmentsCardsColumn.get(i).add(dayPanel);
									for (int j = 0; j < dentistAppointmentsList.size(); j++) {
										addAppointment(dentistAppointmentsList.get(j), i, "Dentist");
									}
									dentistAppointmentsScreen.add(dentistAppointmentsCardsColumn.get(i));
								} else {
									// No appointments for today
									dentistAppointmentsCardsColumn.get(i).setLayout(new BorderLayout());
									dentistAppointmentsCardsColumn.get(i).add(dayName, BorderLayout.NORTH);
								}
								dentistAppointmentsScreen.add(dentistAppointmentsCardsColumn.get(i));
							}
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error fetching appointments",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching appointments",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			JButton viewAllButton = new JButton("Refresh (View All)");
			viewAllButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			viewAllButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					refreshDentistTab();
				}

			});
			JPanel searchPanel = new JPanel();
			searchPanel.add(patientFirstNameLabel);
			searchPanel.add(patientFirstNameField);
			searchPanel.add(patientLastNameLabel);
			searchPanel.add(patientLastNameField);
			searchPanel.add(searchButton);
			JPanel northPanel = new JPanel();
			northPanel.setLayout(new GridLayout(0, 1));
			northPanel.add(titleTab);
			northPanel.add(searchPanel);
			this.dentistTab.add(northPanel, BorderLayout.NORTH);
			this.dentistAppointmentsScreen = new JPanel();
			this.dentistAppointmentsScrollPane = new JScrollPane(this.dentistAppointmentsScreen);
			this.dentistAppointmentsScrollPane.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.black)));
			this.dentistAppointmentsScreen.setLayout(new GridLayout(1, 0));
			this.dentistAppointmentsCardsColumn = new ArrayList<JPanel>();
			this.dentistAppointmentCards = new ArrayList<JPanel>();
			Calendar calendar = Calendar.getInstance();
			Date dateGiven = calendar.getTime();
			String dayNames[] = new DateFormatSymbols().getWeekdays();
			this.dentistAppointmentsList = new ArrayList<Appointment>();
			for (int i = 0; i < 7; i++) {
				// initialize appointments list
				String dentistName = DBQueries.getData("Username", "Employees", "Role", "Dentist");
				Doctor doc = new Doctor(dentistName);
				this.dentistAppointmentsList = Schedule.getAppointmentsByDoctorAndDay(doc,
						new Date(dateGiven.getTime() + ((1000 * 60 * 60 * 24) * i)));
				// sort them
				Collections.sort(dentistAppointmentsList, new Comparator<Appointment>() {
					@Override
					public int compare(Appointment o1, Appointment o2) {
						return o1.getStartTime().compareTo(o2.getStartTime());
					}
				});
				// set time of calendar to obtain day name and day number
				calendar.setTime(new Date(dateGiven.getTime() + ((1000 * 60 * 60 * 24) * i)));
				// title of each day's column
				JLabel dayName = new JLabel(
						dayNames[calendar.get(Calendar.DAY_OF_WEEK)] + " " + calendar.get(Calendar.DAY_OF_MONTH),
						SwingConstants.CENTER);
				dayName.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE));
				// actual appointments if any
				this.dentistAppointmentsCardsColumn.add(new JPanel());
				this.dentistAppointmentsCardsColumn.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK));
				if (this.dentistAppointmentsList.size() > 0) {
					this.dentistAppointmentsCardsColumn.get(i)
							.setLayout(new BoxLayout(this.dentistAppointmentsCardsColumn.get(i), BoxLayout.Y_AXIS));
					JPanel dayPanel = new JPanel();
					dayPanel.setLayout(new BorderLayout());
					dayPanel.add(dayName, BorderLayout.CENTER);
					this.dentistAppointmentsCardsColumn.get(i).add(dayPanel);
					for (int j = 0; j < this.dentistAppointmentsList.size(); j++) {
						addAppointment(this.dentistAppointmentsList.get(j), i, "Dentist");
					}
					this.dentistAppointmentsScreen.add(this.dentistAppointmentsCardsColumn.get(i));
				} else {
					// No appointments for today
					this.dentistAppointmentsCardsColumn.get(i).setLayout(new BorderLayout());
					this.dentistAppointmentsCardsColumn.get(i).add(dayName, BorderLayout.NORTH);
				}
				this.dentistAppointmentsScreen.add(this.dentistAppointmentsCardsColumn.get(i));
			}
			this.dentistTab.add(this.dentistAppointmentsScrollPane, BorderLayout.CENTER);
			// Date picker and new appointment
			JPanel bottomLeftPanel = new JPanel();
			bottomLeftPanel.setLayout(new BorderLayout());
			// date picker
			UtilDateModel model = new UtilDateModel();
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			this.dentistDatePicker = new JDatePickerImpl(datePanel);

			// set by default today
			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			model.setSelected(true);
			model.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					if (e.getPropertyName().equals("value")) {
						refreshDentistTab();
					}
				}
			});
			// Bottom bit
			JLabel labelDatePicker = new JLabel("Week from day: ");
			labelDatePicker.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			JPanel datePickerPanel = new JPanel();
			datePickerPanel.add(labelDatePicker);
			datePickerPanel.add(this.dentistDatePicker);
			bottomLeftPanel.add(datePickerPanel, BorderLayout.WEST);
			bottomLeftPanel.add(viewAllButton, BorderLayout.EAST);
			bottomLeftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			this.dentistTab.add(bottomLeftPanel, BorderLayout.SOUTH);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Function used to initialize hygienist's appointments tab in left section
	 */
	private void initializeHygienistTab() {
		try {
			// dentist tab
			this.hygienistTab = new JPanel();
			this.hygienistTab.setLayout(new BorderLayout());
			this.leftTabbedPane.addTab("Hygienist", this.hygienistTab);
			JLabel titleTab = new JLabel("Hygienist Appointments", SwingConstants.CENTER);
			titleTab.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE));
			// searching
			JLabel patientFirstNameLabel = new JLabel("Patient First Name:", SwingConstants.CENTER);
			patientFirstNameLabel.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			JTextField patientFirstNameField = new JTextField(10);
			patientFirstNameField.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			JLabel patientLastNameLabel = new JLabel("Patient Last Name:", SwingConstants.CENTER);
			patientLastNameLabel.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			JTextField patientLastNameField = new JTextField(10);
			patientLastNameField.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			JButton searchButton = new JButton("Search");
			searchButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			searchButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// What are we searching for?
					String partialPatientFirstName = patientFirstNameField.getText();
					String partialPatientLastName = patientLastNameField.getText();
					try {
						// Search for it
						String docUsername = DBQueries.getData("Username", "Employees", "Role", "Hygienist");
						// use this list to refresh our tab
						try {
							// new starting date
							Date selectedDate = (Date) hygienistDatePicker.getModel().getValue();
							hygienistAppointmentsScreen.removeAll();
							hygienistAppointmentsCardsColumn.clear();
							hygienistAppointmentCards.clear();
							hygienistAppointmentsList.clear();
							frame.repaint();
							hygienistTab.repaint();
							String dayNames[] = new DateFormatSymbols().getWeekdays();
							for (int i = 0; i < 7; i++) {
								// initialize appointments list
								hygienistAppointmentsList = Schedule.getAppointmentsByDocAndNameAndDate(
										new Doctor(docUsername), partialPatientFirstName, partialPatientLastName,
										new Date(selectedDate.getTime() + ((1000 * 60 * 60 * 24) * i)));
								// sort them
								Collections.sort(hygienistAppointmentsList, new Comparator<Appointment>() {
									@Override
									public int compare(Appointment o1, Appointment o2) {
										return o1.getStartTime().compareTo(o2.getStartTime());
									}
								});
								// set time of calendar to obtain day name and day number
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(new Date(selectedDate.getTime() + ((1000 * 60 * 60 * 24) * i)));
								// title of each day's column
								JLabel dayName = new JLabel(dayNames[calendar.get(Calendar.DAY_OF_WEEK)] + " "
										+ calendar.get(Calendar.DAY_OF_MONTH), SwingConstants.CENTER);
								dayName.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE));
								// actual appointments if any
								hygienistAppointmentsCardsColumn.add(new JPanel());
								hygienistAppointmentsCardsColumn.get(i)
										.setBorder(BorderFactory.createLineBorder(Color.BLACK));
								if (hygienistAppointmentsList.size() > 0) {
									hygienistAppointmentsCardsColumn.get(i).setLayout(
											new BoxLayout(hygienistAppointmentsCardsColumn.get(i), BoxLayout.Y_AXIS));
									JPanel dayPanel = new JPanel();
									dayPanel.setLayout(new BorderLayout());
									dayPanel.add(dayName, BorderLayout.CENTER);
									hygienistAppointmentsCardsColumn.get(i).add(dayPanel);
									for (int j = 0; j < hygienistAppointmentsList.size(); j++) {
										addAppointment(hygienistAppointmentsList.get(j), i, "Hygienist");
									}
									hygienistAppointmentsScreen.add(hygienistAppointmentsCardsColumn.get(i));
								} else {
									// No appointments for today
									hygienistAppointmentsCardsColumn.get(i).setLayout(new BorderLayout());
									hygienistAppointmentsCardsColumn.get(i).add(dayName, BorderLayout.NORTH);
								}
								hygienistAppointmentsScreen.add(hygienistAppointmentsCardsColumn.get(i));
							}
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error fetching appointments",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching appointments",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			JButton viewAllButton = new JButton("Refresh (View All)");
			viewAllButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			viewAllButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					refreshDentistTab();
				}
			});
			JPanel searchPanel = new JPanel();
			searchPanel.add(patientFirstNameLabel);
			searchPanel.add(patientFirstNameField);
			searchPanel.add(patientLastNameLabel);
			searchPanel.add(patientLastNameField);
			searchPanel.add(searchButton);
			JPanel northPanel = new JPanel();
			northPanel.setLayout(new GridLayout(0, 1));
			northPanel.add(titleTab);
			northPanel.add(searchPanel);
			this.hygienistTab.add(northPanel, BorderLayout.NORTH);
			// appointments
			this.hygienistAppointmentsScreen = new JPanel();
			this.hygienistAppointmentsScrollPane = new JScrollPane(this.hygienistAppointmentsScreen);
			this.hygienistAppointmentsScrollPane.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.black)));
			this.hygienistAppointmentsScreen.setLayout(new GridLayout(1, 0));
			this.hygienistAppointmentsCardsColumn = new ArrayList<JPanel>();
			this.hygienistAppointmentCards = new ArrayList<JPanel>();
			Calendar calendar = Calendar.getInstance();
			Date dateGiven = calendar.getTime();
			String dayNames[] = new DateFormatSymbols().getWeekdays();
			this.hygienistAppointmentsList = new ArrayList<Appointment>();
			String hygienistName = DBQueries.getData("Username", "Employees", "Role", "Hygienist");
			Doctor doc = new Doctor(hygienistName);
			for (int i = 0; i < 7; i++) {
				// initialize appointments list
				this.hygienistAppointmentsList = Schedule.getAppointmentsByDoctorAndDay(doc,
						new Date(dateGiven.getTime() + ((1000 * 60 * 60 * 24) * i)));
				// sort them
				Collections.sort(hygienistAppointmentsList, new Comparator<Appointment>() {
					@Override
					public int compare(Appointment o1, Appointment o2) {
						return o1.getStartTime().compareTo(o2.getStartTime());
					}
				});
				// set time of calendar to obtain day name and day number
				calendar.setTime(new Date(dateGiven.getTime() + ((1000 * 60 * 60 * 24) * i)));
				// title of each day's column
				JLabel dayName = new JLabel(
						dayNames[calendar.get(Calendar.DAY_OF_WEEK)] + " " + calendar.get(Calendar.DAY_OF_MONTH),
						SwingConstants.CENTER);
				dayName.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE));
				// actual appointments if any
				this.hygienistAppointmentsCardsColumn.add(new JPanel());
				this.hygienistAppointmentsCardsColumn.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK));
				if (this.hygienistAppointmentsList.size() > 0) {
					this.hygienistAppointmentsCardsColumn.get(i)
							.setLayout(new BoxLayout(this.hygienistAppointmentsCardsColumn.get(i), BoxLayout.Y_AXIS));
					JPanel dayPanel = new JPanel();
					dayPanel.setLayout(new BorderLayout());
					dayPanel.add(dayName, BorderLayout.CENTER);
					this.hygienistAppointmentsCardsColumn.get(i).add(dayPanel);
					for (int j = 0; j < this.hygienistAppointmentsList.size(); j++) {
						addAppointment(this.hygienistAppointmentsList.get(j), i, "Hygienist");
					}
					this.hygienistAppointmentsScreen.add(this.hygienistAppointmentsCardsColumn.get(i));
				} else {
					// No appointments for today
					this.hygienistAppointmentsCardsColumn.get(i).setLayout(new BorderLayout());
					this.hygienistAppointmentsCardsColumn.get(i).add(dayName, BorderLayout.NORTH);
				}
				this.hygienistAppointmentsScreen.add(this.hygienistAppointmentsCardsColumn.get(i));
			}
			this.hygienistTab.add(this.hygienistAppointmentsScrollPane, BorderLayout.CENTER);
			// Date picker and new appointment
			JPanel bottomLeftPanel = new JPanel();
			bottomLeftPanel.setLayout(new BorderLayout());
			// date picker
			UtilDateModel model = new UtilDateModel();
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			this.hygienistDatePicker = new JDatePickerImpl(datePanel);

			// set by default today
			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(today);
			model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			model.setSelected(true);
			model.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					if (e.getPropertyName().equals("value")) {
						refreshHygienistTab();
					}
				}
			});
			// Bottom bit
			JLabel labelDatePicker = new JLabel("Week from day: ");
			labelDatePicker.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			JPanel datePickerPanel = new JPanel();
			datePickerPanel.add(labelDatePicker);
			datePickerPanel.add(this.hygienistDatePicker);
			bottomLeftPanel.add(datePickerPanel, BorderLayout.WEST);
			bottomLeftPanel.add(viewAllButton, BorderLayout.EAST);
			bottomLeftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			this.hygienistTab.add(bottomLeftPanel, BorderLayout.SOUTH);
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error fetching appointments",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Function used to initialize right section of the view
	 */
	private void initializeRightScreen() {
		// right screen
		this.rightScreen = new JPanel();
		this.rightScreen.setLayout(new BorderLayout());
		this.rightTabbedPane = new JTabbedPane();
		this.rightTabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.rightScreen.add(rightTabbedPane, BorderLayout.CENTER);
		this.rightTabbedPane.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
		// Register tab
		initializeRegisterTab();
		// Patients tab
		initializePatientsTab();
		// Addresses tab
		initializeAddressesTab();
		// Booking tab
		initializeBookingTab();
	}

	/**
	 * Function used to initialize booking tab in right section
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initializeBookingTab() {
		try {
			this.bookingTab = new JPanel();
			this.bookingTab.setLayout(new BorderLayout());
			this.bookingTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			this.rightTabbedPane.addTab("Book Appointment", this.bookingTab);
			// title
			JLabel titleTab = new JLabel("Appointment Booking", SwingConstants.CENTER);
			titleTab.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE));
			this.bookingTab.add(titleTab, BorderLayout.NORTH);
			// content
			JPanel content = new JPanel();
			content.setLayout(new GridLayout(0, 2));
			JPanel inputsAndButtons = new JPanel();
			inputsAndButtons.setLayout(new FlowLayout());
			JScrollPane inputsAndButtonsScrollPane = new JScrollPane(inputsAndButtons);
			this.bookingTab.add(inputsAndButtonsScrollPane, BorderLayout.CENTER);
			// inputs
			this.bookingTabInputs = new ArrayList<Object>();
			// StartDate
			// day
			UtilDateModel model = new UtilDateModel();
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			Date today = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(today.getTime() + (1000 * 60 * 60 * 24)));
			model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			model.setSelected(true);
			JDatePickerImpl startDate = new JDatePickerImpl(datePanel);
			this.bookingTabInputs.add(startDate);
			// time
			JTimeChooser startTime = new JTimeChooser();
			this.bookingTabInputs.add(startTime);
			// EndDate
			// day
			UtilDateModel model2 = new UtilDateModel();
			JDatePanelImpl datePanel2 = new JDatePanelImpl(model2);
			model2.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			model2.setSelected(true);
			JDatePickerImpl endDate = new JDatePickerImpl(datePanel2);
			endDate.getComponent(1).setEnabled(false);
			this.bookingTabInputs.add(endDate);
			// time
			JTimeChooser endTime = new JTimeChooser();
			this.bookingTabInputs.add(endTime);
			// Doctor
			ArrayList<Doctor> allDoctors = Doctor.getAll();
			String[] doctorNames = new String[allDoctors.size()];
			for (int i = 0; i < allDoctors.size(); i++) {
				doctorNames[i] = "Dr. " + allDoctors.get(i).getFirstName() + " " + allDoctors.get(i).getLastName();
			}
			JComboBox doctorList = new JComboBox(doctorNames);
			doctorList.setSelectedIndex(0);
			doctorList.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			this.bookingTabInputs.add(doctorList);
			// Types
			String[] types = { "Checkup", "Cleaning", "Empty", "Remedial" };
			JComboBox typesList = new JComboBox(types);
			typesList.setSelectedIndex(0);
			typesList.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			this.bookingTabInputs.add(typesList);
			// Patient
			// firstname
			JTextField firstName = new JTextField();
			firstName.setToolTipText("First Name");
			firstName.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			firstName.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (firstName.getText().length() >= 30)
						e.consume();
				}
			});
			this.bookingTabInputs.add(firstName);
			// housenumber
			JTextField houseNumber = new JTextField();
			houseNumber.setToolTipText("House number");
			houseNumber.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			houseNumber.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (houseNumber.getText().length() >= 30)
						e.consume();
				}
			});
			this.bookingTabInputs.add(houseNumber);
			// postcode
			JTextField postCode = new JTextField();
			postCode.setToolTipText("Post code");
			postCode.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			postCode.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (postCode.getText().length() >= 30)
						e.consume();
				}
			});
			this.bookingTabInputs.add(postCode);
			NumberFormat numFormat = new DecimalFormat("#0"); // Format of data in phoneNumber
			// CurrentAppointments
			NumberFormatter numFormatter = new NumberFormatter(numFormat);
			JFormattedTextField currentAppointment = new JFormattedTextField(numFormatter);
			currentAppointment.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			currentAppointment.setText("0");
			this.bookingTabInputs.add(currentAppointment);
			// TotalAppointments
			JFormattedTextField totalAppointments = new JFormattedTextField(numFormatter);
			totalAppointments.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			totalAppointments.setText("0");
			this.bookingTabInputs.add(totalAppointments);
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
			// end day
			JLabel label3 = new JLabel("End Day");
			label3.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label3);
			content.add(endDate);
			endDate.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
					if (!((String) typesList.getSelectedItem()).equals("Empty")) { // Not Empty appointment
						// enable the below two fields
						currentAppointment.setEnabled(true);
						totalAppointments.setEnabled(true);
					}
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
			// patient name
			JLabel label9 = new JLabel("Patient first name");
			label9.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label9.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label9);
			content.add(firstName);
			// patient houseNumber
			JLabel label10 = new JLabel("Patient house number");
			label10.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label10.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label10);
			content.add(houseNumber);
			// patient postcode
			JLabel label11 = new JLabel("Patient Postcode");
			label11.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label11.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label11);
			content.add(postCode);
			// add all to all
			inputsAndButtons.add(content);
			this.bookingTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// Book button
			JButton bookButton = new JButton("Book Appointment");
			bookButton.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE));
			bookButton.setEnabled(false);
			bookButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Attempt registration
					// Extract details
					Date selectedStartDay = (Date) startDate.getModel().getValue();
					Date selectedEndDay = (Date) endDate.getModel().getValue();
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
					String patientName = firstName.getText();
					String patientHouseNum = houseNumber.getText();
					String patientPostCode = postCode.getText();
					// 1) verify patient exists
					try {
						Patient patient = new Patient(patientName, patientHouseNum, patientPostCode);
						if (patient.getFirstName() != null) { // exists!
							LocalDate localDate = selectedStartDay.toInstant().atZone(ZoneId.systemDefault())
									.toLocalDate();
							String year = String.valueOf(localDate.getYear());
							String month = String.valueOf(localDate.getMonthValue());
							String day = String.valueOf(localDate.getDayOfMonth());
							Date workingDayStart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
									.parse(year + "-" + month + "-" + day + " 09:00:00");
							Date chosenStart = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
									.parse(year + "-" + month + "-" + day + " " + timeStartString);
							chosenStart = endTime.getDateWithTime(chosenStart);
							Date workingDayEnd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
									.parse(year + "-" + month + "-" + day + " 17:00:00");
							Date chosenEnd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
									.parse(year + "-" + month + "-" + day + " " + timeEndString);
							chosenEnd = endTime.getDateWithTime(chosenEnd);
							String nameOfDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(workingDayStart);
							if ((chosenStart.before(workingDayStart) || chosenStart.after(workingDayEnd)
									|| chosenEnd.before(workingDayStart) || chosenEnd.after(workingDayEnd)
									|| nameOfDay.equals("Saturday") || nameOfDay.equals("Sunday"))
									&& !typeName.equals("Empty")) {
								JOptionPane.showMessageDialog(frame,
										"Appointment must be within allowed times (9AM to 5PM) (Monday to Friday)",
										"Error", JOptionPane.ERROR_MESSAGE);
							} else {
								// 2) attempt booking
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
								if (typeName.equals("Empty")) { // Appointments extend on multiple days only if empty
									localDate = selectedEndDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
									year = String.valueOf(localDate.getYear());
									month = String.valueOf(localDate.getMonthValue());
									day = String.valueOf(localDate.getDayOfMonth());
									ts2 = Timestamp.valueOf(year + "-" + month + "-" + day + " " + timeEndString);
								}
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
								default:
									new Appointment(ts1, ts2, docUsername, patient.getPatientID(), "",
											AppointmentType.EMPTY, totalAppointmentsNum, currentAppointmentNum);
									break;
								}
								// refresh appointments tab
								if (new Doctor(docUsername).getRole().equals("Dentist")) {
									refreshDentistTab();
								} else {
									refreshHygienistTab();
								}
								currentAppointment.setText("");
								totalAppointments.setText("");
								firstName.setText("");
								houseNumber.setText("");
								postCode.setText("");
								JOptionPane.showMessageDialog(null, "Successfully added Appointment", "Success!",
										JOptionPane.INFORMATION_MESSAGE);
								frame.revalidate();
							}
						} else {
							JOptionPane.showMessageDialog(frame, "Patient doesn't exist", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (CommunicationsException e) {
						JOptionPane.showMessageDialog(frame, "Error connecting to the internet", "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching data from db",
								JOptionPane.ERROR_MESSAGE);
					} catch (ParseException e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			this.bookingTab.add(bookButton, BorderLayout.SOUTH);
			// Validation
			for (int i = 6; i < bookingTabInputs.size(); i++) {
				((Component) bookingTabInputs.get(i)).addKeyListener(new KeyAdapter() {

					public void keyTyped(KeyEvent e) {
						if (firstName.getText().length() > 0 && houseNumber.getText().length() > 0
								&& postCode.getText().length() > 0) {
							bookButton.setEnabled(true);
						} else {
							bookButton.setEnabled(false);
						}
					}
				});
			}
			typesList.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (((String) typesList.getSelectedItem()).equals("Empty")) { // Empty appointment
						// disable all of the other fields
						currentAppointment.setEnabled(false);
						totalAppointments.setEnabled(false);
						firstName.setEnabled(false);
						houseNumber.setEnabled(false);
						postCode.setEnabled(false);
						bookButton.setEnabled(true);
						endDate.getComponent(1).setEnabled(true);
					} else {
						// enable input
						currentAppointment.setEnabled(true);
						totalAppointments.setEnabled(true);
						firstName.setEnabled(true);
						houseNumber.setEnabled(true);
						postCode.setEnabled(true);
						endDate.getComponent(1).setEnabled(false);
					}
					frame.revalidate();
				}
			});
		} catch (

		SQLException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching data from db",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Function used to initialize register tab in right section
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initializeRegisterTab() {
		try {
			// register tab
			this.registerTab = new JPanel();
			this.registerTab.setLayout(new BorderLayout());
			this.rightTabbedPane.addTab("Register", this.registerTab);
			// title
			JLabel titleTab = new JLabel("Patient Registration", SwingConstants.CENTER);
			titleTab.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE));
			this.registerTab.add(titleTab, BorderLayout.NORTH);
			// content of registration
			JPanel content = new JPanel();
			content.setLayout(new GridLayout(0, 2));
			JPanel inputsAndButtons = new JPanel();
			inputsAndButtons.setLayout(new FlowLayout());
			JScrollPane inputsAndButtonsScrollPane = new JScrollPane(inputsAndButtons);
			this.registerTab.add(inputsAndButtonsScrollPane, BorderLayout.CENTER);
			// inputs
			this.registerTabInputs = new ArrayList<Object>();
			// title
			JTextField title = new JTextField();
			title.setToolTipText("Title");
			title.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			title.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (title.getText().length() >= 4)
						e.consume();
				}
			});
			this.registerTabInputs.add(title);
			// firstname
			JTextField firstName = new JTextField();
			firstName.setToolTipText("First Name");
			firstName.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			firstName.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (firstName.getText().length() >= 30)
						e.consume();
				}
			});
			this.registerTabInputs.add(firstName);
			// lastname
			JTextField lastName = new JTextField();
			lastName.setToolTipText("Last Name");
			lastName.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			lastName.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (lastName.getText().length() >= 30)
						e.consume();
				}
			});
			this.registerTabInputs.add(lastName);
			// phonenumber
			NumberFormat numFormat = new DecimalFormat("#0"); // Format of data in phoneNumber
			NumberFormatter numFormatter = new NumberFormatter(numFormat);
			JFormattedTextField phoneNumber = new JFormattedTextField(numFormatter);
			phoneNumber.setToolTipText("Phone number");
			phoneNumber.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			phoneNumber.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (phoneNumber.getText().length() >= 20)
						e.consume();
				}
			});
			this.registerTabInputs.add(phoneNumber);
			// date of birth
			UtilDateModel model = new UtilDateModel();
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			JDatePickerImpl dateOfBirthPicker = new JDatePickerImpl(datePanel);
			this.registerTabInputs.add(dateOfBirthPicker);
			// housenumber
			JTextField houseNumber = new JTextField();
			houseNumber.setToolTipText("House number");
			houseNumber.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			houseNumber.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (houseNumber.getText().length() >= 30)
						e.consume();
				}
			});
			this.registerTabInputs.add(houseNumber);
			// street name
			JTextField streetName = new JTextField();
			streetName.setToolTipText("Street name");
			streetName.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			streetName.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (streetName.getText().length() >= 30)
						e.consume();
				}
			});
			this.registerTabInputs.add(streetName);
			// district
			JTextField district = new JTextField();
			district.setToolTipText("District");
			district.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			district.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (district.getText().length() >= 30)
						e.consume();
				}
			});
			this.registerTabInputs.add(district);
			// city
			JTextField city = new JTextField();
			city.setToolTipText("City");
			city.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			city.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (city.getText().length() >= 30)
						e.consume();
				}
			});
			this.registerTabInputs.add(city);
			// postcode
			JTextField postCode = new JTextField();
			postCode.setToolTipText("Post code");
			postCode.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			postCode.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if (postCode.getText().length() >= 30)
						e.consume();
				}
			});
			this.registerTabInputs.add(postCode);
			// add them to the tab with their respective titles
			JLabel label1 = new JLabel("Title");
			label1.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label1);
			content.add(title);
			JLabel label2 = new JLabel("First name:");
			label2.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label2);
			content.add(firstName);
			JLabel label3 = new JLabel("Last name:");
			label3.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label3);
			content.add(lastName);
			JLabel label4 = new JLabel("Phone Number:");
			label4.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label4.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label4);
			content.add(phoneNumber);
			JLabel label5 = new JLabel("Date of Birth:");
			label5.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label5.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label5);
			dateOfBirthPicker.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(dateOfBirthPicker);
			JLabel label6 = new JLabel("House Number:");
			label6.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label6.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label6);
			content.add(houseNumber);
			JLabel label7 = new JLabel("Street name:");
			label7.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label7.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label7);
			content.add(streetName);
			JLabel label8 = new JLabel("District:");
			label8.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label8.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label8);
			content.add(district);
			JLabel label9 = new JLabel("City:");
			label9.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label9.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label9);
			content.add(city);
			JLabel label10 = new JLabel("Postcode:");
			label10.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label10.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label10);
			content.add(postCode);
			inputsAndButtons.add(content);
			this.registerTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// health plan list
			String[] healthPlanNames = healthPlanConverter(HealthPlan.getAllHealthPlans());
			JList list = new JList(healthPlanNames);
			list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			list.setVisibleRowCount(-1);
			list.setSelectedIndex(0);
			// Health plan radio buttons
			JRadioButton option1 = new JRadioButton("Subscribe to HealthPlan");
			option1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// enable list
					list.setEnabled(true);
					frame.revalidate();
				}
			});
			JRadioButton option2 = new JRadioButton("No Subscription");
			option2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// disable list
					list.setEnabled(false);
					frame.revalidate();
				}
			});
			ButtonGroup group = new ButtonGroup();
			option1.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			option1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			option1.setSelected(true);
			option2.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			option2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			group.add(option1);
			group.add(option2);
			content.add(option1);
			content.add(option2);
			JLabel label11 = new JLabel("Health Plan:");
			label11.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			label11.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label11);
			content.add(list);
			// Button
			JButton register = new JButton("Register");
			register.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE));
			register.setEnabled(false);
			register.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Obtain data
					String title = ((JTextField) registerTabInputs.get(0)).getText();
					String fname = ((JTextField) registerTabInputs.get(1)).getText();
					String lname = ((JTextField) registerTabInputs.get(2)).getText();
					String phone = ((JFormattedTextField) registerTabInputs.get(3)).getText();
					Date date = (Date) ((JDatePickerImpl) registerTabInputs.get(4)).getModel().getValue();
					String houseNum = ((JTextField) registerTabInputs.get(5)).getText();
					String streetName = ((JTextField) registerTabInputs.get(6)).getText();
					String district = ((JTextField) registerTabInputs.get(7)).getText();
					String city = ((JTextField) registerTabInputs.get(8)).getText();
					String postcode = ((JTextField) registerTabInputs.get(9)).getText();
					String hpName = "";
					if (option1.isSelected()) {
						hpName = (String) list.getSelectedValue();
					}
					// Attempt registration on hopes of success
					try {
						if (!Address.dbHasAddress(houseNum, postcode)) { // register Address
							secretary.registerAddress(houseNum, streetName, district, city, postcode);
						}
						// register patient
						secretary.registerPatient(title, fname, lname,
								LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)), phone, houseNum,
								postcode);
						if (!hpName.equals("")) {
							Patient patient = new Patient(fname, houseNum, postcode);
							secretary.subscribePatient(patient, hpName);
						}
						for (int i = 0; i < registerTabInputs.size(); i++) {
							if (i != 4 && i != 3) { // not the date picker or phoneNum
								((JTextField) registerTabInputs.get(i)).setText("");
							} else if (i == 3) { // phoneNum
								((JFormattedTextField) registerTabInputs.get(i)).setText("");
							}
						}
						JOptionPane.showMessageDialog(null, "Successfully added Patient", "Success!",
								JOptionPane.INFORMATION_MESSAGE);
						// refresh patients list
						try {
							ArrayList<Patient> patientsFound = Patient.getAllPatients();
							patientsList = patientListConverter(patientsFound);
							patientsTableModel.setRowCount(0);
							for (int i = 0; i < patientsList.length; i++) {
								patientsTableModel.addRow(patientsList[i]);
							}
							patientsTabInputs.get(0).setText("");
							patientsTabInputs.get(1).setText("");
							patientsTabInputs.get(2).setText("");
							patientsTabInputs.get(3).setText("");
							frame.revalidate();
						} catch (Exception e4) {
							JOptionPane.showMessageDialog(frame, e4.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
						// refresh addresses list
						try {
							ArrayList<Address> addressesFound = Address.getAllAddresses();
							addressesList = addressListConverter(addressesFound);
							addressesTableModel.setRowCount(0);
							for (int i = 0; i < addressesList.length; i++) {
								addressesTableModel.addRow(addressesList[i]);
							}
							addressesTabInputs.get(0).setText("");
							addressesTabInputs.get(1).setText("");
							addressesTabInputs.get(2).setText("");
							addressesTabInputs.get(3).setText("");
							addressesTabInputs.get(4).setText("");
							frame.revalidate();
						} catch (Exception e5) {
							JOptionPane.showMessageDialog(frame, e5.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						}
						frame.revalidate();
					} catch (CommunicationsException e1) {
						JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			this.registerTab.add(register, BorderLayout.SOUTH);
			// Validation there is input in all fields
			for (int i = 0; i < registerTabInputs.size(); i++) {
				if (i != 4) { // not the date picker
					((Component) registerTabInputs.get(i)).addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent e) {
							if (title.getText().length() > 0 && firstName.getText().length() > 0
									&& lastName.getText().length() > 0 && phoneNumber.getText().length() > 0
									&& houseNumber.getText().length() > 0 && streetName.getText().length() > 0
									&& district.getText().length() > 0 && city.getText().length() > 0
									&& postCode.getText().length() > 0) {
								register.setEnabled(true);
							}
						}
					});
				}
			}
		} catch (CommunicationsException e1) {
			JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error fetching data from db",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Function used to convert a health plan arrayList to an array used by JTables
	 * 
	 * @param allHealthPlans
	 *            ArrayList of healthplans
	 * @return String array containing names of health plans
	 */
	private String[] healthPlanConverter(ArrayList<HealthPlan> allHealthPlans) {
		String[] list = new String[allHealthPlans.size()];
		for (int i = 0; i < allHealthPlans.size(); i++) {
			list[i] = allHealthPlans.get(i).getName();
		}
		return list;
	}

	/**
	 * Function used to initialize addresses tab in right section
	 */
	@SuppressWarnings("serial")
	private void initializeAddressesTab() {
		try {
			// addresses tab
			JPanel addressesTab = new JPanel();
			this.rightTabbedPane.addTab("Addresses", addressesTab);
			addressesTab.setLayout(new BorderLayout());
			addressesTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// searching
			JPanel addressesTabSearchPanel = new JPanel();
			addressesTabSearchPanel.setLayout(new GridLayout(0, 2));
			JPanel inputsAndButtons = new JPanel();
			inputsAndButtons.setLayout(new FlowLayout());
			addressesTab.add(inputsAndButtons, BorderLayout.NORTH);
			// inputs
			this.addressesTabInputs = new ArrayList<JTextField>();
			JTextField houseNumber = new JTextField();
			houseNumber.setToolTipText("House number");
			houseNumber.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			this.addressesTabInputs.add(houseNumber);
			JTextField streetName = new JTextField();
			streetName.setToolTipText("Street name");
			streetName.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			this.addressesTabInputs.add(streetName);
			JTextField district = new JTextField();
			district.setToolTipText("District");
			district.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			this.addressesTabInputs.add(district);
			JTextField city = new JTextField();
			city.setToolTipText("City");
			city.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			this.addressesTabInputs.add(city);
			JTextField postcode = new JTextField();
			postcode.setToolTipText("Post code");
			postcode.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			this.addressesTabInputs.add(postcode);
			// add them to the tab
			JLabel label1 = new JLabel("House number:");
			label1.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			addressesTabSearchPanel.add(label1);
			addressesTabSearchPanel.add(houseNumber);
			JLabel label2 = new JLabel("Street name:");
			label2.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			addressesTabSearchPanel.add(label2);
			addressesTabSearchPanel.add(streetName);
			JLabel label3 = new JLabel("District:");
			label3.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			addressesTabSearchPanel.add(label3);
			addressesTabSearchPanel.add(district);
			JLabel label4 = new JLabel("City:");
			label4.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			addressesTabSearchPanel.add(label4);
			addressesTabSearchPanel.add(city);
			JLabel label5 = new JLabel("Postcode:");
			label5.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			addressesTabSearchPanel.add(label5);
			addressesTabSearchPanel.add(postcode);
			inputsAndButtons.add(addressesTabSearchPanel);
			// button
			JButton searchAddressesButton = new JButton("Search");
			searchAddressesButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			searchAddressesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Search and return list of patients
					String hnum = addressesTabInputs.get(0).getText();
					String sname = addressesTabInputs.get(1).getText();
					String dist = addressesTabInputs.get(2).getText();
					String city = addressesTabInputs.get(3).getText();
					String pcode = addressesTabInputs.get(4).getText();
					try {
						ArrayList<Address> addressesFound = secretary.searchAddresses(hnum, sname, dist, city, pcode);
						addressesList = addressListConverter(addressesFound);
						addressesTableModel.setRowCount(0);
						for (int i = 0; i < addressesList.length; i++) {
							addressesTableModel.addRow(addressesList[i]);
						}
						frame.revalidate();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching addresses",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			searchAddressesButton.setMnemonic(KeyEvent.VK_ENTER);
			inputsAndButtons.add(searchAddressesButton);
			// view all button
			JButton viewAllButton = new JButton("Refresh (View all)");
			viewAllButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			viewAllButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						ArrayList<Address> addressesFound = Address.getAllAddresses();
						addressesList = addressListConverter(addressesFound);
						addressesTableModel.setRowCount(0);
						for (int i = 0; i < addressesList.length; i++) {
							addressesTableModel.addRow(addressesList[i]);
						}
						addressesTabInputs.get(0).setText("");
						addressesTabInputs.get(1).setText("");
						addressesTabInputs.get(2).setText("");
						addressesTabInputs.get(3).setText("");
						addressesTabInputs.get(4).setText("");
						frame.revalidate();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching addresses",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			inputsAndButtons.add(viewAllButton);
			// actual addresses
			this.addressesList = addressListConverter(Address.getAllAddresses());
			String[] columnTitles = { "House Number", "Street Name", "District", "City", "Postcode", "Actions" };
			this.addressesTableModel = new DefaultTableModel(this.addressesList, columnTitles) {
				@Override
				public boolean isCellEditable(int row, int col) {
					// only the last column
					return col == 5;
				}
			};
			this.addressesTable = new JTable(this.addressesTableModel);
			this.addressesTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
			this.addressesTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), this.frame));
			JScrollPane addressesScrollPane = new JScrollPane(this.addressesTable);
			this.addressesTable.setFillsViewportHeight(true);
			addressesTab.add(addressesScrollPane, BorderLayout.CENTER);
		} catch (CommunicationsException e) {
			JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching addresses", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Function used to initialize patients tab in right section
	 */
	@SuppressWarnings("serial")
	private void initializePatientsTab() {
		try {
			// patients tab
			JPanel patientsTab = new JPanel();
			patientsTab.setLayout(new BorderLayout());
			patientsTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			// searching
			JPanel patientsTabSearchPanel = new JPanel();
			patientsTabSearchPanel.setLayout(new GridLayout(0, 2));
			JPanel inputsAndButtons = new JPanel();
			inputsAndButtons.setLayout(new FlowLayout());
			patientsTab.add(inputsAndButtons, BorderLayout.NORTH);
			// inputs
			this.patientsTabInputs = new ArrayList<JTextField>();
			JTextField firstName = new JTextField();
			firstName.setToolTipText("Patient First Name");
			firstName.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			this.patientsTabInputs.add(firstName);
			JTextField lastName = new JTextField();
			lastName.setToolTipText("Patient Last Name");
			lastName.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			this.patientsTabInputs.add(lastName);
			JTextField houseNumber = new JTextField();
			houseNumber.setToolTipText("House number");
			houseNumber.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			this.patientsTabInputs.add(houseNumber);
			JTextField postCode = new JTextField();
			postCode.setToolTipText("Post code");
			postCode.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			this.patientsTabInputs.add(postCode);
			// add them to the tab
			JLabel label1 = new JLabel("First name:");
			label1.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			patientsTabSearchPanel.add(label1);
			patientsTabSearchPanel.add(firstName);
			JLabel label2 = new JLabel("Last name:");
			label2.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			patientsTabSearchPanel.add(label2);
			patientsTabSearchPanel.add(lastName);
			JLabel label3 = new JLabel("House Number:");
			label3.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			patientsTabSearchPanel.add(label3);
			patientsTabSearchPanel.add(houseNumber);
			JLabel label4 = new JLabel("Postcode:");
			label4.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE / 2));
			patientsTabSearchPanel.add(label4);
			patientsTabSearchPanel.add(postCode);
			inputsAndButtons.add(patientsTabSearchPanel);
			// button
			JButton searchPatientsButton = new JButton("Search");
			searchPatientsButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			searchPatientsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// Search and return list of patients
					String fname = patientsTabInputs.get(0).getText();
					String lname = patientsTabInputs.get(1).getText();
					String hnum = patientsTabInputs.get(2).getText();
					String pcode = patientsTabInputs.get(3).getText();
					try {
						ArrayList<Patient> patientsFound = secretary.searchPatients(fname, lname, hnum, pcode);
						patientsList = patientListConverter(patientsFound);
						patientsTableModel.setRowCount(0);
						for (int i = 0; i < patientsList.length; i++) {
							patientsTableModel.addRow(patientsList[i]);
						}
						frame.revalidate();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			inputsAndButtons.add(searchPatientsButton);
			// view all button
			JButton viewAllPatientsButton = new JButton("Refresh (View all)");
			viewAllPatientsButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 2));
			viewAllPatientsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						ArrayList<Patient> patientsFound = Patient.getAllPatients();
						patientsList = patientListConverter(patientsFound);
						patientsTableModel.setRowCount(0);
						for (int i = 0; i < patientsList.length; i++) {
							patientsTableModel.addRow(patientsList[i]);
						}
						patientsTabInputs.get(0).setText("");
						patientsTabInputs.get(1).setText("");
						patientsTabInputs.get(2).setText("");
						patientsTabInputs.get(3).setText("");
						frame.revalidate();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Error ", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			inputsAndButtons.add(viewAllPatientsButton);
			// actual patients
			this.patientsList = patientListConverter(Patient.getAllPatients());
			String[] columnTitlesPatients = { "ID", "Title", "First Name", "Last Name", "Date Of Birth", "House Number",
					"Postcode", "Telephone", "Actions" };
			this.patientsTableModel = new DefaultTableModel(this.patientsList, columnTitlesPatients) {
				@Override
				public boolean isCellEditable(int row, int col) {
					// only the last column
					return col == 8;
				}
			};
			this.patientsTable = new JTable(this.patientsTableModel);
			this.patientsTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
			this.patientsTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), this.frame));
			JScrollPane patientsScrollPane = new JScrollPane(patientsTable);
			this.patientsTable.setFillsViewportHeight(true);
			patientsTab.add(patientsScrollPane, BorderLayout.CENTER);
			this.rightTabbedPane.addTab("Patients", patientsTab);
		} catch (CommunicationsException e) {
			JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching patients", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Function used to refresh content of hygienist tab
	 */
	private void refreshHygienistTab() {
		try {
			// new starting date
			Date selectedDate = (Date) hygienistDatePicker.getModel().getValue();
			hygienistAppointmentsScreen.removeAll();
			hygienistAppointmentsCardsColumn.clear();
			hygienistAppointmentCards.clear();
			hygienistAppointmentsList.clear();
			frame.repaint();
			hygienistTab.repaint();
			String dayNames[] = new DateFormatSymbols().getWeekdays();
			String hygienistName = DBQueries.getData("Username", "Employees", "Role", "Hygienist");
			Doctor doc = new Doctor(hygienistName);
			for (int i = 0; i < 7; i++) {
				// initialize appointments list
				hygienistAppointmentsList = Schedule.getAppointmentsByDoctorAndDay(doc,
						new Date(selectedDate.getTime() + ((1000 * 60 * 60 * 24) * i)));
				// sort them
				Collections.sort(hygienistAppointmentsList, new Comparator<Appointment>() {
					@Override
					public int compare(Appointment o1, Appointment o2) {
						return o1.getStartTime().compareTo(o2.getStartTime());
					}
				});
				// set time of calendar to obtain day name and day number
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date(selectedDate.getTime() + ((1000 * 60 * 60 * 24) * i)));
				// title of each day's column
				JLabel dayName = new JLabel(
						dayNames[calendar.get(Calendar.DAY_OF_WEEK)] + " " + calendar.get(Calendar.DAY_OF_MONTH),
						SwingConstants.CENTER);
				dayName.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE));
				// actual appointments if any
				hygienistAppointmentsCardsColumn.add(new JPanel());
				hygienistAppointmentsCardsColumn.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK));
				if (hygienistAppointmentsList.size() > 0) {
					hygienistAppointmentsCardsColumn.get(i)
							.setLayout(new BoxLayout(hygienistAppointmentsCardsColumn.get(i), BoxLayout.Y_AXIS));
					JPanel dayPanel = new JPanel();
					dayPanel.setLayout(new BorderLayout());
					dayPanel.add(dayName, BorderLayout.CENTER);
					hygienistAppointmentsCardsColumn.get(i).add(dayPanel);
					for (int j = 0; j < hygienistAppointmentsList.size(); j++) {
						addAppointment(hygienistAppointmentsList.get(j), i, "Hygienist");
					}
					hygienistAppointmentsScreen.add(hygienistAppointmentsCardsColumn.get(i));
				} else {
					// No appointments for today
					hygienistAppointmentsCardsColumn.get(i).setLayout(new BorderLayout());
					hygienistAppointmentsCardsColumn.get(i).add(dayName, BorderLayout.NORTH);
				}
				hygienistAppointmentsScreen.add(hygienistAppointmentsCardsColumn.get(i));
			}
		} catch (CommunicationsException e1) {
			JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error fetching appointments",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Function used to refresh content of dentist tab
	 */
	private void refreshDentistTab() {
		try {
			// new starting date
			Date selectedDate = (Date) dentistDatePicker.getModel().getValue();
			dentistAppointmentsScreen.removeAll();
			dentistAppointmentsCardsColumn.clear();
			dentistAppointmentCards.clear();
			dentistAppointmentsList.clear();
			frame.repaint();
			dentistTab.repaint();
			String dayNames[] = new DateFormatSymbols().getWeekdays();
			String dentistName = DBQueries.getData("Username", "Employees", "Role", "Dentist");
			Doctor doc = new Doctor(dentistName);
			for (int i = 0; i < 7; i++) {
				// initialize appointments list
				dentistAppointmentsList = Schedule.getAppointmentsByDoctorAndDay(doc,
						new Date(selectedDate.getTime() + ((1000 * 60 * 60 * 24) * i)));
				// sort them
				Collections.sort(dentistAppointmentsList, new Comparator<Appointment>() {
					@Override
					public int compare(Appointment o1, Appointment o2) {
						return o1.getStartTime().compareTo(o2.getStartTime());
					}
				});
				// set time of calendar to obtain day name and day number
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date(selectedDate.getTime() + ((1000 * 60 * 60 * 24) * i)));
				// title of each day's column
				JLabel dayName = new JLabel(
						dayNames[calendar.get(Calendar.DAY_OF_WEEK)] + " " + calendar.get(Calendar.DAY_OF_MONTH),
						SwingConstants.CENTER);
				dayName.setFont(new Font("Sans Serif", Font.BOLD, DisplayFrame.FONT_SIZE));
				// actual appointments if any
				dentistAppointmentsCardsColumn.add(new JPanel());
				dentistAppointmentsCardsColumn.get(i).setBorder(BorderFactory.createLineBorder(Color.BLACK));
				if (dentistAppointmentsList.size() > 0) {
					dentistAppointmentsCardsColumn.get(i)
							.setLayout(new BoxLayout(dentistAppointmentsCardsColumn.get(i), BoxLayout.Y_AXIS));
					JPanel dayPanel = new JPanel();
					dayPanel.setLayout(new BorderLayout());
					dayPanel.add(dayName, BorderLayout.CENTER);
					dentistAppointmentsCardsColumn.get(i).add(dayPanel);
					for (int j = 0; j < dentistAppointmentsList.size(); j++) {
						addAppointment(dentistAppointmentsList.get(j), i, "Dentist");
					}
					dentistAppointmentsScreen.add(dentistAppointmentsCardsColumn.get(i));
				} else {
					// No appointments for today
					dentistAppointmentsCardsColumn.get(i).setLayout(new BorderLayout());
					dentistAppointmentsCardsColumn.get(i).add(dayName, BorderLayout.NORTH);
				}
				dentistAppointmentsScreen.add(dentistAppointmentsCardsColumn.get(i));
			}
		} catch (CommunicationsException e1) {
			JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(frame, e1.getMessage(), "Error fetching appointments",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Function used to add an appointment
	 * 
	 * @param app
	 *            Appointment to add
	 * @param col
	 *            which column to add it in (week-view)
	 * @param role
	 *            which doctor are we adding appointments to ("Hygienist" or
	 *            "Dentist")
	 */
	private void addAppointment(Appointment app, int col, String role) {
		try {
			int index = 0;
			if (role.equals("Dentist")) {
				this.dentistAppointmentCards.add(new JPanel());
				index = this.dentistAppointmentCards.size() - 1;
				this.dentistAppointmentCards.get(index).setLayout(new BorderLayout());
				this.dentistAppointmentCards.get(index).setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.black)));
			} else {
				this.hygienistAppointmentCards.add(new JPanel());
				index = this.hygienistAppointmentCards.size() - 1;
				this.hygienistAppointmentCards.get(index).setLayout(new BorderLayout());
				this.hygienistAppointmentCards.get(index).setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createLineBorder(Color.black)));
			}
			// Get Appointment details
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
			String startString = String.format("%tH:%tM", startTime, startTime);
			String startDayString = String.format("%tD", startTime);
			LocalDateTime endTime = app.getEndTime().toLocalDateTime();
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
			if (appointmentType.equals("Empty")) {
				leftSection.add(startD);
			}
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
			if (role.equals("Dentist")) {
				this.dentistAppointmentCards.get(index).add(leftSection, BorderLayout.WEST);
			} else {
				this.hygienistAppointmentCards.get(index).add(leftSection, BorderLayout.WEST);
			}
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
			JButton detailsButton = new JButton("Details");
			detailsButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 3));
			detailsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DisplayFrame newFrame = new DisplayFrame();
					newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					AppointmentView appointmentView = new AppointmentView(newFrame, app, AppointmentView.SECRETARY);
					newFrame.setDisplayedPanel(appointmentView.getPanel());
				}
			});
			bottomRightSection.add(detailsButton);
			// Patient Info Button
			JButton patientInfoButton = new JButton("Patient Info");
			patientInfoButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 3));
			patientInfoButton.putClientProperty("id", patient.getPatientID());
			patientInfoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						Patient patient = new Patient((int) patientInfoButton.getClientProperty("id"));
						DisplayFrame patientViewFrame = new DisplayFrame();
						PatientView patientView = new PatientView(patientViewFrame, patient, AppointmentView.SECRETARY);
						patientViewFrame.setDisplayedPanel(patientView.getPanel());
					} catch (CommunicationsException e) {
						JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error",
								JOptionPane.ERROR_MESSAGE);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			JButton deleteButton = new JButton("Cancel");
			deleteButton.setFont(new Font("Sans Serif", Font.PLAIN, DisplayFrame.FONT_SIZE / 3));
			deleteButton.putClientProperty("Appointment", app);
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Appointment appointment = (Appointment) deleteButton.getClientProperty("Appointment");
					int selectedOption = JOptionPane.showConfirmDialog(null, "Do you wanna cancel this appointment?",
							"Choose", JOptionPane.YES_NO_OPTION);
					if (selectedOption == JOptionPane.YES_OPTION) {
						try {
							Doctor doc = appointment.getDoctor();
							appointment.removeAppointment();
							if (doc.getRole().equals("Dentist")) {
								refreshDentistTab();
							} else {
								refreshHygienistTab();
							}
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
			if (app.getAppointmentType().equals("Empty")) {
				detailsButton.setEnabled(false);
				patientInfoButton.setEnabled(false);
			}
			bottomRightSection.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			JPanel rightSection = new JPanel();
			rightSection.setLayout(new BoxLayout(rightSection, BoxLayout.Y_AXIS));
			rightSection.add(topRightSection);
			rightSection.add(bottomRightSection);
			if (role.equals("Dentist")) {
				this.dentistAppointmentCards.get(index).add(rightSection, BorderLayout.CENTER);
				this.dentistAppointmentsCardsColumn.get(col).add(this.dentistAppointmentCards.get(index));
			} else {
				this.hygienistAppointmentCards.get(index).add(rightSection, BorderLayout.CENTER);
				this.hygienistAppointmentsCardsColumn.get(col).add(this.hygienistAppointmentCards.get(index));
			}
		} catch (CommunicationsException e) {
			JOptionPane.showMessageDialog(frame, "Not connected to internet", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "Error fetching patient", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Function used to convert arraylist of patients to a 2D array used by JTables
	 * 
	 * @param givenList
	 *            arraylist of patients
	 * @return 2D Object array containing strings representing values of patients
	 */
	private Object[][] patientListConverter(ArrayList<Patient> givenList) {
		Object[][] patientArr = new Object[givenList.size()][9];
		for (int i = 0; i < givenList.size(); i++) {
			patientArr[i] = new Object[9];
			patientArr[i][0] = String.valueOf(givenList.get(i).getPatientID());
			patientArr[i][1] = givenList.get(i).getTitle();
			patientArr[i][2] = givenList.get(i).getFirstName();
			patientArr[i][3] = givenList.get(i).getLastName();
			patientArr[i][4] = givenList.get(i).getDateOfBirth().toString();
			patientArr[i][5] = givenList.get(i).getHouseNumber();
			patientArr[i][6] = givenList.get(i).getPostcode();
			patientArr[i][7] = givenList.get(i).getPhoneNumber();
			patientArr[i][8] = "View";
		}
		return patientArr;
	}

	/**
	 * Function used to convert arraylist of addresses to a 2D array used by JTables
	 * 
	 * @param givenList
	 *            arraylist of addresses
	 * @return 2D Object array containing strings representing values of addresses
	 */
	private Object[][] addressListConverter(ArrayList<Address> givenList) {
		Object[][] addressArr = new Object[givenList.size()][6];
		for (int i = 0; i < givenList.size(); i++) {
			addressArr[i] = new String[6];
			addressArr[i][0] = givenList.get(i).getHouseNumber();
			addressArr[i][1] = givenList.get(i).getStreetName();
			addressArr[i][2] = givenList.get(i).getDistrict();
			addressArr[i][3] = givenList.get(i).getCity();
			addressArr[i][4] = givenList.get(i).getPostcode();
			addressArr[i][5] = "View Patients";
		}
		return addressArr;
	}

	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}

/**
 * Code snippet from following tutorial
 * http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm
 */
@SuppressWarnings("serial")
class ButtonRenderer extends JButton implements TableCellRenderer {
	public ButtonRenderer() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(UIManager.getColor("Button.background"));
		}
		setText((value == null) ? "" : value.toString());
		return this;
	}
}

/**
 * Code snippet from following tutorial
 * http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm
 */
@SuppressWarnings("serial")
class ButtonEditor extends DefaultCellEditor {
	protected JButton button;
	private String label;
	private boolean isPushed;
	private JTable table;
	private int row;
	private DisplayFrame frame;

	public ButtonEditor(JCheckBox checkBox, DisplayFrame frame) {
		super(checkBox);
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
		this.frame = frame;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.table = table;
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}
		this.row = row;
		label = (value == null) ? "" : value.toString();
		button.setText(label);
		isPushed = true;
		return button;
	}

	public Object getCellEditorValue() {
		if (isPushed) {
			if (label.equals("View")) { // Patients action listener
				try {
					Patient patient = new Patient(Integer.valueOf((String) this.table.getValueAt(this.row, 0)));
					DisplayFrame patientViewFrame = new DisplayFrame();
					PatientView patientView = new PatientView(patientViewFrame, patient, AppointmentView.SECRETARY);
					patientViewFrame.setDisplayedPanel(patientView.getPanel());
				} catch (CommunicationsException e) {
					JOptionPane.showMessageDialog(this.frame, "Not connected to internet", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(this.frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else if (label.equals("Pay Subscription")) {
				try {
					String patientId = String.valueOf(this.table.getValueAt(this.row, 0));
					String amountDue = String.valueOf(this.table.getValueAt(this.row, 1));
					DBQueries.execUpdate(
							"DELETE FROM `Payments` WHERE (`PatientID`='" + patientId + "' AND `AmountDue`='"
									+ amountDue + "' AND `StartDate` IS NULL AND `Username` IS NULL) LIMIT 1");
					JOptionPane.showMessageDialog(null, "Successfully paid health plan subscription", "Success!",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (CommunicationsException e) {
					JOptionPane.showMessageDialog(this.frame, "Not connected to internet", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(this.frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else if (label.equals("Pay Appointment")) {
				try {
					String tsString = String.valueOf(this.table.getValueAt(this.row, 2));
					String username = String.valueOf(this.table.getValueAt(this.row, 4));
					PayView pv = new PayView(frame, new Appointment(Timestamp.valueOf(tsString), username));
					frame.setDisplayedPanel(pv.getPanel());
				} catch (CommunicationsException e) {
					JOptionPane.showMessageDialog(this.frame, "Not connected to internet", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(this.frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			} else { // Addresses action listener
				try {
					String hnum = (String) this.table.getValueAt(this.row, 0);
					String pcode = (String) this.table.getValueAt(this.row, 4);
					Address address = new Address(hnum, pcode);
					DisplayFrame addressViewFrame = new DisplayFrame();
					AddressView addressView = new AddressView(addressViewFrame, address);
					addressViewFrame.setDisplayedPanel(addressView.getPanel());
				} catch (CommunicationsException e) {
					JOptionPane.showMessageDialog(this.frame, "Not connected to internet", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(this.frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		isPushed = false;
		return new String(label);
	}

	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}
