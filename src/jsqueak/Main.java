package jsqueak;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.SwingUtilities;

import visualisation.RadialFFTFrequencyVisualiser;
import visualisation.Visualiser;
import visualisation.VisualiserWindow;

public class Main {
	public static void main(String[] args) {
		TargetDataLine line = null;
		Mixer mixer = null;
		final AudioBuffer buffer = new AudioBuffer();
		PeakListener peakListener = new PeakListener(buffer, 300,500);
		//buffer.activateLowPassFilter(100);
		//peakListener.addPeakHandler(new LetterDetector("letterData.txt", 1));
		//peakListener.addPeakHandler(new LetterTrainer("letterData.txt"));
		byte[] rawChunk = new byte[128];
		int[] chunk = new int[256];
		int frame = 0;
		int length = 0;
		String mixerName = ".*Primary Sound Capture.*";
		//mixerName = ".*What U Hear.*";
		for (Mixer.Info info : AudioSystem.getMixerInfo()) {
			System.out.println(info.getName());
			if (info.getName().matches(mixerName)) {
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
		
		Visualiser vis = new RadialFFTFrequencyVisualiser(buffer);
		//Visualiser vis = new StreamVisualiser(buffer,5000);
		vis.setBackground(Color.BLACK);
		vis.setForeground(Color.GREEN);
		w.add(vis);

		//peakListener.addPeakHandler((SegmentVisualiser)vis);
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//            	w.setUndecorated(true);
//            	w.setOpacity(0.5f);
            	w.begin();
            	w.setBounds(0,0,600,200);
            	w.setAlwaysOnTop(true);
            }
        });
		
		int ii = 0;
		while (true){
			length = line.read(rawChunk,0,rawChunk.length);
			
			frame = 0;
			for (int i=0; i<length; i+=2) {
				ByteBuffer bB = ByteBuffer.wrap(new byte[]{rawChunk[i],rawChunk[i+1]});
				bB.order(ByteOrder.LITTLE_ENDIAN);  // if you want little-endian
				chunk[frame] = bB.getShort();
				frame += 1;
			}
			
			buffer.addSamples(chunk, length/2);
			
			//peakListener.analyseBuffer();
			
			//Do all drawing on the Event thread
			if (ii % 10 == 0) {
				SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	w.repaint();
		            }
		        });
			}
			ii++;
		}
	}
}
