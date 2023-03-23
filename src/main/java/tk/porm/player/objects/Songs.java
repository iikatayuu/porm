package tk.porm.player.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import tk.porm.player.interfaces.SongsInterface;

public class Songs implements SongsInterface {
	private Connection connection;
	private ArrayList<Song> songs;
	private int playlist;

	public Songs(Connection connection, int playlist) {
		this.connection = connection;
		this.songs = new ArrayList<Song>();
		this.playlist = playlist;
	}

	@Override
	public ArrayList<Song> getSongs(String search) {
		songs.clear();
		search = "%" + search + "%";

		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM songs WHERE playlist=? AND (title LIKE ? OR artist LIKE ?) ORDER BY id ASC");
			statement.setInt(1, playlist);
			statement.setString(2, search);
			statement.setString(3, search);

			ResultSet result = statement.executeQuery();
			while (result.next()) {
				int id = result.getInt("id");
				String location = result.getString("location");
				String title = result.getString("title");
				String artist = result.getString("artist");
				boolean liked = result.getBoolean("liked");
				int playlistId = result.getInt("playlist");
				Song song = new Song(id, location, title, artist, liked, playlistId);
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
			PreparedStatement statement = connection.prepareStatement("INSERT INTO songs (location, title, artist, playlist) VALUES (?, ?, ?, ?)");
			statement.setString(1, location);
			statement.setString(2, title);
			statement.setString(3, artist);
			statement.setInt(4, playlist);
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

	@Override
	public ArrayList<Song> swapSong(int indexA, int indexB) {
		Song songA = songs.get(indexA);
		Song songB = songs.get(indexB);
		int idA = songA.getID();
		int idB = songB.getID();

		try {
			PreparedStatement statementA = connection.prepareStatement("UPDATE songs SET id = -1 WHERE id = ?");
			statementA.setInt(1, idA);
			statementA.execute();

			PreparedStatement statementB = connection.prepareStatement("UPDATE songs SET id = ? WHERE id = ?");
			statementB.setInt(1, idA);
			statementB.setInt(2, idB);
			statementB.execute();

			PreparedStatement statementC = connection.prepareStatement("UPDATE songs SET id = ? WHERE id = -1");
			statementC.setInt(1, idB);
			statementC.execute();

			songA.setID(idB);
			songB.setID(idA);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		Collections.swap(songs, indexA, indexB);
		return songs;
	}
}
