package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import util.NetworkScanner;

public class ConverterController {
	@FXML
	Button convertBt;
	@FXML
	Button clearBt;
	@FXML
	TextField inputField;
	@FXML
	TextField outputField;

	private NetworkScanner scanner;

	@FXML
	public void initialize() {
		scanner = new NetworkScanner();
		outputField.setDisable(true);
	}

	public void convert(ActionEvent e) {
		System.out.println("convert");
		outputField.setText(scanner.dnsLookup(inputField.getText()));
	}

	public void clear(ActionEvent e) {
		inputField.clear();
		outputField.clear();
	}
}
