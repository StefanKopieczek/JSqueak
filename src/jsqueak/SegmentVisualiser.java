package jsqueak;

import java.awt.Graphics;

import jsqueak.AudioBuffer.Segment;

public class SegmentVisualiser extends Visualiser
implements PeakHandler {
	private AudioBuffer.Segment mSegment;
	
	public SegmentVisualiser(AudioBuffer buffer) {
		super(buffer);
		mSegment = buffer.getLatestChunk();
	}

	/**
	 * Updates the visualiser based on a given segment
	 */
	public void update(Graphics g) {
		g.setColor(this.getForeground());

		double dx = (double)getWidth() / (double)mSegment.length;
		int prevX = 0;
		int prevY = getHeight()/2;
		for (int i=0; i<mSegment.length; i++) {
			int x = (int) (dx * i);
			
			double energy = mSegment.getSample(i);
			
			double scale = energy/50000;
			int size = (int) (scale*getHeight());
			int y = (getHeight()/2)-size;
					
			g.drawLine(prevX,prevY,x,y);

			prevX = x;
			prevY = y;
			
			//g.fillRect(x, mHeight-size, dx, size);
		}
	}

	@Override
	public void handlePeak(Segment segment) {
		mSegment = segment;
	}

	@Override
	public void handleSilence() {
		// TODO Auto-generated method stub
		
	}

}
