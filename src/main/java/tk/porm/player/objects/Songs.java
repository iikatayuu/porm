package tk.porm.player.objects;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import tk.porm.player.interfaces.SongsInterface;

public class Songs implements SongsInterface {
	private Connection connection;
	private ArrayList<Song> songs;

	public Songs(Connection connection) {
		this.connection = connection;
		this.songs = new ArrayList<Song>();
	}

	@Override
	public ArrayList<Song> getSongs(String search) {
		songs.clear();
		search = "%" + search + "%";

		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM songs WHERE title LIKE ? OR artist LIKE ?");
			statement.setString(1, search);
			statement.setString(2, search);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				int id = result.getInt("id");
				String location = result.getString("location");
				String title = result.getString("title");
				String artist = result.getString("artist");
				boolean liked = result.getBoolean("liked");
				Song song = new Song(id, location, title, artist, liked);
				songs.add(song);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return songs;
	}

	@Override
	public void addSong(String location, String title, String artist) {
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO songs (location, title, artist) VALUES (?, ?, ?)");
			statement.setString(1, location);
			statement.setString(2, title);
			statement.setString(3, artist);
			statement.execute();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void removeSong(int id) {
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM songs WHERE id = ?");
			statement.setInt(1, id);
			statement.execute();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public ArrayList<Song> likeSong(int id, boolean like) {
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE songs SET liked = ? WHERE id = ?");
			statement.setBoolean(1, like);
			statement.setInt(2, id);
			statement.execute();

			for (int i = 0; i < songs.size(); i++) {
				Song song = songs.get(i);
				int songID = song.getID();
				if (id == songID) {
					song.setLike(like);
					break;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return songs;
	}
}
