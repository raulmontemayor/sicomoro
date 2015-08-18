package org.iemm.sicomoro.service;

import java.io.File;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigService {
	private static final Logger LOG = LoggerFactory.getLogger(ConfigService.class);
	
	private static final String CONFIG_FILE_NAME = System.getProperty("user.home")
			+ "/.sicomoro/preferences.properties";
	
	private final CompositeConfiguration CONFIG = new CompositeConfiguration();
	private final static ConfigService INSTANCE = new ConfigService();
	private PropertiesConfiguration configuration = null;
	private PropertiesConfiguration defaultConfig = null;
	
	private ConfigService() {
		try {
			final File file = new File(CONFIG_FILE_NAME);
			if(!file.exists()) {
				configuration = new PropertiesConfiguration("default-preferences.properties");
				configuration.setFileName(CONFIG_FILE_NAME);
				configuration.save();
			}
			configuration = new PropertiesConfiguration(CONFIG_FILE_NAME);
			CONFIG.addConfiguration(configuration);
			
			defaultConfig = new PropertiesConfiguration("default-preferences.properties");
			CONFIG.addConfiguration(defaultConfig);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public static Configuration getConfig() {
		return INSTANCE.CONFIG;
	}
	
	public static void saveConfig(String key, Object value) {
		try {
			INSTANCE.configuration.setProperty(key, value);
			INSTANCE.configuration.save(CONFIG_FILE_NAME);
		} catch (ConfigurationException e) {
			LOG.error(e.getMessage(), e);
		}
	}


}
