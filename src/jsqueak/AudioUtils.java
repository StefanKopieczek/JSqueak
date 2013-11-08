package jsqueak;

public class AudioUtils {	
	public static double getEnergy(AudioBuffer.Segment segment) {
		double energy = 0;
		int totalAmplitude = 0;
		
		for (int i=0; i<segment.length; i++) {
			totalAmplitude += Math.abs(segment.getSample(i));
		}
		
		energy = totalAmplitude / segment.length;
		
		return energy;
	}
	
	public static double getEnergyAtFrequency(AudioBuffer.Segment segment, 
											int frequency, int sampleFrequency) {
		double energy = 0;
		double sampleDuration, adjustedFrequency;
		
		sampleDuration = segment.length / sampleFrequency;
		
		adjustedFrequency = frequency * sampleDuration;
		
		energy = Math.abs(sft(segment, adjustedFrequency));
		
		return energy;
	}
	
	public static double getEnergyInRange(int[] samples, int low, int high, int samplesFrequency, int points) {
		double energy = 0;
		
		return energy;
	}
	
	public static double sft(AudioBuffer.Segment a, double k) {
		return 1;
	}
}
