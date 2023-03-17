package tk.porm.player;

import java.util.ArrayList;
import java.io.File;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;

import tk.porm.player.database.DatabaseConnection;
import tk.porm.player.interfaces.PlayerListener;
import tk.porm.player.objects.Song;
import tk.porm.player.objects.Songs;
import tk.porm.player.objects.SongPlayer;
import tk.porm.player.utils.ImagePanel;

public class App extends JFrame {
	public static final long serialVersionUID = 1L;

	private SongPlayer player;
	private Songs songs;
	private ArrayList<Song> songsList;
	private int selected;
	private DefaultTableModel tableModel;
	private ImagePanel albumImgPane;
	private JLabel labelTitle;
	private JLabel labelArtist;
	private JTextField tfSearch;
	private JButton btnTogglePlay;
	private JProgressBar progressBar;

	public static void main(String[] args) {
		final DatabaseConnection dc = new DatabaseConnection();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App app = new App(dc.connection);
					app.setVisible(true);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
	}

	public App(Connection connection) {
		this.songs = new Songs(connection);
		this.selected = -1;

		final JFrame app = this;

		setTitle("Player/Organizer Music");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 500);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel detailsPane = new JPanel();
		detailsPane.setBounds(0, 0, 220, 460);
		detailsPane.setLayout(null);
		contentPane.add(detailsPane);

