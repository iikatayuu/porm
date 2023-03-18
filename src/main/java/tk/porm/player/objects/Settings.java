package tk.porm.player.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import tk.porm.player.interfaces.SettingsInterface;

public class Settings implements SettingsInterface {
	private Connection connection;
	private THEME theme;

	public Settings(Connection connection) {
		this.connection = connection;
		this.theme = THEME.LIGHT;

		try {
			Statement statement = connection.createStatement();
			ResultSet settings = statement.executeQuery("SELECT * FROM settings");

			while (settings.next()) {
				String name = settings.getString("name");
				String value = settings.getString("value");

				if (name.equals("theme")) {
					theme = value.equals("light") ? THEME.LIGHT : THEME.DARK;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public THEME getTheme() {
		return theme;
	}

	@Override
	public void setTheme(THEME theme) {
		try {
			String themeStr = theme == THEME.LIGHT ? "light" : "dark";
			PreparedStatement statement = connection.prepareStatement("UPDATE settings SET value=? WHERE name='theme'");
			statement.setString(1, themeStr);
			statement.execute();
			this.theme = theme;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
