package util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.sun.javafx.PlatformUtil;

public class NetworkScanner extends Observable {

	private int fromPort;
	private int toPort;
	private boolean isRunning;
	List<Future<ScanResult>> futures;

	public NetworkScanner(int fromPort, int toPort) {
		this.isRunning = false;
		this.fromPort = fromPort;
		this.toPort = toPort;
	}

	public void scan(String fromIP,String toIP) {
		try {
			this.isRunning = true;
			final ExecutorService es = Executors.newFixedThreadPool(500);
			int timeout = 100;
			futures = new ArrayList<>();
			List<String> ipRange = getIpList(fromIP, toIP);
			for (String ipaddr : ipRange) {
				if (!isRunning)
					break;
				if (isReachable(ipaddr)) {
					System.out.println("ping: " + ping(ipaddr));
					for (int port = fromPort; port <= toPort; port++) {
						if (!isRunning)
							break;
						futures.add(portIsOpen(es, ipaddr, port, timeout));
					}
				} else {
					System.out.println(ipaddr + " is not reachable");
				}
			}
			es.awaitTermination(200L, TimeUnit.MILLISECONDS);

			// int openPorts = 0;
			// for (final Future<ScanResult> f : futures) {
			// try {
			// if (f.get().isOpen()) {
			// openPorts++;
			// System.out.println(f.get().getIp() + " with " + f.get().getPort()
			// + " Port555.");
			//
			// }
			// } catch (InterruptedException e) {
			//
			// } catch (ExecutionException e) {
			//
			// }
			// }
		} catch (InterruptedException e) {

		}
	}

	public void stop() {
		this.isRunning = false;
		for (final Future<ScanResult> f : futures) {
			f.cancel(true);
		}
	} 

	private boolean isReachable(String host) {
		try {
			return InetAddress.getByName(host).isReachable(2000);
		} catch (Exception e) {
			return false;
		}
	}

	private String ping(String host) {
		PingStrategy pingStategy;
		if (PlatformUtil.isWindows()) {
			pingStategy = new PingWIndows(host);
		} else {
			pingStategy = new PingMacOS(host);
		}
		return pingStategy.ping();
	}

	private Future<ScanResult> portIsOpen(final ExecutorService es, final String ip, final int port,
			final int timeout) {
		return es.submit(new Callable<ScanResult>() {
			@Override
			public ScanResult call() {
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), timeout);
					socket.close();
					/* send Scan Result to UI */
					setChanged();
					notifyObservers(new ScanResult(ip, port, true));
					return new ScanResult(ip, port, true);
				} catch (Exception ex) {/* send Scan Result to UI */
					setChanged();
					notifyObservers(new ScanResult(ip, port, false));
					return new ScanResult(ip, port, false);
				}
			}
		});
	}

	private final String nextIpAddress(final String input) {
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

	private List<String> getIpList(String startIp, String endIp) {
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

}
