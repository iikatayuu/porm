package tk.porm.player.interfaces;

public interface StreamListener {
	public void progress(int read, int length);
	public void beforeClose();
}
