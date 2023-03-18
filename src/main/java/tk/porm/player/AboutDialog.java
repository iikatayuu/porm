package tk.porm.player;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class AboutDialog extends JDialog {
	public static final long serialVersionUID = 1L;

	public AboutDialog(JFrame parent) {
		super(parent, "POrM About Us", true);

		setLocationRelativeTo(parent);
		setBounds(20, 20, 300, 150);

		JLabel labelAbout = new JLabel("About Us");
		labelAbout.setFont(new Font("Arial", Font.BOLD, 18));
		getContentPane().add(labelAbout, BorderLayout.NORTH);
	}
}
