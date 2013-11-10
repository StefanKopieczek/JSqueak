package jsqueak;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		TargetDataLine line = null;
		Mixer mixer = null;
		//PeakListener peakListener = new PeakListener(80,100);
		final AudioBuffer buffer = new AudioBuffer();
		buffer.activateLowPassFilter(100);
		//peakListener.addPeakHandler(new LetterDetector("letterData.txt", 1));
		//peakListener.addPeakHandler(new LetterTrainer("letterData.txt"));
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

		try {
			line.open();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		line.start();
		
		final VisualiserWindow w = new VisualiserWindow();
		final Visualiser v = new FrequencyVisualiser(buffer,600,200);
		w.add(v);
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	w.begin();
            }
        });
		
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
			
			//peakListener.analyseBuffer();
			
			//Do all drawing on the Event thread
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	v.repaint();
	            }
	        });
		}
	}
}
