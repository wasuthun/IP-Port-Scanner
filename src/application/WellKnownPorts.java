package application;

public class WellKnownPorts {
	private String port;
	private String service;

	WellKnownPorts(String sName, String pName) {
		this.service = sName;
		this.port = pName;
	}

	public String getPort() {
		return port;
	}

	public String getService() {
		return service;
	}

	public void setPort(String pName) {
		this.port = pName;
	}

	public void setService(String sName) {
		this.service = sName;
	}
}
