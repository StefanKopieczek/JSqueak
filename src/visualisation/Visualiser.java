package visualisation;

import java.awt.Graphics;

import javax.swing.JPanel;

import jsqueak.AudioBuffer;

public abstract class Visualiser extends JPanel{
	protected int mWidth, mHeight;
	protected AudioBuffer mBuffer;
	
	public Visualiser(AudioBuffer buffer) {
		mBuffer = buffer;
	}
	
//	@Override
//    public Dimension getPreferredSize() {
//        return new Dimension(mWidth, mHeight);
//    }
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		clearScreen(g);
		update(g);
	}
	
	/**
	 * Draws the background to the screen to clear it.
	 */
	public void clearScreen(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0,0,this.getWidth(),this.getHeight());
	}
	
	public abstract void update(Graphics g);
}
