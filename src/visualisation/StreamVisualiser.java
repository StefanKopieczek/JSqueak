package visualisation;

import java.awt.Graphics;

import jsqueak.AudioBuffer;
import jsqueak.AudioBuffer.Segment;

public class StreamVisualiser extends Visualiser{
	int mStreamLength;
	
	public StreamVisualiser(AudioBuffer buffer, int length) {
		super(buffer);
		mStreamLength = length;
	}
	
	/**
	 * Updates the visualiser based on a given segment
	 */
	public void update(Graphics g) {
		g.setColor(this.getForeground());
		AudioBuffer.Segment segment = mBuffer.getSegment(mStreamLength);

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
