package tk.porm.player.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import tk.porm.player.interfaces.SettingsInterface;

public class Settings implements SettingsInterface {
	private Connection connection;
	private THEME theme;
	private REPEAT repeat;
	private boolean shuffle;
	private COMPACT compact;

	public Settings(Connection connection) {
		this.connection = connection;
		this.theme = THEME.LIGHT;
		this.repeat = REPEAT.NONE;
		this.shuffle = false;
		this.compact = COMPACT.ENABLED;

		try {
			Statement statement = connection.createStatement();
			ResultSet settings = statement.executeQuery("SELECT * FROM settings");

			while (settings.next()) {
				String name = settings.getString("name");
				String value = settings.getString("value");

				if (name.equals("theme")) {
					theme = value.equals("light") ? THEME.LIGHT : THEME.DARK;
				}

				if (name.equals("repeat")) {
					if (value.equals("none")) repeat = REPEAT.NONE;
					if (value.equals("all")) repeat = REPEAT.ALL;
					if (value.equals("once")) repeat = REPEAT.ONCE;
				}

				if (name.equals("shuffle")) {
					shuffle = value.equals("true");
				}

				if (name.equals("compact")) {
					compact = value.equals("true") ? COMPACT.ENABLED : COMPACT.DISABLED;
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

	@Override
	public REPEAT getRepeat() {
		return repeat;
	}

	@Override
	public void setRepeat(REPEAT repeat) {
		try {
			String repeatStr = null;
			if (repeat == REPEAT.NONE) repeatStr = "none";
			if (repeat == REPEAT.ALL) repeatStr = "all";
			if (repeat == REPEAT.ONCE) repeatStr = "once";

			PreparedStatement statement = connection.prepareStatement("UPDATE settings SET value=? WHERE name='repeat'");
			statement.setString(1, repeatStr);
			statement.execute();
			this.repeat = repeat;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public boolean getShuffle() {
		return shuffle;
	}

	@Override
	public void setShuffle(boolean shuffle) {
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE settings SET value=? WHERE name='shuffle'");
			statement.setString(1, shuffle ? "true" : "false");
			statement.execute();
			this.shuffle = shuffle;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public COMPACT getCompact() {
		return compact;
	}

	@Override
	public void setCompact(COMPACT compact) {
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE settings SET value=? WHERE name='compact'");
			statement.setString(1, compact == COMPACT.ENABLED ? "true" : "false");
			statement.execute();
			this.compact = compact;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
