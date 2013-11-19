package visualisation;

import java.awt.Graphics;

import jsqueak.AudioBuffer;
import jsqueak.AudioBuffer.Segment;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class FFTFrequencyVisualiser extends Visualiser{
	private int numBars = 300;
	private int minFreq = 40;
	private int maxFreq = 1000;

	private int[][] BANDS = {{40,400},{400,1000},{1000,20000}};
	
	public FFTFrequencyVisualiser(AudioBuffer buffer) {
		super(buffer);
	}
	
	/**
	 * Updates the visualiser based on a given segment
	 */
	public void update(Graphics g) {
		g.setColor(this.getForeground());
		AudioBuffer.Segment segment = mBuffer.getSegment(8192);

		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] transformed = fft.transform(segment.asArrayOfDoubles(), TransformType.FORWARD);
		int numBars = 200;
		double dx = (double)getWidth() / (double)numBars;
		int prevX = 0;
		int prevY = getHeight();
		for (int i=0; i<numBars; i++) {
			int x = (int) (dx * i);
			
			double energy = transformed[i].abs();
			
			double scale = energy/10000000;
			int size = (int) (scale*getHeight());
			int y = getHeight()-size-1;
					
			g.drawLine(prevX,prevY,x,y);

			prevX = x;
			prevY = y;
			
			//g.fillRect(x, y, (int) dx, size);
		}
	}

}
