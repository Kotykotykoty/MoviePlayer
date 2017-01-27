package pl.myPortfolio.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Main extends Application {

	@Override

	public void start(Stage primaryStage) {
		final String appName = "Movie Player Application";
		try {
			Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/pl/myPortfolio/view/MoviePlayerPane.fxml"));

			Scene scene = new Scene(parent);

			primaryStage.setTitle(appName);
			primaryStage.setScene(scene);
			primaryStage.setHeight(600);
			primaryStage.setWidth(800);
			primaryStage.show();

			scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() == 2) {
						primaryStage.setFullScreen(true);
					}

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}