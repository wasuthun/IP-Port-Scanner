package test;

import util.NetworkScanner;

public class Main {

	public static void main(final String... args) {
		NetworkScanner scanner = new NetworkScanner();
		System.out.println(scanner.dnsLookup("127.0.0.1"));
	}
}