package tk.porm.player.objects;

import java.awt.EventQueue;
import java.io.File;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;
import javazoom.jl.player.advanced.PlaybackEvent;

import tk.porm.player.interfaces.PlayerListener;
import tk.porm.player.interfaces.StreamListener;
import tk.porm.player.utils.SongStream;

public class SongPlayer {
	private File file;
	private AdvancedPlayer player;
	private SongStream stream;
	private int pause;
	private boolean paused;
	private Thread threadPlay;
	private PlayerListener listener;

	public SongPlayer(String location) {
		file = new File(location);
		pause = 0;
		threadPlay = null;
	}

	private Thread createThread() {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					stream = new SongStream(file);
					stream.setStreamListener(new StreamListener() {
						@Override
						public void progress(int read, int length) {
							int gap = pause == 0 ? 0 : length - pause;
							if (listener != null) {
								int totalRead = read + gap;
								listener.progress(totalRead, length);
								if (totalRead == length) {
									EventQueue.invokeLater(new Runnable() {
										public void run() {
											listener.onFinish();
										}
									});
								}
							}
						}

						@Override
						public void beforeClose() {
							try {
								int available = stream.available();
								pause = available;
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}
					});

					int length = stream.available();
					player = new AdvancedPlayer(stream);
					player.setPlayBackListener(new PlaybackListener() {
						@Override
						public void playbackStarted(PlaybackEvent event) {
							paused = false;
							if (listener != null) listener.onStart();
						}

						@Override
						public void playbackFinished(PlaybackEvent event) {
							paused = true;
							if (listener != null) listener.onStop();
						}
					});

					if (pause > 0) stream.skip(length - pause);
					player.play();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
	}

	public void setPlayerListener (PlayerListener listener) {
		this.listener = listener;
	}

	public void play() {
		paused = false;
		threadPlay = createThread();
		threadPlay.start();
	}

	public void pause() {
		paused = true;
		try {
			pause = stream.available();
			player.stop();
			threadPlay = null;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public boolean isPaused() {
		return paused;
	}

	public void terminate() {
		if (player != null) {
			player.close();
		}
	}
}
