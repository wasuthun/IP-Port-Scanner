package application;

import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import util.NetworkScanner;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;

/**
 * Create a JavaFX user interface with 1 input view and 2 observers that show
 * the counter value, so that all the views refer to the same Counter object.
 * 
 */
public class Main extends Application {
	private SampleController controller;

	/**
	 * Initialize of fxml for start application
	 */
	@Override
	public void start(Stage primaryStage) {
		NetworkScanner network = new NetworkScanner("127.0.0.1", "127.0.1.5", 0, 0);
		try {
			URL url = getClass().getResource("Sample.fxml");
			if (url == null) {
				System.out.println("Couldn't find file: Sample.fxml");
				Platform.exit();
			}
			FXMLLoader loader = new FXMLLoader(url);
			Parent root = loader.load();
			controller = loader.getController();
			controller.setNetworkScanner(network);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.setTitle("Network");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		NetworkObserver observer = new NetworkObserver(network);
		controller.show(observer);
	}

	/**
	 * Launch program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
