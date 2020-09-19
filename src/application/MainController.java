/*CSE 12141599 LEE EUI SEOK, 12150754 Chun Seung Whan
 * Java Programming (Tamer Professor)
 * Java Course Project (MainController.java)
 * Reporting Date : 2019.06.16
 */
package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.io.IOException;
import java.net.URL;

import java.util.List;

import java.util.ResourceBundle;
import java.util.Vector;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import com.jfoenix.controls.JFXButton;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.util.Duration;
import javafx.scene.media.MediaView;

import javafx.stage.FileChooser;

import javafx.stage.Stage;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.control.Label;

import javafx.scene.control.MenuItem;

import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.Pane;

public class MainController implements Initializable {
	public ObservableList<String> listItems;

	final Label currentlyPlaying = new Label();
	@FXML
	private ChangeListener<Duration> progressChangeListener;
	@FXML
	private JFXButton minimizeButton;
	@FXML
	private JFXButton closeButton;
	@FXML
	private JFXButton playlistButton;

	@FXML
	private ImageView musicAlbum;
	@FXML
	private Label currentSong;
	@FXML
	private JFXTextField lyricsCurrent;
	@FXML
	private JFXSlider currentPlaytime;

	@FXML
	private JFXButton prevButton;
	@FXML
	private JFXButton playButton;
	@FXML
	private JFXButton pauseButton;
	@FXML
	private JFXButton nextButton;
	@FXML
	private JFXButton stopButton;
	@FXML
	private JFXSlider volSlider;
	@FXML
	private Label volButton;

	@FXML
	private MediaView mV;
	private MediaPlayer mP;
	private Media mE;

	@FXML
	private Pane panePlaylist;

	@FXML
	private MenuItem menuOpenFile;
	@FXML
	private MenuItem menuNewPlaylist;
	@FXML
	private MenuItem menuOpenPlaylist;
	@FXML
	private MenuItem menuRemovePlaylist;
	@FXML
	private MenuItem menuAddSong;
	@FXML
	private MenuItem menuRemoveSong;

	@FXML
	private Label playlistName;

	@FXML
	private Label showInfo;
	@FXML
	private JFXButton playlistMinimize;
	@FXML
	private Label playlistStatus;
	@FXML
	private JFXListView<String> playlistView;
	@FXML
	private AnchorPane mainAnchor;
	// List for txt file
	private List<File> file = new Vector<File>();
	private File selectedFile;

	// List of SongInfo class created to save data of current playlist's audiofiles
	private List<SongInfo> s_Info = new Vector<SongInfo>();

	// counter for current index to use for both s_Info and listItems
	int currentSongIndex = 0;

	// counter for
	int cnt = 0;

	// counter for amount of playlist created. MAX set to 10
	int cnt_playlist = 0;

	// FileChooser's filters used in various situation to load and save files
	FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
	FileChooser.ExtensionFilter mp3Filter = new FileChooser.ExtensionFilter("Audio files", "*.mp3", "*.wav");

	// Initialization of few configurations that couldn't be changed in
	// SceneBuilder2
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// playlist is not visible by default
		panePlaylist.setVisible(false);

		// a slider that shows current time. Initialized to not able to be configured
		// by mouseEvent
		currentPlaytime.setMouseTransparent(true);

		// listItems that will show in ListView. Basically to show playlist
		listItems = FXCollections.observableArrayList();

		// setting listView to show the collection of items
		playlistView.setItems(listItems);

		// default path for checking txt == current project path
		String path2 = new File("./").getAbsolutePath();

		// load files to check for txt files
		File enter = new File(path2);

		// for loop to count and stack txt files
		for (File i : enter.listFiles()) {
			if (i.getName().endsWith((".txt"))) {
				file.add(i);
				cnt_playlist++;
			}
		}

