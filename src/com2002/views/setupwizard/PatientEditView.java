package com2002.views.setupwizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import com.mysql.jdbc.CommunicationsException;

import com2002.models.Address;
import com2002.models.Patient;
import com2002.views.AppointmentView;
import com2002.views.DisplayFrame;
import com2002.views.PatientView;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class PatientEditView {
	private JPanel screen;
	private DisplayFrame frame;
	private Patient patient;
	private ArrayList<Object> inputs;
	
	public PatientEditView(DisplayFrame frame, Patient patient) {
		this.frame = frame;
		this.patient = patient;
		initialize();
	}
	
	private void initialize() {
		try {
			this.screen = new JPanel();
			this.screen.setLayout(new BorderLayout());
			//Get patient details
			String originalTitle = patient.getTitle();
			String originalFirstName = patient.getFirstName();
			String originalLastName = patient.getLastName();
			LocalDate originalDateOfBirth = patient.getDateOfBirth();
			String originalPhoneNumber = patient.getPhoneNumber();
			String originalHouseNumber = patient.getHouseNumber();
			String originalStreetName = patient.getAddress().getStreetName();
			String originalPostcode = patient.getPostcode();
			String originalDistrict = patient.getAddress().getDistrict();
			String originalCity = patient.getAddress().getCity();
			//title
			JLabel titleTab = new JLabel("Update patient details", SwingConstants.CENTER);
			titleTab.setFont(new Font("Sans Serif", Font.PLAIN, 
					DisplayFrame.FONT_SIZE));
			this.screen.add(titleTab, BorderLayout.NORTH);
			//content of registration
			JPanel content = new JPanel();
			content.setLayout(new GridLayout(0, 2));
			JPanel inputsAndButtons = new JPanel();
			inputsAndButtons.setLayout(new FlowLayout());
			JScrollPane inputsAndButtonsScrollPane = new JScrollPane(inputsAndButtons);
			this.screen.add(inputsAndButtonsScrollPane, BorderLayout.CENTER);
			//inputs
			this.inputs = new ArrayList<Object>();
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
			title.setText(originalTitle);
			this.inputs.add(title);
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
			firstName.setText(originalFirstName);
			this.inputs.add(firstName);
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
			lastName.setText(originalLastName);
			this.inputs.add(lastName);
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
			phoneNumber.setText(originalPhoneNumber);
			this.inputs.add(phoneNumber);
			//date of birth
			UtilDateModel model = new UtilDateModel();
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			JDatePickerImpl dateOfBirthPicker = new JDatePickerImpl(datePanel);
			this.inputs.add(dateOfBirthPicker);
			Calendar cal = Calendar.getInstance();
			cal.setTime(Date.from(originalDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			model.setSelected(true);
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
			houseNumber.setText(originalHouseNumber);
			this.inputs.add(houseNumber);
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
			streetName.setText(originalStreetName);
			this.inputs.add(streetName);
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
			district.setText(originalDistrict);
			this.inputs.add(district);
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
			city.setText(originalCity);
			this.inputs.add(city);
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
			postCode.setText(originalPostcode);
			this.inputs.add(postCode);
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
			this.screen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        
			//Button
			JButton saveButton = new JButton("Save");
			saveButton.setFont(new Font("Sans Serif", Font.BOLD,
					DisplayFrame.FONT_SIZE));
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//Obtain data
					String title = ((JTextField) inputs.get(0)).getText();
					String fname = ((JTextField) inputs.get(1)).getText();
					String lname = ((JTextField) inputs.get(2)).getText();
					String phone = ((JFormattedTextField) inputs.get(3)).getText();
					Date date = (Date) ((JDatePickerImpl) inputs.get(4)).getModel().getValue();
					String houseNum = ((JTextField) inputs.get(5)).getText();
					String streetName = ((JTextField) inputs.get(6)).getText();
					String district = ((JTextField) inputs.get(7)).getText();
					String city = ((JTextField) inputs.get(8)).getText();
					String postcode = ((JTextField) inputs.get(9)).getText();
					//Check if any changes have been made to address
					boolean changeAddress = !houseNum.equals(originalHouseNumber) || !streetName.equals(originalStreetName) || !district.equals(originalDistrict) ||
							!city.equals(originalCity) || !postcode.equals(originalPostcode);
					//Check if any changes have been made to patient
					Date dob = Date.from(originalDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());
					boolean changePatient = !title.equals(originalTitle) || !fname.equals(originalFirstName) || !lname.equals(originalLastName) ||
							!phone.equals(originalPhoneNumber) || !date.equals(dob);
					//Attempt registration on hopes of success
					try {
						if (changeAddress) {
							if (!Address.dbHasAddress(houseNum, postcode)) { //register Address
								new Address(houseNum, streetName, district, city, postcode);
							} else { //Just change it in patient
								patient.setHouseNumber(houseNum);
								patient.setPostcode(postcode);
							}
							JOptionPane.showMessageDialog (null, "Successfully updated Address", "Success!", JOptionPane.INFORMATION_MESSAGE);
						}
						if (changePatient) {
							//update patient
							if (title != originalTitle) patient.setTitle(title);
							if (fname != originalFirstName) patient.setFirstName(fname);
							if (lname != originalLastName) patient.setLastName(lname);
							if (phone != originalPhoneNumber) patient.setPhoneNumber(phone);
							if (!date.equals(dob)) patient.setDateOfBirth(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)));
							JOptionPane.showMessageDialog (null, "Successfully updated Patient", "Success!", JOptionPane.INFORMATION_MESSAGE);
						}
						if (!changeAddress && !changePatient) {
							JOptionPane.showMessageDialog (null, "Nothing to change", "Message", JOptionPane.INFORMATION_MESSAGE);
						}
						//refresh parent
						PatientView patientView = new PatientView(frame, patient, AppointmentView.SECRETARY);
						frame.setDisplayedPanel(patientView.getPanel());
						frame.revalidate();
					} catch (CommunicationsException e1) {
						JOptionPane.showMessageDialog(frame,
							    "Not connected to internet",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(frame,
							    e1.getMessage(),
							    "Error fetching data from db",
							    JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			this.screen.add(saveButton, BorderLayout.SOUTH);
			//Validation there is input in all fields
			for (int i = 0; i < inputs.size(); i++) {
				if (i != 4) { //not the date picker 
					((Component) inputs.get(i)).addKeyListener(new KeyAdapter() {
						public void keyTyped(KeyEvent e) {
							if (title.getText().length() > 0 && firstName.getText().length() > 0 && lastName.getText().length() > 0
									&& phoneNumber.getText().length() > 0 && houseNumber.getText().length() > 0 && streetName.getText().length() > 0
									&& district.getText().length() > 0 && city.getText().length() > 0 && postCode.getText().length() > 0) {
								saveButton.setEnabled(true);
							}
						}
					});
				}
			}
		} catch (CommunicationsException e1) {
			JOptionPane.showMessageDialog(frame,
				    "Not connected to internet",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(frame,
				    e1.getMessage(),
				    "Error fetching data from db",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public JPanel getPanel() {
		return this.screen;
	}
}
