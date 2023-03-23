package tk.porm.player.interfaces;

import java.util.ArrayList;

public interface PlaylistsInterface {
	public ArrayList<Integer> getPlaylists();
	public int newPlaylist();
	public ArrayList<Integer> deletePlaylist(int playlist);
}
