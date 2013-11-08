package jsqueak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Data {
	private ArrayList<Datum> data;
	
	public Data() {
		data = new ArrayList<Datum>();
	}
	
	public Datum kNearestNeighbour(Datum point, int k) {
		HashMap<Double,Datum> vals = new HashMap<Double,Datum>();
		ArrayList<Double> dists = new ArrayList<Double>();
		double dist;
		
		for (Datum d : data) {
			dist = point.getDistSquared(d);
			vals.put(dist, d);
			dists.add(dist);
		}
		
		Double[] sortedDists = dists.toArray(new Double[dists.size()]);
		Arrays.sort(sortedDists);
		
		int most = 0;
		int val = 0;
		HashMap<String, Integer> kNearest = new HashMap<String, Integer>();
		for (int i=0; i<k; i++) {
			String name = vals.get(sortedDists[i]).name;
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
		
		//ArrayList<>
		for (String name : kNearest.keySet()) {
			if (kNearest.get(name) >= most) {
				most = kNearest.get(name);
			}
		}
	}
}