		// if there is no playlist.txt in directory, do not allow any playlist loading
		// and song adding
		currentPlaytime.setValue(0);
		if (file.isEmpty()) {
			menuOpenPlaylist.setDisable(true);
			menuRemovePlaylist.setDisable(true);
			menuAddSong.setDisable(true);
			menuRemoveSong.setDisable(true);
		} else {
			menuAddSong.setDisable(true);
			menuRemoveSong.setDisable(true);
		}

	}

	// function to double click music from list view and play it
	public void doubleClickMusic(MouseEvent e0) {
		try {

			// get 2 click to start act
			if (e0.getClickCount() == 2) {
				if (mP != null) {
					mP.stop();
				}
				int currentPosition = playlistView.getSelectionModel().getSelectedIndex();
				currentSongIndex = currentPosition;
				String nextSong = s_Info.get(currentPosition).getSongPath();
				createMedia(nextSong, currentPosition);
			}
		} catch (ArrayIndexOutOfBoundsException re) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Look, an Error Dialog");
			alert.setContentText("ArrayIndexOutOfBoundsException: No file was found");
			alert.showAndWait();
		}
	}

	// function to get what to print on the JFXtxtField
	public void Artist_Song_name(int current) {
		String Song = s_Info.get(current).getSongName();
		String Artist = s_Info.get(current).getArtistName();
		showInfo.setText(Song + " - " + Artist);

	}

	// create and start MediaPlayer. Also contains several functions to avoid
	// exceptions and act on it
	private void createMedia(String musicPath, int current) {
		String absolutePath = new File(musicPath).getAbsolutePath();
		try {

			// create mediaPlayer
			mE = new Media(new File(absolutePath).toURI().toString());
			mP = new MediaPlayer(mE);

			// allow to select multiple listitems
			playlistView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			// set volume according to current player
			volSlider.setValue(mP.getVolume() * 100);
			volSlider.valueProperty().addListener(new InvalidationListener() {
				@Override
				public void invalidated(Observable observable) {
					mP.setVolume(volSlider.getValue() / 100);
				}
			});

			// set the max value of the time showing JFXSlider
			currentPlaytime.setMax(s_Info.get(current).getDur() * 100);
			// set slider when there is change in time-property of MediaPlayer
			mP.currentTimeProperty().addListener(
					(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
						currentPlaytime.setValue(newValue.toSeconds() * 100);
					});
			Artist_Song_name(current);
			// function to show the current playing song at the top of the playlist
			playlistView.scrollTo(current);
			mP.play();
			// now it's playing so show pause button
			playButton.setVisible(false);
			pauseButton.setVisible(true);

			// function to play next song in the list if current song is done playing
			auto_next();
		} catch (RuntimeException re) {
			// Handle construction errors
			System.out.println("Caught Exception: " + re.getMessage());
		}
	}

	// bring the Node and minimize
	public void minimize(ActionEvent e0) {
		Stage s = (Stage) ((Node) e0.getSource()).getScene().getWindow();
		// minimize
		s.setIconified(true);
	}

	// function to hide the playlist pane
	public void hideList(ActionEvent e0) {
		try {
			panePlaylist.setVisible(false);
		} // no exception found during trials
		catch (Exception re) {
			re.printStackTrace();
		}
	}

	// function to show the playlist pane
	public void showList(ActionEvent e0) {
		try {
			panePlaylist.setVisible(true);
		} // no exception found during trials
		catch (Exception re) {
			re.printStackTrace();
		}
	}

	// function to close the whole player
	public void close(ActionEvent e0) {
		Stage s = (Stage) ((Node) e0.getSource()).getScene().getWindow();
		s.close();
	}

	// function to play paused or stopped player. also starts from start of the
	// loaded playlist if no song is loaded
	public void play(ActionEvent e0) {
		try {
			if (s_Info.get(0).getSongPath() != null) {
				currentSongIndex = 0;
				createMedia(s_Info.get(0).getSongPath(), 0);
			}
			mP.play();
			playButton.setVisible(false);
			pauseButton.setVisible(true);
		} catch (ArrayIndexOutOfBoundsException re) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Look, an Error Dialog");
			alert.setContentText("ArrayIndexOutOfBoundsException: No file was loaded yet");
			alert.showAndWait();
		}
	}

	// pause the player. set pause button invisible and play button visible
	public void pause(ActionEvent e0) {
		try {
			mP.pause();
			playButton.setVisible(true);
			pauseButton.setVisible(false);
		} // no exception found during trials
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	// stop the player. set pause button invisible and play button visible
	public void stop(ActionEvent e0) {
		try {
			mP.seek(mP.getStartTime());
			mP.pause();
			playButton.setVisible(true);
			pauseButton.setVisible(false);
		} catch (ArrayIndexOutOfBoundsException re) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Look, an Error Dialog");
			alert.setContentText("ArrayIndexOutOfBoundsException: No file was loaded yet");
			alert.showAndWait();
		}
	}

	// try catch to use exception like an if else, since MediaPlayer does not stop
	// even after reaching end of file
	// filter that checks if current MediaPlayer have reached its initial end of
	// file or not. runnable is used to run constantly
	public void auto_next() {
		// media knows when it finishes, but mediaplayer doesnt stop current mediaplayer
		// and decide which song in list to play,
		// by same way we did in next_song
		mP.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				try {
					if (s_Info.get(currentSongIndex + 1) != null) {
						currentSongIndex++;
					}
				} catch (ArrayIndexOutOfBoundsException ei) {
					currentSongIndex = 0;
				}
				createMedia(s_Info.get(currentSongIndex).getSongPath(), currentSongIndex);
			}
		});
	}

	// play the next song in list. If current song is last song in list, go back to
	// first song in the playlist
	public void next_Song(ActionEvent e) {
		// if there is song still playing, stop
		if (mP != null) {
			mP.stop();
		}

		// try catch to use exception like an if else, since Vector give trash number
		// not null when reached over vector's last object
		try {
			// if more song then index of next song
			if (s_Info.get(currentSongIndex + 1) != null) {
				currentSongIndex++;
			}
		}
		// back to the first song
		catch (ArrayIndexOutOfBoundsException ei) {
			currentSongIndex = 0;
		}

		// play the song chosen
		createMedia(s_Info.get(currentSongIndex).getSongPath(), currentSongIndex);
	}

	// play the previous song in list. If current song is first song in list, go to
	// last song in the playlist
	public void prev_Song(ActionEvent e) {
		try {
			if (mP != null) {
				mP.stop();
			}
			if (currentSongIndex > 0) {
				currentSongIndex--;
			} else {
				currentSongIndex = s_Info.size() - 1;
			}
			createMedia(s_Info.get(currentSongIndex).getSongPath(), currentSongIndex);
		} catch (ArrayIndexOutOfBoundsException re) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Look, an Error Dialog");
			alert.setContentText("ArrayIndexOutOfBoundsException: No file was loaded yet");
			alert.showAndWait();
		}
	}

	public void filechoose(ActionEvent action)
			throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
	// this is for Add_song  add song to GUI playlist and also playlist.txt 
		String musicStr = "";
		FileChooser fc = new FileChooser();
		fc.setTitle("Open Music File");
		fc.getExtensionFilters().add(mp3Filter);
		String path = new File("./").getAbsolutePath();
		fc.setInitialDirectory(new File(path));
		// exception check for critical point
		List<File> list = null;
		try {
			list = fc.showOpenMultipleDialog(mainAnchor.getScene().getWindow());

			for (File i : list) {
				// external reference to jaudiotagger to load the songs metadata to get Singer
				// and Name and Duration
				AudioFile aF = AudioFileIO.read(new File(i.getAbsolutePath()));
				// load metadata from audiofile
				Tag tag1 = aF.getTag();
				// save data wanted to custom class SongInfo
				SongInfo songInfo = new SongInfo(tag1.getFirst(FieldKey.TITLE), tag1.getFirst(FieldKey.ARTIST),
						i.getAbsolutePath(), clock(aF.getAudioHeader().getTrackLength()),
						aF.getAudioHeader().getTrackLength());
				s_Info.add(songInfo);
				listItems.add(songInfo.toString2());
			}
			// write data to txt then close writer
			FileWriter play = new FileWriter(selectedFile, true);
			BufferedWriter bw = new BufferedWriter(play);
			for (File i : list) {
				musicStr = i.getAbsolutePath();
				bw.append(musicStr);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			play.close();
		} catch (NullPointerException e) {
			System.out.print("null");
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// conversion to show time ,duration of song 
	public String clock(int duration) {
		int minute = duration / 60;
		int second = duration % 60;
		String time = minute + ":" + second;
		return time;
	}

	// Open Playlist function as requested
	public void readplaylist1(ActionEvent e0)
			throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		FileChooser fc = new FileChooser();
		// txtFilter
		fc.getExtensionFilters().add(txtFilter);
		String path = new File("./").getAbsolutePath();
		fc.setInitialDirectory(new File(path));
		File f = fc.showOpenDialog(mainAnchor.getScene().getWindow());
		BufferedReader reader;
		// if txt file is loaded properly, load songs listed in the text file
		if (f != null) {
			try {
				playlistStatus.setText(f.getName());
				File yourFile = new File(f.getAbsolutePath());
				listItems.clear();
				s_Info.clear();
				reader = new BufferedReader(new FileReader(yourFile));
				String line = reader.readLine();
				while (line != null) {
					File song = new File(line);
					AudioFile aF = AudioFileIO.read(new File(song.getAbsolutePath()));
					Tag tag1 = aF.getTag();
					SongInfo songInfo = new SongInfo(tag1.getFirst(FieldKey.TITLE), tag1.getFirst(FieldKey.ARTIST),
							song.getAbsolutePath(), clock(aF.getAudioHeader().getTrackLength()),
							aF.getAudioHeader().getTrackLength());// this is Song information of length, name, artist 
					s_Info.add(songInfo);  
					listItems.add(songInfo.toString2());
					line = reader.readLine();
				}
				reader.close();
				selectedFile = yourFile;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// allow Adding songs and Removing songs to the playlist
		menuAddSong.setDisable(false);
		menuRemoveSong.setDisable(false);
	}

	// Remove Playlist function as requested
	public void removePlaylist(ActionEvent e0) {
		FileChooser fc = new FileChooser();
		String path = new File("./").getAbsolutePath();
		fc.setInitialDirectory(new File(path));
		fc.setTitle("Remove File");
		fc.getExtensionFilters().add(txtFilter);
		File f = fc.showOpenDialog(mainAnchor.getScene().getWindow());
		if (f != null) {
			if (selectedFile != null) { // this is for checking GUI playlist  if GUI playlist is same with remove one -> it will refresh listview and erase it
				if (f.getName().equals(selectedFile.getName())) {
					listItems.clear();
					menuAddSong.setDisable(true);
					menuRemoveSong.setDisable(true);
					playlistStatus.setText("");
				}
			}
			for (File i : file) {
				if (f.getName().equals(i.getName())) {
					f.delete();
					file.remove(i);
					break;
				}// remove file from vector and also path of file
			}
			cnt_playlist--;//count playlist will down
		}

	}

	// New Playlist function as requested
	public void createPlaylist(ActionEvent event) {
		if (cnt_playlist >= 10) {
			throw new ArithmeticException();
		}
		cnt = 0;
		for (File i : file) {
			if (i.getName().equals("newPlaylist" + cnt + ".txt")) {
				cnt++;
			}
		}
		File playlistTXT = new File("newPlaylist" + cnt + ".txt");
		playlistStatus.setText(playlistTXT.getName());
		try {
			playlistTXT.createNewFile();
			selectedFile = playlistTXT;
			listItems.clear();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file.add(playlistTXT);
		cnt++;
		cnt_playlist++;
		menuOpenPlaylist.setDisable(false);
		menuRemovePlaylist.setDisable(false);
		menuAddSong.setDisable(false);
		menuRemoveSong.setDisable(false);
	}

	// Add Song to Playlist function as requested
	public void openFile(ActionEvent e0)
			throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		if (cnt_playlist >= 10) {
			throw new ArithmeticException();
		}
		String musicStr = "";
		FileChooser fc = new FileChooser();
		fc.setTitle("Open File");
		fc.getExtensionFilters().add(mp3Filter);
		File f = fc.showOpenDialog(mainAnchor.getScene().getWindow());
		if (f != null) {

			musicStr = f.getAbsolutePath();
			FileWriter fw;
			try {
				cnt = 0;
				for (File i : file) {
					if (i.getName().equals("newPlaylist" + cnt + ".txt")) {
						cnt++;
					}
				}
				File playlistTXT = new File("newPlaylist" + cnt + ".txt");
				playlistStatus.setText(playlistTXT.getName());
				playlistTXT.createNewFile();
				fw = new FileWriter(playlistTXT);
				BufferedWriter bw = new BufferedWriter(fw);
				AudioFile aF = AudioFileIO.read(new File(musicStr));
				Tag tag1 = aF.getTag();
				SongInfo songInfo = new SongInfo(tag1.getFirst(FieldKey.TITLE), tag1.getFirst(FieldKey.ARTIST),
						f.getAbsolutePath(), clock(aF.getAudioHeader().getTrackLength()),
						aF.getAudioHeader().getTrackLength());
				s_Info.add(songInfo);
				listItems.add(songInfo.toString2());
				bw.write(musicStr);
				bw.newLine();
				bw.close();
				fw.close();
				selectedFile = playlistTXT;
				file.add(playlistTXT);
				cnt++;
				cnt_playlist++;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			menuOpenPlaylist.setDisable(false);
			menuRemovePlaylist.setDisable(false);
		}
	}

	// Remove Songs function as requested
	public void fileRemove(ActionEvent event) {
		ObservableList<Integer> selected = playlistView.getSelectionModel().getSelectedIndices(); // find selected index and write in to list
		SongInfo nullInfo = new SongInfo("", "", "", "", 0);
		s_Info.add(nullInfo);// when last thing is earsed -> index will be out of bound error so we put it
		listItems.add("");// when last thing is earsed -> index will be out of bound error so we put it

		for (int i : selected) {
			s_Info.get(i).setSongPath(null);
			listItems.set(i, null);
		}// find index of marked and change it to null
		for (int i = listItems.size() - 1; i >= 0; i--) {
			if (listItems.get(i) == null) {
				listItems.remove(i);
			}
			if (s_Info.get(i).getSongPath() == null) {
				s_Info.remove(i);
			}
		}//keep going to check it is null and if null remove it
		s_Info.remove(s_Info.size() - 1);
		listItems.remove(listItems.size() - 1);
		try {// this one is for update playlist file when it is erased 
			FileWriter fw = new FileWriter(selectedFile, false);
			BufferedWriter bw = new BufferedWriter(fw);
			listItems.clear();
			for (int i = 0; i < s_Info.size(); i++) {
				listItems.add(s_Info.get(i).toString2());
				bw.write(s_Info.get(i).getSongPath());
				bw.newLine();
			}
			bw.close();
			fw.close(); 
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
