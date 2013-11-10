package jsqueak;

public class AudioUtils {	
	public static double getEnergy(AudioBuffer.Segment segment) {
		double energy = 0;
		double totalAmplitude = 0;
		
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
		
		sampleDuration = (double)segment.length / (double)sampleFrequency;
		
		adjustedFrequency = frequency * sampleDuration;
		
		energy = Math.abs(sft(segment, adjustedFrequency));
		
		return energy;
	}
	
	/**
	 * Uses composite Simpson's rule to approximate the integral of the 
	 * fourier transform over the range.
	 * @param samples
	 * @param low
	 * @param high
	 * @param samplesFrequency
	 * @param points
	 * @return
	 */
	public static double getEnergyInRange(AudioBuffer.Segment segment, double low, 
							double high, int samplesFrequency, int n) {
		double energy = 0;
		double h = (high - low) / n;
		double odd = 0;
		double even = 0;
		energy = sft(segment, low);
		
		for (int j=1; j<=((n/2)-1); j++) {
			even += sft(segment, low + 2*h*j);
		}
		energy += even*2;
		for (int j=1; j<=n/2; j++) {
			odd += sft(segment, low+(h*(2*j-1)));
		}
		energy += odd*4;
		
		energy += sft(segment, high);
		return (energy*(h/3)) / (high-low);
	}
	
	/**
	 * Calculates the absolute value of the semidiscrete fourier transform
	 * of the segment a at frequency k, relative to the length of a.
	 * @param a array to calculate transform of
	 * @param k frequency to calculate, relative to length of a
	 * @return
	 */
	public static double sft(AudioBuffer.Segment a, double k) {
		double accI, precI, resultR, resultI, expR, expI, e, result;
		
		int N = a.length;
		precI = (2 * Math.PI * k) / N;
		accI = -precI;
		resultR = resultI = 0;
		
		int biggest = 0;
		for (int n=0; n<N; n++) {
			accI += precI;
			expR = Math.cos(accI);
			expI = Math.sin(accI);

			resultR += expR * a.getSample(n);
			resultI += expI * a.getSample(n);
		}
		resultR /= N;
		resultI /= N;
		
		result = Math.sqrt((resultR*resultR) + (resultI*resultI));
		return result;
	}
}
