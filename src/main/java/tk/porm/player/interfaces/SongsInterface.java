package tk.porm.player.interfaces;

import java.util.ArrayList;
import tk.porm.player.objects.Song;

public interface SongsInterface {
	public ArrayList<Song> getSongs(String search);
	public void addSong(String location, String title, String artist);
	public void removeSong(int id);
}
