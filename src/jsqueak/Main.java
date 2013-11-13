package jsqueak;

import java.awt.Color;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		final AudioBuffer buffer = new AudioBuffer();
		PeakListener peakListener = new PeakListener(buffer, 300,500);
		//buffer.activateLowPassFilter(100);
		//peakListener.addPeakHandler(new LetterDetector("letterData.txt", 1));
		//peakListener.addPeakHandler(new LetterTrainer("letterData.txt"));
		int[] chunk = new int[256];
		int length;
		
		String mixerName = ".*Primary Sound Capture.*";
		mixerName = ".*What U Hear.*";
		Pipe inputPipe = new AudioDeviceInputPipe(mixerName);
		
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
            	//w.setAlwaysOnTop(true);
            }
        });
		
		int ii = 0;
		while (true){
			length = inputPipe.read(chunk,0,chunk.length);
			
			buffer.addSamples(chunk, length);
			
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
