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
import java.awt.Image;

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
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;

import tk.porm.player.database.DatabaseConnection;
import tk.porm.player.interfaces.PlayerListener;
import tk.porm.player.objects.Song;
import tk.porm.player.objects.Songs;
import tk.porm.player.objects.SongPlayer;
import tk.porm.player.utils.ImagePanel;
import tk.porm.player.utils.SystemBrowser;

public class App {
	private JFrame frame;
	private SongPlayer player;
	private Songs songs;
	private ArrayList<Song> songsList;
	private int selected;

	private DefaultTableModel tableModel;
	private ImagePanel albumImgPane;
	private JLabel labelTitle;
	private JLabel labelArtist;
	private JButton btnTogglePlay;
	private JTextField tfSearch;
	private JProgressBar progressBar;

	private ImageIcon imgPrev;
	private ImageIcon imgNext;
	private ImageIcon imgPlay;
	private ImageIcon imgPause;

	public static void main(String[] args) {
		final DatabaseConnection dc = new DatabaseConnection();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App app = new App(dc.connection);
					app.frame.setVisible(true);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
	}

	public App(Connection connection) {
		this.songs = new Songs(connection);
		this.selected = -1;

		ClassLoader loader = getClass().getClassLoader();
		URL iconRes = loader.getResource("icon.png");
		String iconPath = iconRes.getPath();
		ImageIcon imgIcon = new ImageIcon(iconPath);
		Image icon = imgIcon.getImage();

		URL imgPrevRes = loader.getResource("prev.png");
		String imgPrevPath = imgPrevRes.getPath();
		imgPrev = new ImageIcon(imgPrevPath);

		URL imgNextRes = loader.getResource("next.png");
		String imgNextPath = imgNextRes.getPath();
		imgNext = new ImageIcon(imgNextPath);

		URL imgPlayRes = loader.getResource("play.png");
		String imgPlayPath = imgPlayRes.getPath();
		imgPlay = new ImageIcon(imgPlayPath);

		URL imgPauseRes = loader.getResource("pause.png");
		String imgPausePath = imgPauseRes.getPath();
		imgPause = new ImageIcon(imgPausePath);

		frame = new JFrame(); 
		frame.setTitle("Player/Organizer Music");
		frame.setIconImage(icon);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 720, 500);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel detailsPane = new JPanel();
		detailsPane.setBounds(0, 0, 220, 460);
		detailsPane.setLayout(null);
		contentPane.add(detailsPane);

		try {
			URL resource = loader.getResource("album.jpg");
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
		labelTitle.setFont(labelTitle.getFont().deriveFont(labelTitle.getFont().getStyle() | Font.BOLD, 16f));
		labelTitle.setBounds(10, 140, 200, 23);
		detailsPane.add(labelTitle);
		
		labelArtist = new JLabel("SONG ARTIST", SwingConstants.CENTER);
		labelArtist.setFont(UIManager.getFont("Label.font"));
		labelArtist.setBounds(10, 160, 200, 23);
		detailsPane.add(labelArtist);

		progressBar = new JProgressBar();
		progressBar.setBounds(30, 200, 160, 8);
		detailsPane.add(progressBar);

		JButton btnPrev = new JButton(imgPrev);
		btnPrev.setIcon(imgPrev);
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected == -1) return;
				selected = selected == 0 ? selected = songsList.size() - 1 : selected - 1;
				playSelected();
			}
		});
		btnPrev.setBorder(BorderFactory.createEmptyBorder());
		btnPrev.setContentAreaFilled(false);
		btnPrev.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnPrev.setBounds(30, 220, 30, 30);
		detailsPane.add(btnPrev);
		
		JButton btnNext = new JButton(imgNext);
		btnNext.setIcon(imgNext);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected == -1) return;
				selected = selected == songsList.size() - 1 ? 0 : selected + 1;
				playSelected();
			}
		});
		btnNext.setBorder(BorderFactory.createEmptyBorder());
		btnNext.setContentAreaFilled(false);
		btnNext.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnNext.setBounds(160, 220, 30, 30);
		detailsPane.add(btnNext);

		btnTogglePlay = new JButton(imgPause);
		btnTogglePlay.setIcon(imgPlay);
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
		btnTogglePlay.setBorder(BorderFactory.createEmptyBorder());
		btnTogglePlay.setContentAreaFilled(false);
		btnTogglePlay.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnTogglePlay.setBounds(95, 220, 30, 30);
		detailsPane.add(btnTogglePlay);

		JPanel searchPane = new JPanel();
		searchPane.setBounds(220, 0, 480, 40);
		searchPane.setLayout(null);
		contentPane.add(searchPane);

		tfSearch = new JTextField();
		tfSearch.setBounds(0, 10, 380, 23);
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

		String[] cols = { "Album Cover", "Title", "Artist" };
		tableModel = new DefaultTableModel(cols, 0) {
			public static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable (int row, int col) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int column) {
				return column == 0 ? ImageIcon.class : super.getColumnClass(column);
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
		TableColumnModel model = songsListTable.getColumnModel();
		songsListTable.setRowHeight(120);
		model.getColumn(0).setPreferredWidth(20);

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
				chooser.showOpenDialog(frame);
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
					JOptionPane.showMessageDialog(frame, "Song(s) was added to database successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
					tfSearch.setText("");
					loadSongs("");
				}
			}
		});
		
		JButton btnDelete = new JButton("Remove Selected");
		btnDelete.setBounds(260, 8, 115, 23);
		actionsPane.add(btnDelete);

		JButton btnOpenConverter = new JButton("YT Downloader");
		btnOpenConverter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SystemBrowser.open("https://yt-downloader.eidoriantan.me");
			}
		});
		btnOpenConverter.setBounds(0, 8, 128, 23);
		actionsPane.add(btnOpenConverter);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected >= 0) {
					Song selectedSong = songsList.get(selected);
					int selectedID = selectedSong.getID();
					selected = -1;
					songs.removeSong(selectedID);
					JOptionPane.showMessageDialog(frame, "Song(s) was deleted to database successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
			String location = song.getLocation();
			ImageIcon album = null;

			try {
				Mp3File mp3file = new Mp3File(location);
				if (mp3file.hasId3v2Tag()) {
					ID3v2 tags = mp3file.getId3v2Tag();
					byte[] imgData = tags.getAlbumImage();

					ByteArrayInputStream stream = new ByteArrayInputStream(imgData);
					BufferedImage albumImg = ImageIO.read(stream);
					Image resized = albumImg.getScaledInstance(110, 110, Image.SCALE_SMOOTH);
					album = new ImageIcon(resized);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			Object[] data = { album, song.getTitle(), song.getArtist() };
			tableModel.addRow(data);
		}
	}

	public void playSelected () {
		if (selected < 0) return;

		if (player != null) {
			player.terminate();
			player = null;
		}

		btnTogglePlay.setIcon(imgPause);
		try {
			Song selectedSong = songsList.get(selected);
			String location = selectedSong.getLocation();
			String title = selectedSong.getTitle();
			String artist = selectedSong.getArtist();
			File songFile = new File(location);
			byte[] imgData = null;

			try {
				Mp3File mp3file = new Mp3File(location);
				if (mp3file.hasId3v2Tag()) {
					ID3v2 tags = mp3file.getId3v2Tag();
					imgData = tags.getAlbumImage();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
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
						btnTogglePlay.setIcon(imgPause);
					}

					@Override
					public void onStop() {
						btnTogglePlay.setIcon(imgPlay);
					}
				});
				player.play();
			} else {
				JOptionPane.showMessageDialog(frame, "File was not found on the system", "Failed", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
