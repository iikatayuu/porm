package tk.porm.player.utils;

import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class ImageMap {
	public static enum ImageKey {
		PREV_LIGHT("prev-light.png"),
		PREV_DARK("prev-dark.png"),
		NEXT_LIGHT("next-light.png"),
		NEXT_DARK("next-dark.png"),
		PLAY_LIGHT("play-light.png"),
		PLAY_DARK("play-dark.png"),
		PAUSE_LIGHT("pause-light.png"),
		PAUSE_DARK("pause-dark.png");

		public final String filename;
		private ImageKey(String filename) {
			this.filename = filename;
		}
	};

	private HashMap<ImageKey, ImageIcon> map;

	public ImageMap(ClassLoader loader) {
		map = new HashMap<ImageKey, ImageIcon>();
		for (ImageKey key : ImageKey.values()) {
			final String filename = key.filename;
			URL imgRes = loader.getResource(filename);
			String imgPath = imgRes.getPath();
			ImageIcon img = new ImageIcon(imgPath);
			map.put(key, img);
		}
	}

	public ImageIcon getIcon(ImageKey key) {
		return map.get(key);
	}
}
