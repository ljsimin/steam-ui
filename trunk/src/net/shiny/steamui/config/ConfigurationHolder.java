package net.shiny.steamui.config;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;


public final class ConfigurationHolder {
	private static final String CONFIGURATION_PROPERTIES = "/configuration.properties";

	public static final Properties properties;
	
	private static final Logger LOG = Logger.getLogger(ConfigurationHolder.class.getName());
	
	static {
		properties = new Properties();
		try {
			properties.load(ConfigurationHolder.class.getResourceAsStream(CONFIGURATION_PROPERTIES));
		} catch (IOException e) {
			LOG.severe("Cannot load property file " + CONFIGURATION_PROPERTIES);
		}
	}
}
