package pl.myPortfolio.model;

import java.io.File;

import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Movie {

	private Media media;

	public Media load() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose movie file");
		ExtensionFilter extensionFilter = new ExtensionFilter("Movie files", "*.mp4", "*.mp3", "*.flv");
		fileChooser.getExtensionFilters().add(extensionFilter);
		File file = fileChooser.showOpenDialog(new Stage());
		String path = file.getPath();

		Media media = new Media(new File(path).toURI().toString());
		return media;

	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

}
