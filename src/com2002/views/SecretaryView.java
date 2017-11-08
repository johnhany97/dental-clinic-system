package com2002.views;

import javax.swing.JPanel;

import com2002.interfaces.Screen;
import com2002.models.Secretary;

public class SecretaryView implements Screen {
	
	private JPanel screen;
	private Secretary secretary;
	private DisplayFrame frame;
	
	public SecretaryView(DisplayFrame frame, Secretary secretary) {
		this.frame = frame;
		this.secretary = secretary;
		initializeScreen();
	}

	private void initializeScreen() {
		this.screen = new JPanel();
	}
	
	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
