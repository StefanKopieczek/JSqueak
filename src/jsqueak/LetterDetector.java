package jsqueak;

import java.util.ArrayList;

public class LetterDetector implements PeakHandler {
	private ArrayList<Datum> letterData;
	
	public LetterDetector() {
		this.letterData = new ArrayList<Datum>();
	}

	@Override
	public void handlePeak(AudioBuffer.Segment segment) {
		System.out.println("PEAK DETECTED!");
	}

	private void analyse(AudioBuffer.Segment segment) {
		
	}
}
