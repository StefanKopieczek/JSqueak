package jsqueak;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class AudioDeviceInputPipe extends Pipe {
	private TargetDataLine line;
	private Mixer mixer;
	
	public AudioDeviceInputPipe(String mixerName) {
		
		for (Mixer.Info info : AudioSystem.getMixerInfo()) {
			System.out.println(info.getName());
			if (info.getName().matches(mixerName)) {
				System.out.println("GETTING MIXER");
				mixer = AudioSystem.getMixer(info);
				break;
			}
		}
		
		for (Line.Info info : mixer.getTargetLineInfo()) {
			System.out.println(info.toString());
			try {
				line = (TargetDataLine) mixer.getLine(info);
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

	public int read(int[] chunk, int start, int length) {
		byte[] bytes = new byte[chunk.length * 4];
		int amountRead = line.read(bytes, start, length*4);
		
		int frame = 0;
		for (int i=0; i<amountRead; i+=4) {
			ByteBuffer bB = ByteBuffer.wrap(new byte[]{bytes[i],bytes[i+1]});
			bB.order(ByteOrder.LITTLE_ENDIAN);  // if you want little-endian
			chunk[frame] = bB.getShort();
			frame += 1;
		}
		
		return amountRead/4;
	}

	@Override
	public void close() {
		line.stop();
		line.close();
	}
}
