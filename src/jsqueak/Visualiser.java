package jsqueak;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class Visualiser extends JPanel{
	protected int mWidth, mHeight;
	protected AudioBuffer mBuffer;
	
	public Visualiser(AudioBuffer buffer, int width, int height) {
		mBuffer = buffer;
		mWidth = width;
		mHeight = height;
	}
	
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(mWidth, mHeight);
    }
	
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
		g.setColor(Color.WHITE);
		g.fillRect(0,0,mWidth,mHeight);
	}
	
	public abstract void update(Graphics g);
}
