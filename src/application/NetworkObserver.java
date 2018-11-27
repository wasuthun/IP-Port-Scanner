package application;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.NetworkScanner;
import util.ScanResult;

public class NetworkObserver implements Observer {

	private ObservableList<DisplayResult> list = FXCollections.observableArrayList();

	private HashMap<String, DisplayResult> map = new HashMap<String, DisplayResult>();

	public NetworkObserver(NetworkScanner network) {
		network.addObserver(this);
	}

	public ObservableList<DisplayResult> getList() {
		return list;
	}

	@Override
	public void update(Observable o, Object arg) {
		ScanResult s = (ScanResult) arg;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (s.isOpen()) {
					DisplayResult result = null;
					if (map.containsKey(s.getIp())) {
						result = map.get(s.getIp());
						result.addPort(s.getPort());
						list.remove(result);
					} else {
						result = new DisplayResult(s.getIp(), new Integer(s.getPort()), s.getPing());
					}
					map.put(s.getIp(), result);
					list.add(result);
				}
			}
		});
	}

}
