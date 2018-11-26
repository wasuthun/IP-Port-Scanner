package util;

public class PingMacOS extends PingStrategy {

	public PingMacOS(String host) {
		super(host, "min/avg/max/stddev = (\\S+)", "ping -c 1 ");
	}

	public String getPing(String line) {
		try {
			return Double.parseDouble((line.split("/"))[1]) + "ms";
		} catch (Exception e) {
			return "Can't convert String to Integer.";
		}
	}
}