		ClassLoader loader = getClass().getClassLoader();
		URL resource = loader.getResource("album.jpg");
		try {
			String imagePath = resource.getPath();
			File imageFile = new File(imagePath);
			BufferedImage image = ImageIO.read(imageFile);
			albumImgPane = new ImagePanel(image, 120, 120);
			albumImgPane.setBounds(50, 10, 120, 120);
			detailsPane.add(albumImgPane);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		labelTitle = new JLabel("SONG TITLE", SwingConstants.CENTER);
		labelTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
		labelTitle.setBounds(10, 141, 200, 23);
		detailsPane.add(labelTitle);
		
		labelArtist = new JLabel("SONG ARTIST", SwingConstants.CENTER);
		labelArtist.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelArtist.setBounds(10, 168, 200, 23);
		detailsPane.add(labelArtist);

		progressBar = new JProgressBar();
		progressBar.setBounds(30, 202, 160, 14);
		detailsPane.add(progressBar);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected == -1) return;
				selected = selected == songsList.size() - 1 ? 0 : selected + 1;
				playSelected();
			}
		});
		btnNext.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNext.setBounds(150, 230, 55, 23);
		detailsPane.add(btnNext);

		JButton btnPrev = new JButton("Prev");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected == -1) return;
				selected = selected == 0 ? selected = songsList.size() - 1 : selected - 1;
				playSelected();
			}
		});
		btnPrev.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnPrev.setBounds(10, 230, 55, 23);
		detailsPane.add(btnPrev);

		btnTogglePlay = new JButton("Pause");
		btnTogglePlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (player == null) return;
				if (player.isPaused()) {
					player.play();
				} else {
					player.pause();
				}
			}
		});
		btnTogglePlay.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnTogglePlay.setBounds(72, 230, 70, 23);
		detailsPane.add(btnTogglePlay);

		JPanel searchPane = new JPanel();
		searchPane.setBounds(220, 0, 480, 40);
		searchPane.setLayout(null);
		contentPane.add(searchPane);

		tfSearch = new JTextField();
		tfSearch.setBounds(0, 10, 370, 23);
		tfSearch.setColumns(10);
		searchPane.add(tfSearch);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String search = tfSearch.getText();
				loadSongs(search);
			}
		});
		btnSearch.setBounds(390, 10, 80, 23);
		searchPane.add(btnSearch);

		JPanel songsListPane = new JPanel();
		songsListPane.setBounds(220, 40, 480, 380);
		songsListPane.setLayout(null);
		contentPane.add(songsListPane);

		String[] cols = { "Title", "Artist" };
		tableModel = new DefaultTableModel(cols, 0) {
			public static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable (int row, int col) {
				return false;
			}
		};

		final JTable songsListTable = new JTable(tableModel);
		songsListTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked (MouseEvent e) {
				int clicked = e.getClickCount();
				selected = songsListTable.getSelectedRow();
				if (clicked == 2) {
					playSelected();
				}
			}
		});
		songsListTable.setBounds(0, 0, 480, 380);

		JScrollPane scrollPane = new JScrollPane(songsListTable);
		scrollPane.setBounds(0, 0, 480, 380);
		songsListPane.add(scrollPane);

		JPanel actionsPane = new JPanel();
		actionsPane.setBounds(220, 420, 480, 40);
		contentPane.add(actionsPane);
		actionsPane.setLayout(null);

		JButton btnBrowse = new JButton("Add files");
		btnBrowse.setBounds(385, 8, 90, 23);
		actionsPane.add(btnBrowse);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("MPEG3 Songs", "mp3");
				chooser.setMultiSelectionEnabled(true);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.addChoosableFileFilter(filter);
				chooser.showOpenDialog(app);
				File[] files = chooser.getSelectedFiles();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					String location = file.getPath();
					String title = "";
					String artist = "";

					try {
						Mp3File mp3file = new Mp3File(location);
						
						if (mp3file.hasId3v2Tag()) {
							ID3v2 tags = mp3file.getId3v2Tag();
							title = tags.getTitle();
							artist = tags.getArtist();
						} else if (mp3file.hasId3v1Tag()) {
							ID3v1 tags = mp3file.getId3v1Tag();
							title = tags.getTitle();
							artist = tags.getArtist();
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}

					if (title.equals("")) {
						title = Paths.get(location).getFileName().toString();
						artist = "";
					}

					songs.addSong(location, title, artist);
				}

				if (files.length > 0) {
					JOptionPane.showMessageDialog(app, "Song(s) was added to database successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
					tfSearch.setText("");
					loadSongs("");
				}
			}
		});
		
		JButton btnDelete = new JButton("Remove Selected");
		btnDelete.setBounds(260, 8, 115, 23);
		actionsPane.add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected >= 0) {
					Song selectedSong = songsList.get(selected);
					int selectedID = selectedSong.getID();
					selected = -1;
					songs.removeSong(selectedID);
					JOptionPane.showMessageDialog(app, "Song(s) was deleted to database successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
					tfSearch.setText("");
					loadSongs("");
				}
			}
		});

		loadSongs("");
	}

	public void loadSongs(String search) {
		songsList = songs.getSongs(search);
		tableModel.setRowCount(0);

		for (int i = 0; i < songsList.size(); i++) {
			Song song = songsList.get(i);
			String[] data = { song.getTitle(), song.getArtist() };

			tableModel.addRow(data);
		}
	}

	public void playSelected () {
		if (selected < 0) return;

		if (player != null) {
			player.terminate();
			player = null;
		}
		
		btnTogglePlay.setText("Pause");
		try {
			Song selectedSong = songsList.get(selected);
			String location = selectedSong.getLocation();
			File songFile = new File(location);
			String title = "";
			String artist = "";
			byte[] imgData = null;

			try {
				Mp3File mp3file = new Mp3File(location);
				
				if (mp3file.hasId3v2Tag()) {
					ID3v2 tags = mp3file.getId3v2Tag();
					title = tags.getTitle();
					artist = tags.getArtist();
					imgData = tags.getAlbumImage();
				} else if (mp3file.hasId3v1Tag()) {
					ID3v1 tags = mp3file.getId3v1Tag();
					title = tags.getTitle();
					artist = tags.getArtist();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			if (title.equals("")) {
				title = Paths.get(location).getFileName().toString();
			}

			labelTitle.setText(title);
			labelArtist.setText(artist);

			if (imgData != null) {
				ByteArrayInputStream stream = new ByteArrayInputStream(imgData);
				BufferedImage album = ImageIO.read(stream);
				albumImgPane.setImage(album);
			}

			if (songFile.exists()) {
				player = new SongPlayer(location);
				player.setPlayerListener(new PlayerListener() {
					@Override
					public void progress(int read, int length) {
						progressBar.setValue(read);
						progressBar.setMaximum(length);
					}

					@Override
					public void onStart() {
						btnTogglePlay.setText("Pause");
					}

					@Override
					public void onStop() {
						btnTogglePlay.setText("Play");
					}
				});
				player.play();
			} else {
				JOptionPane.showMessageDialog(this, "File was not found on the system", "Failed", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
