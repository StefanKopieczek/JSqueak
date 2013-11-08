package jsqueak;

import java.util.ArrayList;
import java.util.Date;

public class PeakListener {
	private ArrayList<PeakHandler> callbacks;
	private AudioBuffer mBuffer;
	private boolean isHigh;
	private long lastPeakTime;
	private int lowPass;
	private int highPass;
	private AudioBuffer.Segment peakSegment;
	private Date date;
	
	private static long MIN_PEAK_DURATION = 500;
	
	public PeakListener(int lowPass, int highPass) {
		this.lowPass = lowPass;
		this.highPass = highPass;
		this.isHigh = false;
		this.date = new Date();
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
		System.out.println(energy);
		long currentTime = this.date.getTime();
		long timeSinceLastPeak = currentTime - lastPeakTime;
		
		if (this.isHigh) {
			if (energy <= this.lowPass && timeSinceLastPeak >= MIN_PEAK_DURATION) {
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
				this.isHigh = true;
				peakSegment = latestChunk;
			}
		}
		
	}
}
