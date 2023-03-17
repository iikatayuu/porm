package tk.porm.player.objects;

public class Song {
	private int id;
	private String location;
	private String title;
	private String artist;

	public Song(int id, String location, String title, String artist) {
		this.id = id;
		this.location = location;
		this.title = title;
		this.artist = artist;
	}

	public int getID() {
		return id;
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
}
