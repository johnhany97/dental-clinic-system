/**
 * DisplayFrame class
 * 
 * Class representing the Frame in which the application
 * will be shown
 * @author John Ayad
 */
package com2002.views;

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
	  
	  /** Defines Default number of rows and columns **/
	  final public static int DEFAULT_NUM = 5;
	  
	  /** Defines number of columns **/
	  private int numCols;
	  
	  /** Defines number of rows **/
	  private int numRows;
	  
	  /** Defines name of the app **/
	  final public static String TITLE = "Dental Clinic System";
	  
	  //Instance variables
	  private int screenHeight;
	  private int screenWidth;
	  private int screenWidthStep;
	  private int screenHeightStep;
	  
	  /**
	   * Constructor
	   * 
	   * Used to create an instance of the DisplayFrame
	   */
	  public DisplayFrame() {
		  this.numCols = DEFAULT_NUM;
		  this.numRows = DEFAULT_NUM;
		  initializeFrame(); //initialize the frame's size and location
	  }

	  /**
	   * Function used to change frame size
	   * 
	   * @param numRows Int representing the number of rows with which we multiply the step
	   * @param numCols Int representing the number of cols with which we multiply the step 
	   */
	  public void setFrameSize(int numRows, int numCols) {
		  this.numRows = numRows;
		  this.numCols = numCols;
		  this.screenWidth = this.screenWidthStep * this.numCols;
		  this.screenHeight = this.screenHeightStep * this.numRows;
		  setSize(this.screenWidth, this.screenHeight);
	  }
	  
	  /**
	   * Function to set the current panel on the frame
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
	    this.screenWidth = this.screenWidthStep * this.numCols;
	    this.screenHeight = this.screenHeightStep * this.numRows;
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
