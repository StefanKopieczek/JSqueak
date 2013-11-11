package jsqueak;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * This handler takes a audio segment and decides what letter it sounds most like.
 * @author RPN
 *
 */
public class LetterTrainer implements PeakHandler {
	private int SECTIONS = 3;
	private int[][] BANDS = {{40,400},{400,1000},{1000,10000}};
	private String letters = "abcdefghijklmnopqrstuvwxyz0123456789";
	private int index;
	
	public LetterTrainer() {
		this.index = 0;
	}

	public void handlePeak(AudioBuffer.Segment segment) {
		System.out.println("Analysing audio data...");
		Datum point = analyse(segment);
		
		char letter = letters.charAt(index);
		point.name = String.valueOf(letter);
		writeLetter("letterData.txt",point);
		
		index++;
		if (index >= letters.length()) {
			index = 0;
		}
		System.out.print("Now say: ");
		System.out.print(letters.charAt(index));
	}

	private Datum analyse(AudioBuffer.Segment segment) {
		int sectionLength = segment.length / this.SECTIONS;
		ArrayList<Double> vals = new ArrayList<Double>();
		
		double totalEnergy = AudioUtils.getEnergy(segment);
		double logEnergy = Math.log10(totalEnergy);
		
		for (int i=0; i<this.SECTIONS; i++) {
			AudioBuffer.Segment section = segment.subsegment(i*sectionLength, 
														sectionLength);
			
			for (int[] band : this.BANDS) {
				vals.add(Math.log10(AudioUtils.getEnergyInRange(section,band[0],band[1],44100,10))/logEnergy);
			}
			
			vals.add(Math.log10(AudioUtils.getEnergy(section))/logEnergy);
		}
		
		double[] valArray = new double[vals.size()];
		for (int i=0; i<vals.size(); i++) {
			valArray[i] = vals.get(i);
		}
		return new Datum("Unknown",valArray);
	}
	
	private void writeLetter(String filename, Datum letter) {
		PrintWriter writer = null;

		try {
		    writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
		    StringBuilder builder = new StringBuilder();
		    builder.append(letter.name);
		    for (double val : letter.values) {
		    	builder.append(" ");
		    	builder.append(val);
		    }

		    writer.println(builder.toString());
		} catch (IOException ex){
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
	}

	@Override
	public void handleSilence() {
		// TODO Auto-generated method stub
		
	}
}
