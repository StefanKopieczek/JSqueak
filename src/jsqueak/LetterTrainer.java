package jsqueak;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This handler takes a audio segment and decides what letter it sounds most like.
 * @author RPN
 *
 */
public class LetterTrainer implements PeakHandler {
	private int SECTIONS = 3;
	private int[][] BANDS = {{40,400},{400,1000},{1000,20000}};
	private final String letters = "abcdefghijklmnopqrstuvwxyz0123456789";
	private int index;
	private final String filename;
	
	public LetterTrainer(String filename) {
		this.index = 0;
		this.filename = filename;
	}

	public void handlePeak(AudioBuffer.Segment segment) {
		System.out.println("HANDLING PEAK!");
		Datum point = analyse(segment);
		point.name = String.valueOf(letters.charAt(index));
		writeDatumToFile(filename, point);
		
		index++;
		if (index >= letters.length()) {
			index = 0;
		}
		System.out.print("Now say: ");
		System.out.println(letters.charAt(index));
		//CALLBACK WITH LETTER
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
	
	private void writeDatumToFile(String filename, Datum point) {
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
		    StringBuilder builder = new StringBuilder();
		    builder.append(point.name);
		    
		    for (double val : point.values){
		    	builder.append(" ");
		    	builder.append(val);
		    }
		    out.println(builder.toString());
		    out.close();
		} catch (IOException e) {
		    //oh noes!
		}
	}

	public void handleSilence() {
		// TODO Auto-generated method stub
		
	}
}
