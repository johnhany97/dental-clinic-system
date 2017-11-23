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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
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
import com2002.models.Doctor;
import com2002.models.HealthPlan;
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
	private JDatePickerImpl datePicker;
	//right panel
	private JPanel rightScreen;
	private JTabbedPane tabbedPane;
	//patients tab
	private List<JTextField> patientsTabInputs;
	private Object[][] patientsList;
	private JTable patientsTable;
	private DefaultTableModel patientsTableModel;
	//addresses tab
	private List<JTextField> addressesTabInputs;
	private Object[][] addressesList;
	private JTable addressesTable;
	private DefaultTableModel addressesTableModel;
	//register tab
	private List<Object> registerTabInputs;
	private JPanel registerTab;
	
	public SecretaryView(DisplayFrame frame, Secretary secretary) {
		this.frame = frame;
		this.secretary = secretary;
		this.screen = new JPanel();
		this.screen.setLayout(new GridLayout(1, 2));
		initializeScreen();
	}

	private void initializeScreen() {
		//left screen
		initializeLeftScreen();
		//right screen
		initializeRightScreen();
		//Add both to main screen
		this.screen.add(this.leftScreen);
		this.screen.add(this.rightScreen);
	}
	
	private void initializeLeftScreen() {
		try {
			//left screen
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
			this.datePicker = new JDatePickerImpl(datePanel);
	
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
							appointmentsScreen.repaint();
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
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame,
				    "Database error. Check your internet connnection.",
				    "Error fetching appointments",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void initializeRightScreen() {
		//right screen
		this.rightScreen = new JPanel();
		this.rightScreen.setLayout(new BorderLayout());
		this.tabbedPane = new JTabbedPane();
		this.tabbedPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		this.rightScreen.add(tabbedPane, BorderLayout.CENTER);
		this.tabbedPane.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
		//Register tab
		initializeRegisterTab();
		//Patients tab
		initializePatientsTab();
		//Addresses tab
		initializeAddressesTab();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initializeRegisterTab() {
		try {
			//register tab
			this.registerTab = new JPanel();
			this.registerTab.setLayout(new BorderLayout());
			this.tabbedPane.addTab("Register", registerTab);
			JLabel titleTab = new JLabel("Patient Registration", SwingConstants.CENTER);
			titleTab.setFont(new Font("Sans Serif", Font.PLAIN, 
					DisplayFrame.FONT_SIZE));
			this.registerTab.add(titleTab, BorderLayout.NORTH);
			//content of registration
			JPanel content = new JPanel();
			content.setLayout(new GridLayout(0, 2));
			JPanel inputsAndButtons = new JPanel();
			inputsAndButtons.setLayout(new FlowLayout());
			JScrollPane inputsAndButtonsScrollPane = new JScrollPane(inputsAndButtons);
			this.registerTab.add(inputsAndButtonsScrollPane, BorderLayout.CENTER);
			//inputs
			this.registerTabInputs = new ArrayList<Object>();
			//title
			JTextField title = new JTextField();
			title.setToolTipText("Title");
			title.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
			title.addKeyListener(new KeyAdapter() {
			    public void keyTyped(KeyEvent e) { 
			        if (title.getText().length() >= 4)
			            e.consume();
			    }  
			});
			this.registerTabInputs.add(title);
			//firstname
			JTextField firstName = new JTextField();
			firstName.setToolTipText("First Name");
			firstName.setFont(new Font("Sans Serif", Font.PLAIN,
				    	DisplayFrame.FONT_SIZE / 2));
			firstName.addKeyListener(new KeyAdapter() {
			    public void keyTyped(KeyEvent e) { 
			        if (firstName.getText().length() >= 30)
			            e.consume(); 
			    }  
			});
			this.registerTabInputs.add(firstName);
			//lastname
			JTextField lastName = new JTextField();
			lastName.setToolTipText("Last Name");
			lastName.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
			lastName.addKeyListener(new KeyAdapter() {
			    public void keyTyped(KeyEvent e) { 
			        if (lastName.getText().length() >= 30)
			            e.consume(); 
			    }  
			});
			this.registerTabInputs.add(lastName);
			//phonenumber
			NumberFormat numFormat = new DecimalFormat("#0"); //Format of data in phoneNumber
			NumberFormatter  numFormatter  = new NumberFormatter(numFormat);
			JFormattedTextField phoneNumber = new JFormattedTextField(numFormatter);
			phoneNumber.setToolTipText("Phone number");
			phoneNumber.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
			phoneNumber.addKeyListener(new KeyAdapter() {
			    public void keyTyped(KeyEvent e) { 
			        if (phoneNumber.getText().length() >= 20)
			            e.consume(); 
			    }  
			});
			this.registerTabInputs.add(phoneNumber);
			//date of birth
			UtilDateModel model = new UtilDateModel();
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			JDatePickerImpl dateOfBirthPicker = new JDatePickerImpl(datePanel);
			this.registerTabInputs.add(dateOfBirthPicker);
			//housenumber
			JTextField houseNumber = new JTextField();
			houseNumber.setToolTipText("House number");
			houseNumber.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
			houseNumber.addKeyListener(new KeyAdapter() {
			    public void keyTyped(KeyEvent e) { 
			        if (houseNumber.getText().length() >= 30)
			            e.consume(); 
			    }  
			});
			this.registerTabInputs.add(houseNumber);
			//street name
			JTextField streetName = new JTextField();
			streetName.setToolTipText("Street name");
			streetName.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
			streetName.addKeyListener(new KeyAdapter() {
			    public void keyTyped(KeyEvent e) { 
			        if (streetName.getText().length() >= 30)
			            e.consume(); 
			    }  
			});
			this.registerTabInputs.add(streetName);
			//district
			JTextField district = new JTextField();
			district.setToolTipText("District");
			district.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
			district.addKeyListener(new KeyAdapter() {
			    public void keyTyped(KeyEvent e) { 
			        if (district.getText().length() >= 30)
			            e.consume(); 
			    }  
			});
			this.registerTabInputs.add(district);
			//city
			JTextField city = new JTextField();
			city.setToolTipText("City");
			city.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
			city.addKeyListener(new KeyAdapter() {
			    public void keyTyped(KeyEvent e) { 
			        if (city.getText().length() >= 30)
			            e.consume(); 
			    }  
			});
			this.registerTabInputs.add(city);
			//postcode
			JTextField postCode = new JTextField();
			postCode.setToolTipText("Post code");
			postCode.setFont(new Font("Sans Serif", Font.PLAIN,
			    	DisplayFrame.FONT_SIZE / 2));
			postCode.addKeyListener(new KeyAdapter() {
			    public void keyTyped(KeyEvent e) { 
			        if (postCode.getText().length() >= 30)
			            e.consume(); 
			    }  
			});
			this.registerTabInputs.add(postCode);
			//add them to the tab with their respective titles
			JLabel label1 = new JLabel("Title");
			label1.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			label1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label1);
			content.add(title);
			JLabel label2 = new JLabel("First name:");
			label2.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			label2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label2);
			content.add(firstName);
			JLabel label3 = new JLabel("Last name:");
			label3.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			label3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label3);
			content.add(lastName);
			JLabel label4 = new JLabel("Phone Number:");
			label4.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			label4.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label4);
			content.add(phoneNumber);
			JLabel label5 = new JLabel("Date of Birth:");
			label5.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			label5.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label5);
			dateOfBirthPicker.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(dateOfBirthPicker);
			JLabel label6 = new JLabel("House Number:");
			label6.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			label6.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label6);
			content.add(houseNumber);
			JLabel label7 = new JLabel("Street name:");
			label7.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE / 2));
			label7.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label7);
			content.add(streetName);
			JLabel label8 = new JLabel("District:");
			label8.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE / 2));
			label8.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label8);
			content.add(district);
			JLabel label9 = new JLabel("City:");
			label9.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE / 2));
			label9.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label9);
			content.add(city);
			JLabel label10 = new JLabel("Postcode:");
			label10.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			label10.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			content.add(label10);
			content.add(postCode);
			inputsAndButtons.add(content);
			this.registerTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        //health plan list
	        String[] healthPlanNames = healthPlanConverter(HealthPlan.getAllHealthPlans());
	        JList list = new JList(healthPlanNames);
	        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	        list.setVisibleRowCount(-1);
	        list.setSelectedIndex(0);
			//Health plan radio buttons
	        JRadioButton option1 = new JRadioButton("Subscribe to HealthPlan");
	        option1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//enable list
			        list.setEnabled(true);
			        frame.revalidate();
				}
	        });
	        JRadioButton option2 = new JRadioButton("No Subscription");
	        option2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//disable list
			        list.setEnabled(false);
			        frame.revalidate();
				}
	        });
	        ButtonGroup group = new ButtonGroup();
	        option1.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
	        option1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        option1.setSelected(true);
	        option2.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
	        option2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        group.add(option1);
	        group.add(option2);
	        content.add(option1);
	        content.add(option2);
	        JLabel label11 = new JLabel("Health Plan:");
	        label11.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
	        label11.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        content.add(label11);
	        content.add(list);
			//Button
			JButton register = new JButton("Register");
			register.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE));
			register.setEnabled(false);
			register.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Obtain data
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
					//Attempt registration on hopes of success
					try {
						if (!Address.dbHasAddress(houseNum, postcode)) { //register Address
							secretary.registerAddress(houseNum, streetName, district, city, postcode);
						}
						//register patient
						secretary.registerPatient(title, fname, lname, LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)), phone, houseNum, postcode);
						if (!hpName.equals("")) {
							Patient patient = new Patient(fname, houseNum, postcode);
							secretary.subscribePatient(patient, hpName);
						}
						for (int i = 0; i < registerTabInputs.size(); i++) {
							if (i != 4 && i != 3) { //not the date picker or phoneNum
								((JTextField) registerTabInputs.get(i)).setText("");
							} else if (i == 3) { //phoneNum
								((JFormattedTextField) registerTabInputs.get(i)).setText("");
							}
						}
						JOptionPane.showMessageDialog (null, "Successfully added Patient", "Success!", JOptionPane.INFORMATION_MESSAGE);
						//refresh patients list
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
							JOptionPane.showMessageDialog(frame,
								    e4.getMessage(),
								    "Error fetching patients",
								    JOptionPane.ERROR_MESSAGE);
						}
						//refresh addresses list
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
							JOptionPane.showMessageDialog(frame,
								    e5.getMessage(),
								    "Error fetching addresses",
								    JOptionPane.ERROR_MESSAGE);
						}
						frame.revalidate();
						
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(frame,
							    e1.getMessage(),
							    "Error fetching data from db",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			this.registerTab.add(register, BorderLayout.SOUTH);
			//Validation there is input in all fields
			for (int i = 0; i < registerTabInputs.size(); i++) {
				if (i != 4) { //not the date picker 
					((Component) registerTabInputs.get(i)).addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent e) {
							if (title.getText().length() > 0 && firstName.getText().length() > 0 && lastName.getText().length() > 0
									&& phoneNumber.getText().length() > 0 && houseNumber.getText().length() > 0 && streetName.getText().length() > 0
									&& district.getText().length() > 0 && city.getText().length() > 0 && postCode.getText().length() > 0) {
								register.setEnabled(true);
							}
						}
					});
				}
			}
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(frame,
				    e1.getMessage(),
				    "Error fetching data from db",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String[] healthPlanConverter(ArrayList<HealthPlan> allHealthPlans) {
		String[] list = new String[allHealthPlans.size()];
		for (int i = 0; i < allHealthPlans.size(); i++) {
			list[i] = allHealthPlans.get(i).getName();
		}
		return list;
	}

	@SuppressWarnings("serial")
	private void initializeAddressesTab() {
		try {
			//addresses tab
			JPanel addressesTab = new JPanel();
			this.tabbedPane.addTab("Addresses", addressesTab);
			addressesTab.setLayout(new BorderLayout());
			addressesTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			//searching
			JPanel addressesTabSearchPanel = new JPanel();
			addressesTabSearchPanel.setLayout(new GridLayout(0,2));
			JPanel inputsAndButtons = new JPanel();
			inputsAndButtons.setLayout(new FlowLayout());
			addressesTab.add(inputsAndButtons, BorderLayout.NORTH);
			//inputs
			this.addressesTabInputs = new ArrayList<JTextField>();
			JTextField houseNumber = new JTextField();
			houseNumber.setToolTipText("House number");
			houseNumber.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			this.addressesTabInputs.add(houseNumber);
			JTextField streetName = new JTextField();
			streetName.setToolTipText("Street name");
			streetName.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			this.addressesTabInputs.add(streetName);
			JTextField district = new JTextField();
			district.setToolTipText("District");
			district.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			this.addressesTabInputs.add(district);
			JTextField city = new JTextField();
			city.setToolTipText("City");
			city.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			this.addressesTabInputs.add(city);
			JTextField postcode = new JTextField();
			postcode.setToolTipText("Post code");
			postcode.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			this.addressesTabInputs.add(postcode);
			//add them to the tab
			JLabel label1 = new JLabel("House number:");
			label1.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			addressesTabSearchPanel.add(label1);
			addressesTabSearchPanel.add(houseNumber);
			JLabel label2 = new JLabel("Street name:");
			label2.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			addressesTabSearchPanel.add(label2);
			addressesTabSearchPanel.add(streetName);
			JLabel label3 = new JLabel("District:");
			label3.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			addressesTabSearchPanel.add(label3);
			addressesTabSearchPanel.add(district);
			JLabel label4 = new JLabel("City:");
			label4.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			addressesTabSearchPanel.add(label4);
			addressesTabSearchPanel.add(city);
			JLabel label5 = new JLabel("Postcode:");
			label5.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			addressesTabSearchPanel.add(label5);
			addressesTabSearchPanel.add(postcode);
			inputsAndButtons.add(addressesTabSearchPanel);
			//button
			JButton searchAddressesButton = new JButton("Search");
			searchAddressesButton.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
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
						JOptionPane.showMessageDialog(frame,
							    e.getMessage(),
							    "Error fetching addresses",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			searchAddressesButton.setMnemonic(KeyEvent.VK_ENTER);
			inputsAndButtons.add(searchAddressesButton);
			//view all button
			JButton viewAllButton = new JButton("View all");
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
						JOptionPane.showMessageDialog(frame,
							    e.getMessage(),
							    "Error fetching addresses",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			inputsAndButtons.add(viewAllButton);
			//actual addresses
			this.addressesList = addressListConverter(Address.getAllAddresses());
			String[] columnTitles = {"House Number", "Street Name", "District", "City", "Postcode", "Actions"};
			this.addressesTableModel = new DefaultTableModel(this.addressesList, columnTitles) {
				@Override
				public boolean isCellEditable(int row, int col) {
					//only the last column
					return col == 5;
				}
			};
			this.addressesTable = new JTable(this.addressesTableModel);
			this.addressesTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
			this.addressesTable.getColumn("Actions").setCellEditor(
			        new ButtonEditor(new JCheckBox(), this.frame));
			JScrollPane addressesScrollPane = new JScrollPane(this.addressesTable);
			this.addressesTable.setFillsViewportHeight(true);
			addressesTab.add(addressesScrollPane, BorderLayout.CENTER);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
				    e.getMessage(),
				    "Error fetching addresses",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@SuppressWarnings("serial")
	private void initializePatientsTab() {
		try {
			//patients tab
			JPanel patientsTab = new JPanel();
			patientsTab.setLayout(new BorderLayout());
			patientsTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			//searching
			JPanel patientsTabSearchPanel = new JPanel();
			patientsTabSearchPanel.setLayout(new GridLayout(0, 2));
			JPanel inputsAndButtons = new JPanel();
			inputsAndButtons.setLayout(new FlowLayout());
			patientsTab.add(inputsAndButtons, BorderLayout.NORTH);
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
			patientsTabSearchPanel.add(label1);
			patientsTabSearchPanel.add(firstName);
			JLabel label2 = new JLabel("Last name:");
			label2.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			patientsTabSearchPanel.add(label2);
			patientsTabSearchPanel.add(lastName);
			JLabel label3 = new JLabel("House Number:");
			label3.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			patientsTabSearchPanel.add(label3);
			patientsTabSearchPanel.add(houseNumber);
			JLabel label4 = new JLabel("Postcode:");
			label4.setFont(new Font("Sans Serif", Font.BOLD,
			    	DisplayFrame.FONT_SIZE / 2));
			patientsTabSearchPanel.add(label4);
			patientsTabSearchPanel.add(postCode);
			inputsAndButtons.add(patientsTabSearchPanel);
			//button
			JButton searchPatientsButton = new JButton("Search");
			searchPatientsButton.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 2));
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
						e.printStackTrace();
						JOptionPane.showMessageDialog(frame,
							    e.getMessage(),
							    "Error fetching appointments",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			inputsAndButtons.add(searchPatientsButton);
			//view all button
			JButton viewAllPatientsButton = new JButton("View all");
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
						e.printStackTrace();
						JOptionPane.showMessageDialog(frame,
							    e.getMessage(),
							    "Error fetching patients",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			inputsAndButtons.add(viewAllPatientsButton);
			//actual patients
			this.patientsList = patientListConverter(Patient.getAllPatients());
			String[] columnTitlesPatients = {"ID", "Title", "First Name", "Last Name", "Date Of Birth", "House Number", "Postcode", "Telephone", "Actions"};
			this.patientsTableModel = new DefaultTableModel(this.patientsList, columnTitlesPatients) {
				@Override
				public boolean isCellEditable(int row, int col) {
					//only the last column
					return col == 8;
				}
			};
			this.patientsTable = new JTable(this.patientsTableModel);
			this.patientsTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
			this.patientsTable.getColumn("Actions").setCellEditor(
			        new ButtonEditor(new JCheckBox(), this.frame));
			JScrollPane patientsScrollPane = new JScrollPane(patientsTable);
			this.patientsTable.setFillsViewportHeight(true);
			patientsTab.add(patientsScrollPane, BorderLayout.CENTER);
			this.tabbedPane.addTab("Patients", patientsTab);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frame,
				    "Database error. Check your internet connnection.",
				    "Error fetching patients",
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
			String startString = String.format("%tH:%tM", startTime, startTime);
			String startDayString = String.format("%tD", startTime);
			LocalDateTime endTime = app.getEndTime().toLocalDateTime();
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
			JButton detailsButton = new JButton("Details");
			detailsButton.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 3));
			bottomRightSection.add(detailsButton);
			//Patient Info Button
			JButton patientInfoButton = new JButton("Patient Info");
			patientInfoButton.setFont(new Font("Sans Serif", Font.PLAIN,
					DisplayFrame.FONT_SIZE / 3));
			patientInfoButton.putClientProperty("id", patient.getPatientID());
			patientInfoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						Patient patient = new Patient((int) patientInfoButton.getClientProperty("id"));
						DisplayFrame patientViewFrame = new DisplayFrame();
						PatientView patientView = new PatientView(patientViewFrame, patient);
						patientViewFrame.setDisplayedPanel(patientView.getPanel());
					} catch (CommunicationsException e) {
						JOptionPane.showMessageDialog(frame,
							    "Database error. Check your internet connnection.",
							    "Error fetching patient",
							    JOptionPane.ERROR_MESSAGE);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(frame,
							    e.getMessage(),
							    "Error fetching patient",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			JButton deleteButton = new JButton("Delete");
			deleteButton.setFont(new Font("Sans Serif", Font.PLAIN, 
					DisplayFrame.FONT_SIZE / 3));
			deleteButton.putClientProperty("Appointment", app);
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
								Date selectedDate = (Date) datePicker.getModel().getValue();
								List<Appointment> appointmentList = Schedule.getAppointmentsByDay(selectedDate);
								appointmentCards.clear();
								appointmentsScreen.removeAll();
								appointmentsScreen.repaint();
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
			bottomRightSection.add(patientInfoButton);
			bottomRightSection.add(deleteButton);
			if (app.getAppointmentType().equals("Empty")) {
				detailsButton.setEnabled(false);
				patientInfoButton.setEnabled(false);
			}
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

	private Object[][] addressListConverter(ArrayList<Address> givenList) {
		Object[][] addressArr = new Object[givenList.size()][6];
		for (int i = 0; i < givenList.size(); i++) {
			addressArr[i] = new String[6];
			addressArr[i][0] = givenList.get(i).getHouseNumber();
			addressArr[i][1] = givenList.get(i).getStreetName();
			addressArr[i][2] = givenList.get(i).getDistrict();
			addressArr[i][3] = givenList.get(i).getCity();
			addressArr[i][4] = givenList.get(i).getPostcode();
			addressArr[i][5] = "View";
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

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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
			if (label.equals("View")) { //Patients action listener
				try {
					Patient patient = new Patient(Integer.valueOf((String) this.table.getValueAt(this.row, 0)));
					DisplayFrame patientViewFrame = new DisplayFrame();
					PatientView patientView = new PatientView(patientViewFrame, patient);
					patientViewFrame.setDisplayedPanel(patientView.getPanel());
				} catch (CommunicationsException e) {
					JOptionPane.showMessageDialog(this.frame,
						    "Error connecting to the database. Check internet connection.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(this.frame,
						    "Error fetching the information from the database",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			} else { //Addresses action listener
				try {
					String hnum = (String) this.table.getValueAt(this.row, 0);
					String pcode = (String) this.table.getValueAt(this.row, 4);
					Address address = new Address(hnum, pcode);
					DisplayFrame addressViewFrame = new DisplayFrame();
					AddressView addressView = new AddressView(addressViewFrame, address);
					addressViewFrame.setDisplayedPanel(addressView.getPanel());
				} catch (CommunicationsException e) {
					JOptionPane.showMessageDialog(this.frame,
						    "Error connecting to the database. Check internet connection.",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(this.frame,
						    "Error fetching the information from the database",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
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



