package pl.myPortfolio.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MoviePlayerController implements Initializable {

	@FXML
	private MenuItem openMenu;

	@FXML
	private ToggleButton playStopButton;

	@FXML
	private MediaView moviePlayer;

	@FXML
	private Button perviousButton;

	@FXML
	private Slider movieSlider;

	@FXML
	private MenuItem closeMenu;

	@FXML
	private Button repeatButton;

	@FXML
	private MenuItem aboutMenu;

	@FXML
	private Button nextButton;

	@FXML
	private Slider volumeSlider;

	@FXML
	private Button stopButton;

	@FXML
	private ToggleButton muteButton;

	@FXML
	private Label timeLabel;
	Media media;

	MediaPlayer player;

	public void initialize(URL location, ResourceBundle resources) {
		displayContorol();
		menu();
		audioControl();
		volumeSlider();

	}

	public void displayContorol() {

		playStopButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				moviePlayer.setMediaPlayer(player);
				moviePlayer.setFitHeight(player.getMedia().getHeight());
				moviePlayer.setFitWidth(player.getMedia().getWidth());
				player.play();

			}
		});

		stopButton.setOnAction(x -> player.pause());

		repeatButton.setOnAction(x -> player.stop());
		nextButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				player.seek(player.getCurrentTime().multiply(1.3));
			}
		});
		perviousButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				player.seek(player.getCurrentTime().divide(1.3));

			}
		});

	}

	public void menu() {

		openMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (media != null) {
					media = null;
				}
				load();

			}
		});

		closeMenu.setOnAction(x -> Platform.exit());
		aboutMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {// obsluga okna about
				Parent parent = null;
				try {
					parent = FXMLLoader.load(getClass().getResource("/pl/myPortfolio/view/HelpPane.fxml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.setTitle("Mp3Player v1.0 - about");
				stage.setScene(scene);
				stage.show();
			}
		});
	}

	public void load() {
		if (this.media != null || player != null) {

			player.stop();
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose movie file");
		ExtensionFilter extensionFilter = new ExtensionFilter("Movie files", "*.mp4", "*.mp3", "*.flv");
		fileChooser.getExtensionFilters().add(extensionFilter);
		File file = fileChooser.showOpenDialog(new Stage());
		String path = file.getPath();

		Media media = new Media(new File(path).toURI().toString());
		this.media = media;
		MediaPlayer player = new MediaPlayer(media);
		this.player = player;
		getMovieLength();
		videoSlider();

	}

	public void audioControl() {

		muteButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (muteButton.isSelected()) {
					player.setVolume(0);

				} else {
					player.setVolume(100);

				}

			}
		});

	}

	public void volumeSlider() {

		final double minVolume = 0;
		final double maxVolume = 1;
		volumeSlider.setMin(minVolume);
		volumeSlider.setMax(maxVolume);
		volumeSlider.setValue(maxVolume);
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				player.setVolume(newValue.doubleValue());
			}
		});

	}

	public void videoSlider() {

		player.setOnReady(new Runnable() {

			@Override
			public void run() {
				movieSlider.setMax(getMovieLength());

			}
		});
		player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
			@Override
			public void changed(ObservableValue<? extends Duration> arg, Duration oldVal, Duration newVal) {
				movieSlider.setValue(newVal.toSeconds());
				timeLabel.setText(formatTime(player.getCurrentTime(), player.getMedia().getDuration()));
			}
		});

		movieSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (movieSlider.isValueChanging()) {
					player.seek(Duration.seconds(newValue.doubleValue()));
				}

			}
		});

	}

	public double getMovieLength() {
		if (media != null) {
			return media.getDuration().toSeconds();

		} else {
			return 0;
		}
	}

	private static String formatTime(Duration elapsed, Duration duration) {
		int intElapsed = (int) Math.floor(elapsed.toSeconds());
		int elapsedHours = intElapsed / (60 * 60);
		if (elapsedHours > 0) {
			intElapsed -= elapsedHours * 60 * 60;
		}
		int elapsedMinutes = intElapsed / 60;
		int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

		if (duration.greaterThan(Duration.ZERO)) {
			int intDuration = (int) Math.floor(duration.toSeconds());
			int durationHours = intDuration / (60 * 60);
			if (durationHours > 0) {
				intDuration -= durationHours * 60 * 60;
			}
			int durationMinutes = intDuration / 60;
			int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
			if (durationHours > 0) {
				return String.format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds,
						durationHours, durationMinutes, durationSeconds);
			} else {
				return String.format("%02d:%02d/%02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes,
						durationSeconds);
			}
		} else {
			if (elapsedHours > 0) {
				return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
			} else {
				return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
			}
		}
	}

}
