package application;

import java.util.concurrent.ExecutionException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import util.NetworkScanner;
import util.ScanResult;

public class SampleController {
	@FXML
	private Button Play;
	@FXML
	private ListView<ScanResult> view;

	private Thread scan_thread;
	private NetworkScanner scanner;
	
	
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
		this.scanner=scanner;
	}
	public void show(ConsoleView console) {
		//System.out.println(console.getList());
		view.setItems(console.getList());
		view.setCellFactory(new Callback<ListView<ScanResult>, 
	            ListCell<ScanResult>>() {
	                @Override 
	                public ListCell<ScanResult> call(ListView<ScanResult> list) {
	                    return new RectCell();
	                }
	            }
	        );
	}
	static class RectCell extends ListCell<ScanResult> {
        @Override
        public void updateItem(ScanResult item, boolean empty) {
            super.updateItem(item, empty);
            Rectangle rect = new Rectangle(100, 20);
            if (item != null) {
            		setText(item.getIp()+" with "+item.getPort()+" Port.");
            }
        }
    }

}
