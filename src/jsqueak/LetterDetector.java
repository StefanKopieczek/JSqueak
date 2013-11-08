package jsqueak;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This handler takes a audio segment and decides what letter it sounds most like.
 * @author RPN
 *
 */
public class LetterDetector implements PeakHandler {
	private Data letterData;
	private int SECTIONS = 3;
	private int[][] BANDS = {{40,400},{400,1000},{1000,20000}};
	private int k;
	
	public LetterDetector(String filename, int k) {
		loadLetterData(filename);
		this.k = k;
	}

	public void handlePeak(AudioBuffer.Segment segment) {
		System.out.println("HANDLING PEAK!");
		Datum point = analyse(segment);
		
		String letter = letterData.kNearestNeighbour(point, k);
		
		System.out.print("I think you said: ");
		System.out.println(letter);
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
	
	private void loadLetterData(String filename) {
		letterData = new Data();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
		    try {
		        String line = br.readLine();
		        
		        while (line != null) {
			        String[] splitLine = line.split(" ");
			        
			        double[] valArray = new double[splitLine.length-1];
					for (int i=1; i<splitLine.length; i++) {
						valArray[i-1] = Double.parseDouble(splitLine[i]);
					}
					
					letterData.addDatum(new Datum(splitLine[0], valArray));
					line = br.readLine();
		        }
		        
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				br.close();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
