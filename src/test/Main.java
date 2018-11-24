package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.javafx.PlatformUtil;

public class Main {

	public static void main(final String... args) throws InterruptedException, ExecutionException {
		final ExecutorService es = Executors.newFixedThreadPool(1000);
		final int timeout = 100;
		final List<Future<ScanResult>> futures = new ArrayList<>();
		List<String> ipRange = getIpList("127.0.0.0", "127.0.0.1");

		for (String ipaddr : ipRange) {
			if (isReadable(ipaddr)) {
				System.out.println("ping: " + ping(ipaddr));
				for (int port = 1; port <= 65535; port++) {
					futures.add(portIsOpen(es, ipaddr, port, timeout));
				}

			} else {
				System.out.println(ipaddr + " is not reachable");
			}
		}
		es.awaitTermination(200L, TimeUnit.MILLISECONDS);

		int openPorts = 0;
		for (final Future<ScanResult> f : futures) {
			if (f.get().isOpen()) {
				openPorts++;

				System.out.println(f.get().getIp() + " with " + f.get().getPort() + " Port.");
			}
		}
	}

	public static boolean isReadable(String host) {
		try {
			return InetAddress.getByName(host).isReachable(2000);
		} catch (Exception e) {
			return false;
		}

	}

	public static String ping(String host) {
		PingStrategy pingStategy;
		if (PlatformUtil.isMac()) {
			pingStategy = new MacOS(host);
		} else {
			pingStategy = new Windows(host);
		}
		return pingStategy.ping();
	}

	public static Future<ScanResult> portIsOpen(final ExecutorService es, final String ip, final int port,
			final int timeout) {
		return es.submit(new Callable<ScanResult>() {
			@Override
			public ScanResult call() {
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), timeout);
					socket.close();
					return new ScanResult(ip, port, true);
				} catch (Exception ex) {
					return new ScanResult(ip, port, false);
				}
			}
		});
	}

	public static abstract class PingStrategy {

		private Pattern pattern;
		private String command;
		private String host;

		public PingStrategy(String host, String regex, String command) {
			this.pattern = Pattern.compile(regex);
			this.command = command;
			this.host = host;
		}

		public String ping() {
			try {
				Runtime r = Runtime.getRuntime();
				System.out.println("Sending Ping Request to " + host);
				Process p = r.exec(command + host);
				Matcher m = null;
				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					m = pattern.matcher(inputLine);
					if (m.find())
						return getPing(m.group(1));
				}
				in.close();
				return "ping command is not supported.";
			} catch (Exception e) {
				e.printStackTrace();
				return "ping error.";
			}
		}

		public abstract String getPing(String line);

	}

	public static class MacOS extends PingStrategy {

		public MacOS(String host) {
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

	public static class Windows extends PingStrategy {

		public Windows(String host) {
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

	public static final String nextIpAddress(final String input) {
		final String[] tokens = input.split("\\.");
		if (tokens.length != 4)
			throw new IllegalArgumentException();
		for (int i = tokens.length - 1; i >= 0; i--) {
			final int item = Integer.parseInt(tokens[i]);
			if (item < 255) {
				tokens[i] = String.valueOf(item + 1);
				for (int j = i + 1; j < 4; j++) {
					tokens[j] = "0";
				}
				break;
			}
		}
		return new StringBuilder().append(tokens[0]).append('.').append(tokens[1]).append('.').append(tokens[2])
				.append('.').append(tokens[3]).toString();
	}

	public static List<String> getIpList(String startIp, String endIp) {
		List<String> result = new ArrayList<>();
		String currentIp = startIp;
		result.add(startIp);
		while (!currentIp.equals(endIp)) {
			String nextIp = nextIpAddress(currentIp);
			result.add(nextIp);
			currentIp = nextIp;
		}
		return result;
	}

	public static class ScanResult {
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

		public void setPort(int port) {
			this.port = port;
		}

		public boolean isOpen() {
			return isOpen;
		}

		public void setOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}
	}
}
