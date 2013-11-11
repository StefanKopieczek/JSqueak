package jsqueak;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class FrequencyVisualiser extends Visualiser{
	private int numBars = 300;
	private int minFreq = 40;
	private int maxFreq = 2000;

	private int[][] BANDS = {{40,400},{400,1000},{1000,20000}};
	
	public FrequencyVisualiser(AudioBuffer buffer) {
		super(buffer);
	}
	
	/**
	 * Updates the visualiser based on a given segment
	 */
	public void update(Graphics g) {
		g.setColor(this.getForeground());
		AudioBuffer.Segment segment = mBuffer.getSegment(2048);
		int df = (maxFreq - minFreq) / numBars;
		double dx = (double)getWidth() / (double)numBars;
		int prevX = 0;
		int prevY = getHeight();
		for (int i=0; i<numBars; i++) {
			int f = minFreq + i*df;
			int x = (int) (dx * i);
			
			double energy = AudioUtils.getEnergyAtFrequency(segment, f, 44100);
			
			double scale = energy/500;
			int size = (int) (scale*getHeight());
			int y = getHeight()-size-1;
					
			g.drawLine(prevX,prevY,x,y);

			prevX = x;
			prevY = y;
			
			//g.fillRect(x, mHeight-size, dx, size);
		}
	}

}
