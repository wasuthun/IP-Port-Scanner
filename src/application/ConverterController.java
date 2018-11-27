package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConverterController {

	@FXML
	Button convertBt;
	@FXML
	Button clearBt;
	@FXML
	TextField inputField;
	@FXML
	TextField outputField;

	@FXML
	public void initialze() {
		outputField.setEditable(true);
		outputField.setDisable(false);
	}

	public void convert(ActionEvent e) {
		System.out.println("convert");
	}

	public void clear(ActionEvent e) {
		System.out.println("Clear");
	}
}
