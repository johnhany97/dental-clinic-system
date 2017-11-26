/**
 * Screen Interface
 * 
 * Interface to be implemented by any class representing a panel
 * @author John Ayad
 */
package com2002.interfaces;

import javax.swing.JPanel;

public interface Screen {

	/**
	 * Function that should return the class' panel
	 * 
	 * @return JPanel representing panel of the view
	 */
	public JPanel getPanel();
}
