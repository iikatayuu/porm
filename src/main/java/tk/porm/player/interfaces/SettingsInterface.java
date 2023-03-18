package tk.porm.player.interfaces;

public interface SettingsInterface {
	public enum THEME { LIGHT, DARK }

	public THEME getTheme();
	public void setTheme(THEME theme);
}
