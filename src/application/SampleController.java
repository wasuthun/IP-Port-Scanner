package application;

import java.util.Collections;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import util.NetworkScanner;

public class SampleController {
	@FXML
	private Button Play;
	@FXML
	private ListView<DisplayResult> view;
	@FXML
	private ListView<Integer> view2;
	@FXML
	private Button Stop;
	private Task<Void> scan_thread;
	private NetworkScanner scanner;
	@FXML
	private Button Pause;
	@FXML
	private ProgressBar bar;
	@FXML
	private TextField text;
	@FXML
	private TextField inputPort;

	@FXML
	public void handleMouseClick(MouseEvent arg0) {
		System.out.println("clicked on " + view.getSelectionModel().getSelectedItem());
		System.out.println(view.getSelectionModel().getSelectedItem().getClass());
		ObservableList<Integer> list = FXCollections.observableArrayList();
		DisplayResult displayPort = view.getSelectionModel().getSelectedItem();
		if (displayPort != null) {
			view2.getItems().clear();
			for (Integer port : displayPort.getPort()) {
				list.add(port);
			}
			Collections.sort(list, portOrder);
			view2.setItems(list);
			view2.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>() {
				@Override
				public ListCell<Integer> call(ListView<Integer> list) {
					return new UpdatePort();
				}
			});
		}

	}

	public void play(ActionEvent e) {
		// Scan IP from input
		System.out.println("Receive input ip => " + text.getText());
		String[] inputIP = text.getText().split("-");
		
		//Scan Port from input
		System.out.println("Receive input port => " + inputPort.getText());
		String[] inputPorts = inputPort.getText().split("-");
		
		// Implementation.
		// 1 IP.
		if (inputIP.length == 1) {
			String focusIP = inputIP[0];
			final String[] tokens = focusIP.split("\\.");
			if (tokens.length == 4) {
				System.out.println("Input IP has right pattern.");
				// 1 Port
				if(inputPorts.length == 1) {
					int focusPort = Integer.parseInt(inputPorts[0]);
					// Scan only 1 IP with 1 Port
					scan_thread = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							// scanner.scan(inputIP[0], inputIP[0], 0, 100);
							scanner.scan(focusIP, focusIP, focusPort, focusPort);
							return null;
						}
					};
					bar.progressProperty().bind(scan_thread.progressProperty());
					new Thread(scan_thread).start();
				}
				// 2 Ports
				else if(inputPorts.length == 2) {
					int startPort = Integer.parseInt(inputPorts[0]);
					int endPort = Integer.parseInt(inputPorts[1]);
					// Scan only 1 IP between 2 Ports
					scan_thread = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							// scanner.scan(inputIP[0], inputIP[0], 0, 100);
							scanner.scan(focusIP, focusIP, startPort, endPort);
							return null;
						}
					};
					bar.progressProperty().bind(scan_thread.progressProperty());
					new Thread(scan_thread).start();
				}
				else {
					System.out.println("Input Port has wrong pattern, try again.");
				}
			} else {
				System.out.println("Input IP has wrong pattern, try again.");
			}
		} 
		// 2 IP.
		else if (inputIP.length == 2) {
			boolean shouldDo = true;
			String startIP = inputIP[0];
			String endIP = inputIP[1];
			for (int i = 0; i < inputIP.length; i++) {
				final String[] tokens = inputIP[i].split("\\.");
				if (tokens.length != 4) {
					shouldDo = false;
				}
			}
			if (shouldDo) {
				// 1 Port
				if(inputPorts.length == 1) {
					int focusPort = Integer.parseInt(inputPorts[0]);
					// Scan 2 IP in 1 Port
					scan_thread = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							// scanner.scan(inputIP[0], inputIP[1], 0, 100);
							scanner.scan(startIP, endIP, focusPort, focusPort);
							return null;
						}
					};
					bar.progressProperty().bind(scan_thread.progressProperty());
					new Thread(scan_thread).start();
				}
				// 2 Port
				else if(inputPorts.length == 2) {
					int startPort = Integer.parseInt(inputPorts[0]);
					int endPort = Integer.parseInt(inputPorts[0]);
					// Scan 2 IP between 2 Ports
					scan_thread = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							// scanner.scan(inputIP[0], inputIP[1], 0, 100);
							scanner.scan(startIP, endIP, startPort, endPort);
							return null;
						}
					};
					bar.progressProperty().bind(scan_thread.progressProperty());
					new Thread(scan_thread).start();
				}
				else {
					System.out.println("Input Port has wrong pattern, try again.");
				}
			} else {
				System.out.println("Some input IP has wrong pattern, try again.");
			}
		} 
		else {
			System.out.println("Input IP is error, try again.");
			text.setText("");
		}
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
		scanner.stop();

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

	private static class UpdatePort extends ListCell<Integer> {
		@Override
		public void updateItem(Integer item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
				setText(item + "");
			}
		}
	}

	Comparator<Integer> portOrder = new Comparator<Integer>() {
		@Override
		public int compare(Integer m1, Integer m2) {
			return m1.compareTo(m2);
		}
	};

}
