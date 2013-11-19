package jsqueak;

import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class NoiseReductionPipe extends Pipe {
	private int BUFFER_LENGTH = 8192;
	private int[] primaryBuffer;
	private int[] backupBuffer;
	private int samplesInBuffer;
	private boolean backupReady;
	private WorkerThread mThread;
	
	public NoiseReductionPipe() {
		primaryBuffer = new int[BUFFER_LENGTH];
		backupBuffer = new int[BUFFER_LENGTH];
		samplesInBuffer = 0;
		backupReady = false;
	}

	@Override
	public int read(int[] chunk, int start, int length) {
		int[] samples = new int[length];
		int amountRead = 0;
		
		if (!backupReady && (mThread == null || !mThread.isAlive())) {
			mThread = new WorkerThread();
			mThread.start();
		}
		
		if (samplesInBuffer < length) {
			System.arraycopy(primaryBuffer,BUFFER_LENGTH-samplesInBuffer,
							chunk,start,
							samplesInBuffer);
			amountRead = samplesInBuffer;
			while (!backupReady) {
				block();
			}
			swapBuffers();
			System.arraycopy(primaryBuffer,0,
							chunk, start + amountRead,
							length - amountRead);
		}
		else {
			System.arraycopy(primaryBuffer, BUFFER_LENGTH-samplesInBuffer, 
					chunk, start, length);
			samplesInBuffer -= length;
			amountRead = length;
		}
		
		return amountRead;
	}
	
	private void swapBuffers() {
		int[] temp = primaryBuffer;
		primaryBuffer = backupBuffer;
		backupBuffer = temp;
		samplesInBuffer = BUFFER_LENGTH;
		backupReady = false;
		mThread = new WorkerThread();
		mThread.run();
	}

	private class WorkerThread extends Thread {
		@Override
		public void run() {
			int fillPerLoop = 256;
			for (int i=0; i<BUFFER_LENGTH; i+=fillPerLoop) {
				mInput.read(backupBuffer, i, fillPerLoop);
			}
			double[] temp = new double[BUFFER_LENGTH];
			for (int i=0; i<BUFFER_LENGTH; i++) {
				temp[i] = backupBuffer[i];
			}
			FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
			Complex[] transformed = fft.transform(temp, TransformType.FORWARD);
			
			for (int i=0; i<BUFFER_LENGTH; i++) {
				double energy = transformed[i].abs();
				if (energy < 20000 || i > 2000 || i < 30) {
					transformed[i] = new Complex(0);
				}
			}
			Complex[] inverted = fft.transform(transformed, TransformType.INVERSE);
			for (int i=0; i<BUFFER_LENGTH; i++) {
				backupBuffer[i] = (int) inverted[i].getReal();
			}
			
			backupReady = true;
			unblock();
		}
	}
}
