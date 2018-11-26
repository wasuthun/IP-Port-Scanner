package application;

import java.util.Observable;
import java.util.Observer;

import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;
import util.NetworkScanner;
import util.ScanResult;

public class ConsoleView implements Observer {

	private NetworkScanner network;

	public ConsoleView(NetworkScanner network) {
		this.network = network;
	}

	@Override
	public void update(Observable o, Object arg) {
		ScanResult s = (ScanResult) arg;
		System.out.printf("%s with %s Port \n", s.getIp(), s.getPort());
	}

}
