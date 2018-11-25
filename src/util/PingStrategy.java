package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PingStrategy {

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
			return "ping error";
		} catch (Exception e) {
			e.printStackTrace();
			return "ping error.";
		}
	}

	public abstract String getPing(String line);

}