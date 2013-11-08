package jsqueak;

/**
 * A super-fast audio buffer.
 * Pointers & shit. It's basically C, but in Java. I win.
 * @author RPN
 *
 */
public class AudioBuffer {
	private final int CHUNK_SIZE = 1024;
	private final int BUFFER_LENGTH = 10000;
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
	
	/**
	 * Gets the segment of audio up to now of length length.
	 * @param length
	 * @return
	 */
	public Segment getSegment(int length) {
		int start = mPointer - length;
		if (start < 0) {
			start += raw_data.length;
		}

		return new Segment(this, start, length);
	}
	
	public Segment getLatestChunk() {
		return this.getSegment(this.CHUNK_SIZE);
	}
	
	/**
	 * Add samples to buffer.
	 * If samples is longer than the buffer bad things might happen.
	 * @param samples
	 */
	public void addSamples(int[] samples, int length) {
		int distToEnd;
		
		if (mPointer + length >= raw_data.length) {
			distToEnd = raw_data.length-mPointer;
			System.arraycopy(samples, 0, raw_data, mPointer, distToEnd);
			System.arraycopy(samples, distToEnd, raw_data, 0, length-distToEnd);
		}
		else {
			System.arraycopy(samples, 0, raw_data, mPointer, length);
		}
		
		mPointer += length;
		if (mPointer >= raw_data.length) {
			mPointer -= raw_data.length;
		}
	}
	
	/**
	 * A segment of audio.
	 * Really just a wrapper over the AudioBuffer, but looks to outsiders like an array of integers.
	 * @author RPN
	 *
	 */
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
			assert(index < this.length);
			return mBuffer.getAbsSample(startPointer + index);
		}
		
		public Segment subsegment(int start, int length) {
			assert(start < this.length);
			assert(start+length < this.length);
			int absStart = start + startPointer;
			if (absStart >= mBuffer.raw_data.length){
				absStart -= mBuffer.raw_data.length;
			}
			return new Segment(mBuffer, absStart, length);
		}
		
		/**
		 * Extends this segment by extraLength samples.
		 * @param extraLength
		 */
		public void extend(int extraLength) {
			this.length += extraLength;
		}
		
		/**
		 * Extends this segment to the end of the given segment.
		 * @param segment
		 */
		public void extend(Segment segment) {
			int newLen = (segment.startPointer + segment.length) - this.startPointer;
			if (newLen < 0) {
				newLen += mBuffer.raw_data.length;
			}
			this.length = newLen;
		}
	}
}
