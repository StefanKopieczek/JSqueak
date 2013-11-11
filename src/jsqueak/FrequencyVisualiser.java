package jsqueak;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class FrequencyVisualiser extends Visualiser{
	private int numBars = 300;
	private int minFreq = 40;
	private int maxFreq = 2000;

	private int[][] BANDS = {{40,400},{400,1000},{1000,20000}};
	
	public FrequencyVisualiser(AudioBuffer buffer, int width, int height) {
		super(buffer, width, height);
	}
	
	/**
	 * Updates the visualiser based on a given segment
	 */
	public void update(Graphics g) {
		g.setColor(Color.RED);
		AudioBuffer.Segment segment = mBuffer.getSegment(2048);
		int df = (maxFreq - minFreq) / numBars;
		int dx = mWidth / numBars;
		int prevX = 0;
		int prevY = mHeight;
		for (int i=0; i<numBars; i++) {
			int f = minFreq + i*df;
			int x = dx * i;
			
			double energy = AudioUtils.getEnergyAtFrequency(segment, f, 44100);
			
			double scale = energy/200;
			int size = (int) (scale*mHeight);
			int y = mHeight-size;
					
			g.drawLine(prevX,prevY,x,y);

			prevX = x;
			prevY = y;
			
			//g.fillRect(x, mHeight-size, dx, size);
		}
	}

}
