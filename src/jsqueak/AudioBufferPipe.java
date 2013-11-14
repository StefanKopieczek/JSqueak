package jsqueak;

public class AudioBufferPipe extends Pipe 
implements AudioBufferListener{
	private AudioBuffer mBuffer;
	private AudioBuffer.Segment mSegment;
	private UpdaterThread mThread;
	int samplesRequested;
	
	public AudioBufferPipe(int bufferLength) {
		super();
		mBuffer = new AudioBuffer(bufferLength);
		mBuffer.addListener(this);
		samplesRequested = 0;
	}
	
	@Override
	public void readFrom(Pipe other) {
		super.readFrom(other);
		mSegment = mBuffer.getLatestChunk();
		mThread = new UpdaterThread();
		mThread.start();
	}
	
	public AudioBuffer getbuffer() {
		return mBuffer;
	}

	@Override
	public int read(int[] chunk, int start, int length) {
		samplesRequested = length;
		
		if (mSegment.length < samplesRequested) {
			block();
		}
		
		int[] data = mSegment.subsegment(0, samplesRequested).asArray();
		System.arraycopy(data, 0, chunk, 0, length);
		mSegment.move(length);
		mSegment.extend(-length);
		
		return length;
	}

	@Override
	public void onSamplesAdded(int length) {
		mSegment.extend(mBuffer.getLatestChunk());
		if ((samplesRequested > 0) && (mSegment.length >= samplesRequested)) {
			unblock();
		}
	}
	
	private class UpdaterThread extends Thread {
		private boolean stopped = false;
		
		@Override
		public void run() {
			int[] data = new int[128];
			while (!stopped) {
				int amountRead = mInput.read(data,0,128);
				mBuffer.addSamples(data, amountRead);
			}
		}
		
		public void pleaseStop() {
			stopped = true;
		}
	}
}
