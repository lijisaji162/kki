package com.polus.fibicomp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadPropertyFile {

	private static Properties props = null;
	private static String propertyFileName = null;

	/**
	 * Get a property value from the <code>coeus.properties</code> file.
	 *
	 * @param key An key value
	 * @return The property value or default value if no property exists
	 * @throws IOException
	 */
	public static String getProperty(String key, String propertyFile) throws IOException {
		if (props == null || (!propertyFile.equalsIgnoreCase(propertyFileName))) {
			synchronized (ReadPropertyFile.class) {
				if (props == null || (!propertyFile.equalsIgnoreCase(propertyFileName))) {
					props = loadProperties(propertyFile);
					propertyFileName = propertyFile;
				}
			}
		}
		return props.getProperty(key);
	}

	/**
	 * Load Properties
	 *
	 * @return The Properties
	 * @throws IOException
	 */
	private static Properties loadProperties(String propertyFile) throws IOException {
		InputStream stream = null;
		try {
			props = new Properties();
			stream = new ReadPropertyFile().getClass().getResourceAsStream(propertyFile);
			props.load(stream);

		} finally {
			try {
				stream.close();
			} catch (Exception ex) {

			}
		}
		return props;
	}

}
