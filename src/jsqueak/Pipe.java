package jsqueak;

public abstract class Pipe {
	protected Pipe mInput;
	private Object lock;
	
	public Pipe() {
		lock = new Object();
	}
	
	public void readFrom(Pipe other) {
		mInput = other;
	}
	
	public abstract int read(int[] chunk, int start, int length);
	
	public void close() {
	}
	
	public interface Pump {
		public void pump();
	}
	
	protected void block() {
		synchronized(lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void unblock() {
		synchronized(lock) {
			lock.notify();
		}
	}
}
