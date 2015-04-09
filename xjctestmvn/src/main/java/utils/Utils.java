package utils;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class Utils {
	public static String getFile(String fileName) {

		String result = "";

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			result = IOUtils
					.toString(classLoader.getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}
}
