package jsqueak;

import java.util.Arrays;

public class NullPipe extends Pipe {

	@Override
	public int read(int[] chunk, int start, int length) {
		block();
		return length;
	}

}
