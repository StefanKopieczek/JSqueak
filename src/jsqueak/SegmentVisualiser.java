package jsqueak;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class SegmentVisualiser extends Visualiser{
	public SegmentVisualiser(AudioBuffer buffer) {
		super(buffer);
	}
	
	/**
	 * Updates the visualiser based on a given segment
	 */
	public void update(Graphics g) {
		g.setColor(this.getForeground());
		AudioBuffer.Segment segment = mBuffer.getSegment(50000);

		double dx = (double)getWidth() / (double)segment.length;
		int prevX = 0;
		int prevY = getHeight()/2;
		for (int i=0; i<segment.length; i++) {
			int x = (int) (dx * i);
			
			double energy = segment.getSample(i);
			
			double scale = energy/50000;
			int size = (int) (scale*getHeight());
			int y = (getHeight()/2)-size;
					
			g.drawLine(prevX,prevY,x,y);

			prevX = x;
			prevY = y;
			
			//g.fillRect(x, mHeight-size, dx, size);
		}
	}

}
