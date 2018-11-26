package application;


import java.util.concurrent.ExecutionException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
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
	public void handleMouseClick(MouseEvent arg0) {
		System.out.println("clicked on " + view.getSelectionModel().getSelectedItem());
		System.out.println( view.getSelectionModel().getSelectedItem().getClass());
		ObservableList<Integer> list = FXCollections.observableArrayList();
		DisplayResult displayPort=view.getSelectionModel().getSelectedItem();
		if(displayPort!=null) {
			for (Integer port:displayPort.getPort()) {
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
//		scan_thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					scanner.scan();
//				} catch (InterruptedException | ExecutionException exception) {
//					exception.printStackTrace();
//				}
//			}
//		});
		scan_thread= new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				try {
					scanner.scan();
				} catch (InterruptedException | ExecutionException exception) {
					exception.printStackTrace();
				}
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
	private static class UpdatePort extends ListCell<Integer> {
		@Override
		public void updateItem(Integer item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
				setText(item+"");
			}
		}
	}

}
