package jsqueak;

public class AudioSinkPipe extends Pipe 
implements Pipe.Pump{

	@Override
	public int read(int[] chunk, int start, int length) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void pump() {
		int[] chunk = new int[128];
		mInput.read(chunk, 0, chunk.length);
	}
}
