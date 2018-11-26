package util;

public class PingWIndows extends PingStrategy {
	public PingWIndows(String host) {
		super(host, "Average = (\\d+)", "ping -n 1 ");
	}

	@Override
	public String getPing(String line) {
		try {
			return Integer.parseInt(line) + "ms";
		} catch (Exception e) {
			return "Can't convert String to Integer.";
		}
	}
}
