package com2002;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DisplayFrame extends JFrame {
	
	  /** Defines how many cells to divide width of screen by **/
	  final public static int WIDTH_STEP = 14;

	  /** Defines how many cells to divide the height of the screen by **/
	  final public static int HEIGHT_STEP = 7;

	  /** Defines normal font size **/
	  final public static int FONT_SIZE = 30;
	  
	  /** Defines number of columns **/
	  final public static int NUM_COLS = 5;
	  
	  /** Defines number of rows **/
	  final public static int NUM_ROWS = 5;
	  
	  /** Defines name of the app **/
	  final public static String TITLE = "Dental Clinic System";
	  
	  //Instance variables
	  private int screenHeight;
	  private int screenWidth;
	  private int screenWidthStep;
	  private int screenHeightStep;
	  
	  public DisplayFrame() {
		  initializeFrame();
	  }
	  
	  /**
	   *  Function to set the current panel on the frame
	   * 
	   * @param panel JPanel representing the panel to be displayed on the Frame
	   */
	  public void setDisplayedPanel(JPanel panel) {
		  Container contentPane = this.getContentPane();
		  contentPane.removeAll();
		  contentPane.add(panel);
		  repaint();
		  setVisible(true);
	  }
	
	  /**
	   * Function responsible for initializing JFrame properties
	   */
	  private void initializeFrame() {
	    // Title
	    setTitle(TITLE);
	    // Size and location
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Dimension screenDimensions = toolkit.getScreenSize();
	    this.screenWidthStep = screenDimensions.width / WIDTH_STEP;
	    this.screenHeightStep = screenDimensions.height / HEIGHT_STEP;
	    this.screenWidth = this.screenWidthStep * NUM_COLS;
	    this.screenHeight = this.screenHeightStep *NUM_ROWS;
	    setSize(this.screenWidth, this.screenHeight);
	    int screenLocationX = (screenDimensions.width - this.screenWidth) / 2;
	    int screenLocationY = (screenDimensions.height - this.screenHeight) / 2;
	    setLocation(new Point(screenLocationX, screenLocationY));
	    
	    // Default close operation
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // Prevent resizing
	    setResizable(false);
	  }
}
