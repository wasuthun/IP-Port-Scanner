package application;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import util.Summary;

public class DisplayResult {

	private String ipaddr;
	private String ping;

	private List<Integer> port;

	public DisplayResult(String ipaddr, int port, String ping) {
		this.port = new ArrayList<Integer>();
		this.ipaddr = ipaddr;
		this.port.add(port);
		this.ping = ping;
	}

	public void addPort(int port) {
		this.port.add(port);
	}

	public List<Integer> getPort() {
		return port;
	}

	public String getIpaddr() {
		return this.ipaddr;
	}

	public String getPing() {
		return ping;
	}

	public void setPing(String ping) {
		this.ping = ping;
	}

}
