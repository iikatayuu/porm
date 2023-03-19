package tk.porm.player;

import java.io.InputStream;

import java.awt.Rectangle;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import tk.porm.player.utils.ImagePanel;

public class AboutDialog extends JDialog {
	public static final long serialVersionUID = 1L;

	public AboutDialog(JFrame parent) {
		super(parent, "POrM About Us", true);

		ClassLoader loader = getClass().getClassLoader();
		InputStream iconStream = loader.getResourceAsStream("icon.png");
		BufferedImage iconImg = null;
		try {
			iconImg = ImageIO.read(iconStream);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		Rectangle parentBounds = parent.getBounds();
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(parentBounds.x + 100, parentBounds.y + 100, 276, 340);
		getContentPane().setLayout(null);

		ImagePanel panel = new ImagePanel(iconImg, 80, 80);
		panel.setBounds(95, 15, 75, 75);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblTitle = new JLabel("<html><p align=\"center\">Player/Organizer for Music (POrM)</p></html>");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitle.setBounds(10, 97, 240, 40);
		getContentPane().add(lblTitle);
		
		JLabel lblCredits = new JLabel("Credits:", SwingConstants.CENTER);
		lblCredits.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCredits.setBounds(10, 144, 240, 20);
		getContentPane().add(lblCredits);

		JLabel lblTan = new JLabel("Tan, Adriane Justine", SwingConstants.CENTER);
		lblTan.setBounds(10, 167, 240, 16);
		getContentPane().add(lblTan);

		JLabel lblVillanueva = new JLabel("Villanueva, Lanz Joseph", SwingConstants.CENTER);
		lblVillanueva.setBounds(10, 182, 240, 16);
		getContentPane().add(lblVillanueva);

		JLabel lblLocson = new JLabel("Locson, Nicholas Gabriel", SwingConstants.CENTER);
		lblLocson.setBounds(10, 197, 240, 16);
		getContentPane().add(lblLocson);

		JLabel lblCalimpong = new JLabel("Calimpong, Chrysler Vaughn", SwingConstants.CENTER);
		lblCalimpong.setBounds(10, 213, 240, 16);
		getContentPane().add(lblCalimpong);

		JLabel lblTantiado = new JLabel("Tantiado, Nathan Xavier", SwingConstants.CENTER);
		lblTantiado.setBounds(10, 229, 240, 16);
		getContentPane().add(lblTantiado);

		JLabel lblCopyright = new JLabel("Copyright (C) 2023", SwingConstants.CENTER);
		lblCopyright.setBounds(10, 275, 240, 16);
		getContentPane().add(lblCopyright);
	}
}
