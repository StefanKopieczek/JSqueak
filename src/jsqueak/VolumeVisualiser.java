package jsqueak;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class VolumeVisualiser extends Visualiser{
	public VolumeVisualiser(AudioBuffer buffer, int width, int height) {
		super(buffer, width, height);
	}

	/**
	 * Updates the visualiser based on a given segment
	 */
	@Override
	public void update(Graphics g) {
		double energy = AudioUtils.getEnergy(mBuffer.getSegment(10000));
		int size = (int) (300*(1-Math.exp(-energy/500)));
		g.setColor(Color.RED);
		g.fillOval((mWidth-size)/2, (mHeight-size)/2, size,size);
	}

}
