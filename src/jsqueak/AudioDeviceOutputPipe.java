package jsqueak;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class AudioDeviceOutputPipe extends Pipe 
implements Pipe.Pump{
	private SourceDataLine line;
	private Mixer mixer;
	
	public AudioDeviceOutputPipe(String mixerName) {
		
		for (Mixer.Info info : AudioSystem.getMixerInfo()) {
			System.out.println(info.getName());
			if (info.getName().matches(mixerName)) {
				System.out.println("GETTING MIXER");
				mixer = AudioSystem.getMixer(info);
				break;
			}
		}
		
		for (Line.Info info : mixer.getSourceLineInfo()) {
			System.out.println(info.toString());
			if (info.getLineClass() != SourceDataLine.class) {
				continue;
			}
			try {
				line = (SourceDataLine) mixer.getLine(info);
				System.out.println(line.getFormat().getChannels());
				System.out.println(line.getFormat().getSampleSizeInBits());

			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			line.open();
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		line.start();
	}

	@Override
	public int read(int[] chunk, int start, int length) {
		
		return 0;
	}

	public void pump() {
		int[] chunk = new int[128];
		mInput.read(chunk, 0, chunk.length);
		
		byte[] bytes = new byte[chunk.length*4];
		for (int i=0; i<chunk.length; i++) {
			int bytePos = i*4;
			bytes[bytePos] = (byte) (chunk[i] & 0xFF);
			bytes[bytePos+1] = (byte) ((chunk[i] >> 8) & 0xFF);
			bytes[bytePos+2] = (byte) (chunk[i] & 0xFF);
			bytes[bytePos+3] = (byte) ((chunk[i] >> 8) & 0xFF);
		}
		
		line.write(bytes, 0, bytes.length);
	}
}
