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
		double diff = 0;
		assert(this.values.length == other.values.length);

		for (int i=0; i<other.getLength(); i++) {
			diff = other.get(i) - this.get(i);
			result += diff*diff;
		}

		return result;
	}
}
