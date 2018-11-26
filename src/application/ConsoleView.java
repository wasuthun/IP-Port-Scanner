package application;

import java.util.Observable;
import java.util.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.NetworkScanner;
import util.ScanResult;

public class ConsoleView implements Observer {

	private NetworkScanner network;
	private ObservableList<ScanResult> list = FXCollections.observableArrayList();

	public ConsoleView(NetworkScanner network) {
		this.network = network;
	}

	public ObservableList<ScanResult> getList() {
		return list;
	}

	@Override
	public void update(Observable o, Object arg) {
		ScanResult s = (ScanResult) arg;
		list.add(s);
		System.out.printf("%s with %s Port \n", s.getIp(), s.getPort());
	}

}
