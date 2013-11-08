package jsqueak;

import java.util.ArrayList;

/**
 * This handler takes a audio segment and decides what letter it sounds most like.
 * @author RPN
 *
 */
public class LetterDetector implements PeakHandler {
	private ArrayList<Datum> letterData;
	
	public LetterDetector() {
		this.letterData = new ArrayList<Datum>();
	}

	@Override
	public void handlePeak(AudioBuffer.Segment segment) {
		System.out.println("PEAK DETECTED!");
		System.out.println(AudioUtils.getEnergyAtFrequency(segment,440,44100));
	}

	private void analyse(AudioBuffer.Segment segment) {
		
	}
}
