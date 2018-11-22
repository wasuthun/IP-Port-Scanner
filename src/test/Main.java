package test;

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

public class Main {

	public static void main(final String... args) throws InterruptedException, ExecutionException {
		final ExecutorService es = Executors.newFixedThreadPool(1000);
		final int timeout = 100;
		final List<Future<ScanResult>> futures = new ArrayList<>();
		List<String> ipRange = getIpList("127.0.0.1", 10);
		for (String ipaddr : ipRange) {
			System.out.println("SCANNING... " + ipaddr);
			for (int port = 1; port <= 65535; port++) {
				futures.add(portIsOpen(es, ipaddr, port, timeout));
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

		// System.out.println("There are " + openPorts + " open ports on host "
		// + ip + " (probed with a timeout of "
		// + timeout + "ms)");
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

	public static List<String> getIpList(String startIp, int number) {
		List<String> result = new ArrayList<>();
		String currentIp = startIp;
		for (int i = 0; i < number; i++) {
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
