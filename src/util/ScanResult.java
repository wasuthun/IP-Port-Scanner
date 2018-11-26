package util;

public class ScanResult {
	private String ipaddr;
	private int port;
	private boolean isOpen;

	public ScanResult(String ipaddr, int port, boolean isOpen) {
		super();
		this.ipaddr = ipaddr;
		this.port = port;
		this.isOpen = isOpen;
	}

	public int getPort() {
		return port;
	}

	public String getIp() {
		return this.ipaddr;
	}

	public boolean isOpen() {
		return isOpen;
	}

}