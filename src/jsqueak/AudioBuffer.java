package jsqueak;

public class AudioBuffer {
	private final int CHUNK_SIZE = 1024;
	private final int BUFFER_LENGTH = 1000;
	private int[] raw_data;
	private int mPointer;
	
	public AudioBuffer() {
		this.raw_data = new int[this.BUFFER_LENGTH * this.CHUNK_SIZE];
		this.mPointer = 0;
	}
	
	public int getSample(int index) {
		return getAbsSample(mPointer + index);
	}
	
	public int getAbsSample(int index) {
		if (index >= this.raw_data.length) {
			index -= this.raw_data.length;
		}
		if (index < 0) {
			index += this.raw_data.length;
		}
		return raw_data[index];
	}
	
	public Segment getSegment(int length) {
		int start = mPointer - length;
		if (start < 0) {
			start += raw_data.length;
		}
		return new Segment(this, mPointer - length, length);
	}
	
	public Segment getLatestChunk() {
		return this.getSegment(this.CHUNK_SIZE);
	}
	
	public void addSamples(int[] samples) {
		int distToEnd;
		if (mPointer + samples.length >= raw_data.length) {
			distToEnd = raw_data.length-mPointer;
			System.arraycopy(samples, 0, raw_data, mPointer, distToEnd);
			System.arraycopy(samples, distToEnd, raw_data, 0, samples.length-distToEnd);
		}
		else {
			System.arraycopy(samples, 0, raw_data, mPointer, samples.length);
		}
		mPointer += samples.length;
		if (mPointer >= raw_data.length) {
			mPointer -= raw_data.length;
		}
	}
	
	public class Segment {
		private final int startPointer;
		public int length;
		private final AudioBuffer mBuffer;
		
		public Segment(AudioBuffer buffer, int start, int length) {
			this.mBuffer = buffer;
			this.startPointer = start;
			this.length = length;
		}
		
		public int getSample(int index) {
			return mBuffer.getAbsSample(startPointer + index);
		}
		
		public void extend(int extraLength) {
			this.length += extraLength;
		}
		
		public void extend(Segment segment) {
			int newLen = (segment.startPointer + segment.length) - this.startPointer;
			if (newLen < 0) {
				newLen += mBuffer.raw_data.length;
			}
			this.length = newLen;
		}
	}
}
