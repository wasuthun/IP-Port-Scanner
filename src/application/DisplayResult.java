package application;

import java.util.ArrayList;
import java.util.List;

public class DisplayResult {

	private String ipaddr;

	private List<Integer> port;

	public DisplayResult(String ipaddr, int port) {
		this.port = new ArrayList<Integer>();
		this.ipaddr = ipaddr;
		this.port.add(port);
	}

	public void addPort(int port) {
		this.port.add(port);
	}

	public List<Integer> getPort() {
		return port;
	}

	public String getIp() {
		return this.ipaddr;
	}

}
