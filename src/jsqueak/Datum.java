package jsqueak;

public class Datum {
	public String name;
	public double[] values;
	
	public Datum(String name, double[] vals) {
		this.name = name;
		this.values = vals;
	}
	
	public int getLength() {
		return this.values.length;
	}
	
	public double get(int index) {
		return this.values[index];
	}
	
	public double getDistSquared(Datum other) {
		double result = 0;
		
		for (int i=0; i<other.getLength(); i++) {
			result += other.get(i) * other.get(i);
		}
		
		return result;
	}
}
