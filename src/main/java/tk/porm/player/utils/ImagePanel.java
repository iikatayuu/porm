package tk.porm.player.utils;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	public static final long serialVersionUID = 1L;

	private Image image;
	private int width;
	private int height;

	public ImagePanel(Image image, int width, int height) {
		this.image = image;
		this.width = width;
		this.height = height;
	}

	public void setImage(Image image) {
		this.image = image;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Image tmp = this.image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		graphics.drawImage(tmp, 0, 0, null);
	}
}
