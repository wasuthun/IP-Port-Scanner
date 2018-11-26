package application;

import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import util.NetworkScanner;

public class SampleController {
	@FXML
	private Button Play;
	@FXML
	private Button Pause;
	@FXML
	private Button Stop;
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

	public void pause(ActionEvent e) {
		System.out.println("Pause");
		try {
			scan_thread.wait(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void stop(ActionEvent e) {
		System.out.println("Stop");
		if (scan_thread != null) {
			scan_thread.stop();
		} else {
			System.out.println("null");
		}
	}

	public void setNetworkScanner(NetworkScanner scanner) {
		this.scanner = scanner;
	}

}