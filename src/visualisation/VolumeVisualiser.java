package visualisation;

import java.awt.Color;
import java.awt.Graphics;

import jsqueak.AudioBuffer;
import jsqueak.AudioUtils;

public class VolumeVisualiser extends Visualiser{
	public VolumeVisualiser(AudioBuffer buffer) {
		super(buffer);
	}

	/**
	 * Updates the visualiser based on a given segment
	 */
	@Override
	public void update(Graphics g) {
		double energy = AudioUtils.getEnergy(mBuffer.getSegment(5000));
		int size = (int) (300*(1-Math.exp(-energy/500)));
		g.setColor(Color.RED);
		g.fillOval((mWidth-size)/2, (mHeight-size)/2, size,size);
	}

}
