package jsqueak;

public class InitialBuilder {
	private StringBuilder mBuilder;
	
	public InitialBuilder() {
		this.mBuilder = new StringBuilder();
	}
	
	public void addInitial(char initial) {
		this.mBuilder.append(initial);
	}
	
	public void finish() {
		String initials = this.mBuilder.toString();
		//Callback to listener here
	}
}
