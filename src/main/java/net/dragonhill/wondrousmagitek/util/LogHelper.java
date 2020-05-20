package net.dragonhill.wondrousmagitek.util;

import net.dragonhill.wondrousmagitek.config.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
	public static final Logger LOGGER = LogManager.getLogger(Constants.modId);

	public static Logger getLogger() {
		return LOGGER;
	}
}
