package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
	private Button Help;
	@FXML
	private ProgressBar bar;

	@FXML
	public void handleMouseClick(MouseEvent arg0) {
		System.out.println("clicked on " + view.getSelectionModel().getSelectedItem());
		System.out.println(view.getSelectionModel().getSelectedItem().getClass());
		ObservableList<Integer> list = FXCollections.observableArrayList();
		DisplayResult displayPort = view.getSelectionModel().getSelectedItem();
		if (displayPort != null) {
			for (Integer port : displayPort.getPort()) {
				list.add(port);
			}
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
		System.out.println("varit");
		scan_thread = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				scanner.scan();
				return null;
			}
		};
		bar.progressProperty().bind(scan_thread.progressProperty());
		new Thread(scan_thread).start();

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

	TableView<WellPort> tableView;
	public static class WellPort {
		private String port;
		private String service;

		private WellPort(String sName, String pName) {
			this.service = sName;
			this.port = pName;
		}

		public String getPort() {
			return port;
		}

		public String getService() {
			return service;
		}

		public void setPort(String pName) {
			this.port = pName;
		}

		public void setService(String sName) {
			this.service = sName;
		}
	}

	public ObservableList<WellPort> getWellPort() {
		ObservableList<WellPort> wPorts = FXCollections.observableArrayList();
		wPorts.add(new WellPort("HTTP", "80"));
		wPorts.add(new WellPort("HTTPS", "443"));
		wPorts.add(new WellPort("FTP", "20,21"));
		wPorts.add(new WellPort("DNS", "53"));
		wPorts.add(new WellPort("SMTP", "25"));
		wPorts.add(new WellPort("POP3", "110"));
		wPorts.add(new WellPort("IMAP", "143"));
		wPorts.add(new WellPort("Telnet", "23"));
		wPorts.add(new WellPort("SSH", "22"));
		return wPorts;
	}

	private Stage priStage = new Stage();

	public void help(ActionEvent e) {
		priStage.setTitle("Help Information");

		TableColumn<WellPort, String> portName = new TableColumn<>("Port");
		portName.setMinWidth(200);
		portName.setCellValueFactory(new PropertyValueFactory<>("port"));

		TableColumn<WellPort, String> serviceName = new TableColumn<>("Service");
		serviceName.setMinWidth(200);
		serviceName.setCellValueFactory(new PropertyValueFactory<>("service"));
		tableView = new TableView<>();
		tableView.setItems(getWellPort());
		tableView.getColumns().addAll(serviceName, portName);

		VBox vBox = new VBox();
		vBox.getChildren().addAll(tableView);
		Scene scene = new Scene(vBox);
		priStage.setScene(scene);
		priStage.show();
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

}