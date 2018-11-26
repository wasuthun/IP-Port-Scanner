package application;

import java.util.concurrent.ExecutionException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import util.NetworkScanner;

public class SampleController {
	@FXML
	private Button Play;
	@FXML
	private ListView<DisplayResult> view;
	@FXML
	private Button Stop;
	private Thread scan_thread;
	private NetworkScanner scanner;
	@FXML
	private Button Pause;

	@FXML
	public void handleMouseClick(MouseEvent arg0) {
		System.out.println("clicked on " + view.getSelectionModel().getSelectedItem());
	}

	public void play(ActionEvent e) {
		System.out.println("varit");
		scan_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					scanner.scan();
				} catch (InterruptedException | ExecutionException exception) {
					exception.printStackTrace();
				}
			}
		});
		scan_thread.start();

	}

	public void setNetworkScanner(NetworkScanner scanner) {
		this.scanner = scanner;
	}

	public void show(NetworkObserver obs) {
		view.setItems(obs.getList());
		view.setCellFactory(new Callback<ListView<DisplayResult>, ListCell<DisplayResult>>() {
			@Override
			public ListCell<DisplayResult> call(ListView<DisplayResult> list) {
				return new Update();
			}
		});
	}

	public void pause(ActionEvent e) {
		System.out.println("Pause");
	}

	public void stop(ActionEvent e) {
		System.out.println("Stop");
		if (scan_thread != null) {
		} else {
			System.out.println("null");
		}

	}

	private static class Update extends ListCell<DisplayResult> {
		@Override
		public void updateItem(DisplayResult item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
				setText(item.getIp());
			}
		}
	}

}
