package com2002.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com2002.interfaces.Screen;
import com2002.models.HealthPlan;
import com2002.models.Patient;
import com2002.models.Usage;

public class PatientView implements Screen {
	
	private JPanel screen;
	private DisplayFrame frame;
	private Patient patient;
	
	public PatientView(DisplayFrame frame, Patient patient) {
		this.frame = frame;
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.patient = patient;
		initialize();
	}
	
	private void initialize() {
		//Patient details
		String name = this.patient.getTitle() + " " + this.patient.getFirstName() + " " + this.patient.getLastName();
		String dob = this.patient.getDateOfBirth().toString();
		long years = ChronoUnit.YEARS.between(this.patient.getDateOfBirth(), LocalDate.now());
		String phoneNumber = this.patient.getPhoneNumber();
		//screen
		this.screen = new JPanel();
		this.screen.setLayout(new BorderLayout());
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
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(0, 1));
		this.screen.add(centerPanel, BorderLayout.CENTER);
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
		details.setLayout(new GridLayout(0, 4));
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
		hpPanel.setLayout(new BoxLayout(hpPanel, BoxLayout.Y_AXIS));
		String nameString = "No Health Plan";
		String checkupString = "Checkup: 0 out of 0";
		String repairString = "Repair: 0 out of 0";
		String hygieneString = "Hygiene: 0 out of 0";
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
	    centerPanel.add(hpPanel);
	}

	@Override
	public JPanel getPanel() {
		
		return this.screen;
	}

}
