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
		this.frame.setFrameSize(DisplayFrame.DEFAULT_NUM, DisplayFrame.DEFAULT_NUM * 2);
		this.frame.centerFrame();
	}
	
	@Override
	public JPanel getPanel() {
		return this.screen;
	}

}
