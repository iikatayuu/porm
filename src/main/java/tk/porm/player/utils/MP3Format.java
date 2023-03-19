package tk.porm.player.utils;

import java.io.FileInputStream;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;

public class MP3Format {
	private int duration;

	public MP3Format(String location) {
	    try {
	    	FileInputStream file = new FileInputStream(location);
		    Bitstream stream = new Bitstream(file);
			Header header = stream.readFrame();
			int channelSize = (int) file.getChannel().size();
		    duration = (int) header.total_ms(channelSize) / 1000;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public int getDuration() {
		return duration;
	}

	public static String formatDuration(int duration) {
		int mins = (int) Math.floor(duration / 60);
		int secs = (int) duration % 60;
		return String.format("%02d:%02d", mins, secs);
	}
}
