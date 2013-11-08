package jsqueak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Data {
	private ArrayList<Datum> data;
	
	public Data() {
		data = new ArrayList<Datum>();
	}
	
	public void addDatum(Datum d) {
		data.add(d);
	}
	
	public String kNearestNeighbour(Datum point, int k) {

		HashMap<Double,String> vals = new HashMap<Double,String>();
		ArrayList<Double> dists = new ArrayList<Double>();
		double dist;
		
		for (Datum d : data) {
			dist = point.getDistSquared(d);
			vals.put(dist, d.name);
			dists.add(dist);
		}
		
		Double[] sortedDists = dists.toArray(new Double[dists.size()]);
		Arrays.sort(sortedDists);
		
		int most = 0;
		int val = 0;

		LinkedHashMap<String, Integer> kNearest = new LinkedHashMap<String, Integer>();

		for (int i=0; i<k; i++) {
			String name = vals.get(sortedDists[i]);
			if (!kNearest.containsKey(name)) {
				val = 1;
			}
			else {
				val = kNearest.get(name)+1;
			}
			kNearest.put(name,val);
			if (val > most) {
				most = val;
			}
		}

		ArrayList<String> winners = new ArrayList<String>();
		for (String name : kNearest.keySet()) {
			if (kNearest.get(name) == most) {
				winners.add(name);
			}
		}
		
		for (String letter : kNearest.keySet()) {
			for (String l : winners) {
				if (l.equals(letter)) {
					return letter;
				}
			}
		}
		
		return null;
	}
}
