package jsqueak;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class Main {
	public static void main(String[] args) {
		TargetDataLine line = null;
		Mixer mixer = null;
		PeakListener peakListener = new PeakListener(400,1000);
		AudioBuffer buffer = peakListener.getBuffer();
		peakListener.addPeakHandler(new LetterDetector("letterData.txt"));
		byte[] rawChunk = new byte[1024];
		int[] chunk = new int[256];
		int frame = 0;
		int length;
		
		for (Mixer.Info info : AudioSystem.getMixerInfo()) {
			System.out.println(info.getName());
			if (info.getName().matches(".*Primary Sound Capture.*")) {
				System.out.println("GETTING MIXER");
				mixer = AudioSystem.getMixer(info);
				break;
			}
		}
		
		for (Line.Info info : mixer.getTargetLineInfo()) {
			System.out.println(info.toString());
			try {
				line = (TargetDataLine) mixer.getLine(info);
				System.out.println(line.getFormat().getChannels());
				System.out.println(line.getFormat().getSampleSizeInBits());

			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		try {
			line.open();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		line.start();
		
		while (true){
			length = line.read(rawChunk,0,rawChunk.length);
			
			frame = 0;
			for (int i=0; i<length; i+=4) {
				ByteBuffer bB = ByteBuffer.wrap(new byte[]{rawChunk[i],rawChunk[i+1]});
				bB.order(ByteOrder.LITTLE_ENDIAN);  // if you want little-endian
				chunk[frame] = bB.getShort();
				frame += 1;
			}
			
			buffer.addSamples(chunk, length/4);
			
			peakListener.analyseBuffer();
		}
	}
}
