package util;

public class Summary {

	private int openPorts;
	private int closePorts;

	private int deadIPs;
	private int aliveIPs;

	public Summary(int openPorts, int closePorts, int deadIps, int aliveIps) {
		this.openPorts = openPorts;
		this.closePorts = closePorts;
		this.deadIPs = deadIps;
		this.aliveIPs = aliveIps;
	}

	public int getOpenPorts() {
		return openPorts;
	}

	public int getClosePorts() {
		return closePorts;
	}

	public int getDeadIPs() {
		return this.deadIPs;
	}

	public int getAliveIPs() {
		return this.aliveIPs;
	}

}
