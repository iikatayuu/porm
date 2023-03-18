package tk.porm.player.utils;

import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class ImageMap {
	public static enum ImageKey {
		PREV_LIGHT("prev-light.png"),
		NEXT_LIGHT("next-light.png"),
		PLAY_LIGHT("play-light.png"),
		PAUSE_LIGHT("pause-light.png"),
		HEART_LIGHT("heart-light.png"),
		NO_HEART_LIGHT("no-heart-light.png"),
		REPEAT_LIGHT("repeat-light.png"),
		REPEAT_1_LIGHT("repeat-1-light.png"),
		NO_REPEAT_LIGHT("no-repeat-light.png"),
		SHUFFLE_LIGHT("shuffle-light.png"),
		NO_SHUFFLE_LIGHT("no-shuffle-light.png"),
		PREV_DARK("prev-dark.png"),
		NEXT_DARK("next-dark.png"),
		PLAY_DARK("play-dark.png"),
		PAUSE_DARK("pause-dark.png"),
		HEART_DARK("heart-dark.png"),
		NO_HEART_DARK("no-heart-dark.png"),
		REPEAT_DARK("repeat-dark.png"),
		REPEAT_1_DARK("repeat-1-dark.png"),
		NO_REPEAT_DARK("no-repeat-dark.png"),
		SHUFFLE_DARK("shuffle-dark.png"),
		NO_SHUFFLE_DARK("no-shuffle-dark.png");

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
