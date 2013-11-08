package jsqueak;

public interface PeakHandler {
	public void handlePeak(AudioBuffer.Segment segment);
	public void handleSilence();
}
