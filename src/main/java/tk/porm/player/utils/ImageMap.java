package tk.porm.player.utils;

import java.io.InputStream;
import java.util.HashMap;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
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
			String filename = key.filename;
			InputStream stream = loader.getResourceAsStream(filename);
			try {
				BufferedImage image = ImageIO.read(stream);
				ImageIcon img = new ImageIcon(image);
				map.put(key, img);
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public ImageIcon getIcon(ImageKey key) {
		return map.get(key);
	}
}
