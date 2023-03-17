package tk.porm.player.objects;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import tk.porm.player.interfaces.SongsInterface;

public class Songs implements SongsInterface {
	private Connection connection;

	public Songs(Connection connection) {
		this.connection = connection;
	}

	@Override
	public ArrayList<Song> getSongs(String search) {
		ArrayList<Song> songs = new ArrayList<Song>();
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
				Song song = new Song(id, location, title, artist);
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
}
