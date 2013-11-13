package jsqueak;

public class AudioBufferReaderPipe extends Pipe 
implements AudioBufferListener{
	private AudioBuffer mBuffer;
	private AudioBuffer.Segment mSegment;
	int samplesRequested;
	
	public AudioBufferReaderPipe(AudioBuffer buffer) {
		super();
		mBuffer = buffer;
		mSegment = mBuffer.getLatestChunk();
		samplesRequested = 0;
	}

	@Override
	public int read(int[] chunk, int start, int length) {
		samplesRequested = length;
		if (mSegment.length < samplesRequested) {
			block();
		}
		
		int[] data = mSegment.subsegment(0, length).asArray();
		System.arraycopy(data, 0, chunk, 0, length);
		
		return length;
	}

	@Override
	public void onSamplesAdded(int length) {
		mSegment.extend(mBuffer.getLatestChunk());
		if ((samplesRequested > 0) && (mSegment.length > samplesRequested)) {
			unblock();
		}
	}
}
