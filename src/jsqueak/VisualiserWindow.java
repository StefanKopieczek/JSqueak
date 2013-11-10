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
	/**
	 * The panel to draw everything to.
	 */
	private JPanel mPanel;
	
	/**
	 * The graphics object we actually draw to.
	 */
	private Graphics g;
	
	/**
	 * Width of the game screen.
	 */
	private int mWidth;
	
	/**
	 * Height of the game screen.
	 */
	private int mHeight;
	
	public VisualiserWindow() {
		mWidth = 600;
		mHeight = 200;
	}
	
	/**
	 * Sets up window and makes it visible.
	 */
	public void begin() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIgnoreRepaint(true);
		
		createBufferStrategy(2);
		getBufferStrategy();
		
		this.pack();
		//this.setSize(mWidth,mHeight);
		this.setResizable(false);
	}
	
	/**
	 * @return the Game Panel
	 */
	public JPanel getPanel() {
		return mPanel;
	}
	
	/**
	 * Commits all the changes made and paints to the screen.
	 */
	public void paintToScreen() {
		mPanel.repaint();
	}
	
	@Override
	public int getWidth() {
		return mWidth;
	}
	
	@Override
	public int getHeight() {
		return mHeight;
	}
}
