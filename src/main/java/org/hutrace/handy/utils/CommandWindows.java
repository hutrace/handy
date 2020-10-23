package org.hutrace.handy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandWindows extends Logger {
	public static CommandWindows createWindowsCommand() {
		return new CommandWindows();
	}

	public String callingSys(String command) {
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