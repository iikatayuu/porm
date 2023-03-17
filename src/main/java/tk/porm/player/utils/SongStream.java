package tk.porm.player.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import tk.porm.player.interfaces.StreamListener;

public class SongStream extends FileInputStream {
	private StreamListener listener;
	private int totalRead;
	private int length;

	public SongStream(File file) throws FileNotFoundException,IOException {
		super(file);
		totalRead = 0;
		length = available();
	}

	@Override
	public int read(byte[] b) throws IOException {
		int count = super.read(b);
		update(count);
		return count;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int count = super.read(b, off, len);
		update(count);
		return count;
	}

	@Override
	public int read() throws IOException {
		int count = super.read();
		update(count);
		return count;
	}

	private void update(int count) {
		if (count >= 0) totalRead += count;
		if (listener != null) listener.progress(totalRead, length);
	}

	@Override
	public void close() throws IOException {
		if (listener != null) listener.beforeClose();
		super.close();
	}
	
	public void setStreamListener (StreamListener listener) {
		this.listener = listener;
	}
}
