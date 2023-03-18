package tk.porm.player.interfaces;

public interface PlayerListener {
	public void progress(int read, int length);
	public void onStart();
	public void onStop();
	public void onFinish();
}
