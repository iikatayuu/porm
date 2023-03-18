package tk.porm.player.interfaces;

public interface SettingsInterface {
	public enum THEME { LIGHT, DARK }
	public enum REPEAT { NONE, ALL, ONCE }

	public THEME getTheme();
	public void setTheme(THEME theme);

	public REPEAT getRepeat();
	public void setRepeat(REPEAT repeat);

	public boolean getShuffle();
	public void setShuffle(boolean shuffle);
}
