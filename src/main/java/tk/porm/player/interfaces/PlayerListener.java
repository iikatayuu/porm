package tk.porm.player.interfaces;

public interface PlayerListener {
	public abstract void progress(int read, int length);
	public abstract void onStart();
	public abstract void onStop();
}
