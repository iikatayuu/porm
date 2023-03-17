package tk.porm.player.interfaces;

public interface StreamListener {
	public abstract void progress(int read, int length);
	public abstract void beforeClose();
}
