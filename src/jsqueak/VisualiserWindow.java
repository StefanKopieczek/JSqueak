package jsqueak;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * This class handles drawing.
 * @author rynor_000
 *
 */
public class VisualiserWindow extends JFrame {	
	public VisualiserWindow() {
	}
	
	/**
	 * Sets up window and makes it visible.
	 */
	public void begin() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createBufferStrategy(2);
		
		this.pack();
		//this.setResizable(false);
	}
}
