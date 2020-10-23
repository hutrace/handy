package org.hutrace.handy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Command extends Logger {
	
	private static Command instance;
	
	private Command() {}
	
	public static Command instance() {
		if(instance == null) {
			instance = new Command();
		}
		return instance;
	}
	
	public String linux(String command) {
		String[] cmds = new String[] { "/bin/sh", "-c", command };
		Runtime runtime = Runtime.getRuntime();

		try {
			StringBuilder result = new StringBuilder();
			Process process = runtime.exec(cmds);
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;

			while ((line = br.readLine()) != null) {
				this.log.debug(System.lineSeparator());
				this.log.debug(line);
				result.append(System.lineSeparator());
				result.append(line);
			}

			is.close();
			isr.close();
			br.close();
			return result.toString();
		} catch (IOException var10) {
			var10.printStackTrace();
			return null;
		}
	}
	
	public String windows(String command) {
		String[] cmds = new String[] { "cmd", "/C", command };
		Runtime runtime = Runtime.getRuntime();

		try {
			StringBuilder result = new StringBuilder();
			Process process = runtime.exec(cmds);
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;

			while ((line = br.readLine()) != null) {
				this.log.debug(line);
				result.append(line);
				result.append("\r\n");
			}

			is.close();
			isr.close();
			br.close();
			return result.toString();
		} catch (IOException var10) {
			var10.printStackTrace();
			return null;
		}
	}
	
}