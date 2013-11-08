package jsqueak;

import java.util.ArrayList;
import java.util.Date;

/**
 * Listens to an AudioBuffer and calls it's PeakHandlers when it's detected a peak in the audio.
 * @author RPN
 *
 */
public class PeakListener {
	private ArrayList<PeakHandler> callbacks;
	private AudioBuffer mBuffer;
	private boolean isHigh;
	private long peakStart=0;
	private double lowPass;
	private double highPass;
	private AudioBuffer.Segment peakSegment;
	
	private static long MIN_PEAK_DURATION = 500;
	
	public PeakListener(double lowPass, double highPass) {
		this.lowPass = lowPass;
		this.highPass = highPass;
		this.isHigh = false;
		this.mBuffer = new AudioBuffer();
		this.callbacks = new ArrayList<PeakHandler>();
	}
	
	public AudioBuffer getBuffer() {
		return this.mBuffer;
	}
	
	public void addPeakHandler(PeakHandler handler) {
		this.callbacks.add(handler);
	}
	
	public void removePeakHandler(PeakHandler handler) {
		this.callbacks.remove(handler);
	}
	
	private void onPeak(AudioBuffer.Segment segment) {
		for (PeakHandler handler : this.callbacks) {
			handler.handlePeak(segment);
		}
	}
	
	public void analyseBuffer() {
		AudioBuffer.Segment latestChunk = mBuffer.getLatestChunk();
		double energy = AudioUtils.getEnergy(latestChunk);
		long currentTime = (new Date()).getTime();
		long peakLength = currentTime - peakStart;
		
		if (this.isHigh) {
			if ((energy <= this.lowPass) && (peakLength >= MIN_PEAK_DURATION)) {
				this.isHigh = false;
				onPeak(peakSegment);
			}
			else {
				peakSegment.extend(latestChunk);
			}
		} 
		else 
		{
			if (energy > this.highPass) {
				System.out.println("PEAK STARTED");
				
				this.isHigh = true;
				peakStart = currentTime;
				peakSegment = latestChunk;
			}
		}
		
	}
}
