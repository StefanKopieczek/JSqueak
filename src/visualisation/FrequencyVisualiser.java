package visualisation;

import java.awt.Graphics;

import jsqueak.AudioBuffer;
import jsqueak.AudioUtils;
import jsqueak.AudioBuffer.Segment;

public class FrequencyVisualiser extends Visualiser{
	private int numBars = 50;
	private int minFreq = 40;
	private int maxFreq = 1000;

	private int[][] BANDS = {{40,400},{400,1000},{1000,20000}};
	
	public FrequencyVisualiser(AudioBuffer buffer) {
		super(buffer);
	}
	
	/**
	 * Updates the visualiser based on a given segment
	 */
	public void update(Graphics g) {
		g.setColor(this.getForeground());
		AudioBuffer.Segment segment = mBuffer.getSegment(8192);
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
					
			//g.drawLine(prevX,prevY,x,y);

			prevX = x;
			prevY = y;
			
			g.fillRect(x, y, (int) dx, size);
		}
	}

}
