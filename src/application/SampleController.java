package application;

import com.sun.glass.events.KeyEvent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SampleController {
	@FXML
	private Button Play;

	public void play(ActionEvent e) {
		System.out.println("varit");
	}
}
