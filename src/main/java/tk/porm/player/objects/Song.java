package tk.porm.player.objects;

public class Song {
	private int id;
	private String location;
	private String title;
	private String artist;
	private int playlist;
	private boolean liked;

	public Song(int id, String location, String title, String artist, boolean liked, int playlist) {
		this.id = id;
		this.location = location;
		this.title = title;
		this.artist = artist;
		this.liked = liked;
		this.playlist = playlist;
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public boolean getLiked() {
		return liked;
	}

	public void setLike(boolean liked) {
		this.liked = liked;
	}

	public int getPlaylist() {
		return playlist;
	}
}
