package jsqueak;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class Main {
	public static void main(String[] args) {
		TargetDataLine line = null;
		Mixer mixer = null;
		PeakListener peakListener = new PeakListener(800,2500);
		AudioBuffer buffer = peakListener.getBuffer();
		peakListener.addPeakHandler(new LetterDetector());
		byte[] rawChunk = new byte[1024];
		int[] chunk = new int[1024];
		int index = 0;
		
		for (Mixer.Info info : AudioSystem.getMixerInfo()) {
			System.out.println(info.getName());
			if (info.getName().matches("Microphone \\(Plantronics *")) {
				System.out.println("GETTING MIXER");
				mixer = AudioSystem.getMixer(info);
			}
		}
		
		for (Line.Info info : mixer.getTargetLineInfo()) {
			System.out.println(info.toString());
		}
//		AudioFormat format = new AudioFormat(44100,8,1,false,false);
//		DataLine.Info info = new DataLine.Info(TargetDataLine.class, 
//		    format); // format is an AudioFormat object
//		if (!AudioSystem.isLineSupported(info)) {
//		    // Don't handle the error ... 
//
//		}
//		// Obtain and open the line.
//		try {
//		    line = (Port) AudioSystem.getLine(Port.Info.MICROPHONE);
//		    line.open(format);
//		} catch (LineUnavailableException ex) {
//		    // Handle the error ... 
//		}
		
		line.start();
		
		while (true){
			line.read(rawChunk,0,rawChunk.length);
			
			index = 0;
			for (byte b : rawChunk) {
				chunk[index] = (int) b;				
			}
			
			peakListener.analyseBuffer();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
