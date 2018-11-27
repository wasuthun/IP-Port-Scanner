package util;

public class ScanResult {

	private String ipaddr;
	private int port;
	private boolean isOpen;
	private String ping;

	public ScanResult(String ipaddr, int port, boolean isOpen, String ping) {
		super();
		this.ipaddr = ipaddr;
		this.port = port;
		this.isOpen = isOpen;
		this.setPing(ping);
	}

	public int getPort() {
		return port;
	}

	public String getPing() {
		return this.ping;
	}

	public String getIp() {
		return this.ipaddr;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setPing(String ping) {
		this.ping = ping;
	}

}