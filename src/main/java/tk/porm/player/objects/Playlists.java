package tk.porm.player.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import tk.porm.player.interfaces.PlaylistsInterface;

public class Playlists implements PlaylistsInterface {
	private Connection connection;
	private ArrayList<Integer> playlists;

	public Playlists(Connection connection) {
		this.connection = connection;
		this.playlists = new ArrayList<Integer>();
	}

	@Override
	public ArrayList<Integer> getPlaylists() {
		playlists.clear();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM playlists ORDER BY id ASC");
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				int playlist = result.getInt("id");
				playlists.add(playlist);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return playlists;
	}

	@Override
	public int newPlaylist() {
		int id = 0;
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO playlists (created) VALUES (CURRENT_TIMESTAMP)", PreparedStatement.RETURN_GENERATED_KEYS);
			statement.executeUpdate();
			ResultSet result = statement.getGeneratedKeys();

			while (result.next()) id = result.getInt(1);
			playlists.add(id);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return id;
	}

	@Override
	public ArrayList<Integer> deletePlaylist(int playlist) {
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM songs WHERE playlist=?");
			statement.setInt(1, playlist);
			statement.execute();

			PreparedStatement playlistsStatement = connection.prepareStatement("DELETE FROM playlists WHERE id=?");
			playlistsStatement.setInt(1, playlist);
			playlistsStatement.execute();

			for (int i = 0; i < playlists.size(); i++) {
				if (playlists.get(i) == playlist) {
					playlists.remove(i);
					break;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return playlists;
	}

}
