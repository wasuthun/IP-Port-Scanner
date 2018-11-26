package application;

import java.util.Observable;
import java.util.Observer;

import util.NetworkScanner;

public class ConsoleView implements Observer{
	
	private NetworkScanner network;

	public ConsoleView(NetworkScanner network) {
		this.network=network;
	}
	@Override
	public void update(Observable o, Object arg) {
		if(arg!=null)
			System.out.println(arg);
		System.out.printf("Count : \n");
	}

}
