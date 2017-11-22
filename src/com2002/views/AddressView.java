package com2002.views;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com2002.interfaces.Screen;
import com2002.models.Address;

public class AddressView implements Screen {
	
	private JPanel screen;
	private DisplayFrame frame;
	private Address address;
	
	public AddressView(DisplayFrame frame, Address address) {
		this.frame = frame;
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.address = address;
		initialize();
	}
	
	private void initialize() {
		this.screen = new JPanel();
	}
	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
