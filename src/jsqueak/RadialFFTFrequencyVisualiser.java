package jsqueak;

import java.awt.Color;
import java.awt.Graphics;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class RadialFFTFrequencyVisualiser extends Visualiser{
	private int numBars = 300;
	private int minFreq = 40;
	private int maxFreq = 1000;

	private int[][] BANDS = {{40,400},{400,1000},{1000,20000}};
	
	public RadialFFTFrequencyVisualiser(AudioBuffer buffer) {
		super(buffer);
	}
	
	/**
	 * Updates the visualiser based on a given segment
	 */
	public void update(Graphics g) {
		AudioBuffer.Segment segment = mBuffer.getSegment(8192);
		int fR = getForeground().getRed();
		int fG = getForeground().getGreen();
		int fB = getForeground().getBlue();
		int bR = getBackground().getRed();
		int bG = getBackground().getGreen();
		int bB = getBackground().getBlue();

		FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] transformed = fft.transform(segment.asArray(), TransformType.FORWARD);
		int numBars = 300;
		double dr = (double)getWidth() / (double)numBars;

		for (int i=0; i<numBars; i++) {
			int r = (int) (dr * i);
			
			double energy = transformed[i].abs();
			
			double scale = Math.min(1,energy/8000000);
			
			if (scale <= 0.1) {
				continue;
			}

			int R = (int) (bR + (fR-bR)*scale);
			int G = (int) (bG + (fG-bG)*scale);
			int B = (int) (bB + (fB-bB)*scale);
			g.setColor(new Color(R,G,B));
					
			g.drawOval((getWidth()-r)/2, (getHeight()-r)/2, r, r);
		}
	}

}
