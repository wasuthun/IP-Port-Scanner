package application;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.NetworkScanner;
import util.ScanResult;
import util.Summary;

public class NetworkObserver implements Observer {

	private ObservableList<DisplayResult> list = FXCollections.observableArrayList();

	private HashMap<String, DisplayResult> map = new HashMap<String, DisplayResult>();

	private SampleController controller;

	private NetworkScanner network;

	public NetworkObserver(NetworkScanner network) {
		this.network = network;
		network.addObserver(this);
	}

	public ObservableList<DisplayResult> getList() {
		return list;
	}

	public void setController(SampleController controller) {
		this.controller = controller;
	}

	@Override
	public void update(Observable o, Object arg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (arg instanceof ScanResult) {
					ScanResult s = (ScanResult) arg;
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
						controller.setStatus("Scanning... " + s.getIp() + " with port " + s.getPort());
						list.add(result);
					}
				} else if (arg instanceof Summary) {
					Summary s = (Summary) arg;
					controller.finished();
					controller.setStatus("Reachable IP " + s.getAliveIPs() + " ,Unreachable IP " + s.getDeadIPs()
							+ " ,Open Port " + s.getOpenPorts());
					map.clear();
				}

			}
		});
	}

}
