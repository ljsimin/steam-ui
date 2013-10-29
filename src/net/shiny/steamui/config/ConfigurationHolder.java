package net.shiny.steamui.config;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;


public final class ConfigurationHolder {
	private static final String CONFIGURATION_PROPERTIES = "configuration.properties";
	private static final String API_PROPERTIES = "api.properties";

	/**
	 * Application configuration
	 */
	public static final Properties configuration;
	/**
	 * API keys for web services. Should not be under revision control.
	 * Create your file with your own keys.
	 */
	public static final Properties api;
	
	private static final Logger LOG = Logger.getLogger(ConfigurationHolder.class.getName());
	
	static {
		configuration = new Properties();
		api = new Properties();
		try {
			configuration.load(ConfigurationHolder.class.getResourceAsStream(CONFIGURATION_PROPERTIES));
		} catch (IOException e) {
			LOG.severe("Cannot load property file " + CONFIGURATION_PROPERTIES);
		}
		try {
			api.load(ConfigurationHolder.class.getResourceAsStream(API_PROPERTIES));
		} catch (IOException e) {
			LOG.severe("Cannot load property file " + API_PROPERTIES);
		}
	}
}
