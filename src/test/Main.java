package test;

import util.NetworkScanner;

public class Main {

	public static void main(final String... args) {
		NetworkScanner scanner = new NetworkScanner();
		System.out.println(scanner.dnsLookup("74.125.24.94"));
	}
}