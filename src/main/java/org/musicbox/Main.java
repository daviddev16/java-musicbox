package org.musicbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		LOGGER.info("Starting MusicBox...");
		try {
			LOGGER.info("\n___  ___          _     ______           \r\n" + 
					"|  \\/  |         (_)    | ___ \\          \r\n" + 
					"| .  . |_   _ ___ _  ___| |_/ / _____  __\r\n" + 
					"| |\\/| | | | / __| |/ __| ___ \\/ _ \\ \\/ /\r\n" + 
					"| |  | | |_| \\__ \\ | (__| |_/ / (_) >  < \r\n" + 
					"\\_|  |_/\\__,_|___/_|\\___\\____/ \\___/_/\\_\\\r\n" + 
					"                                         \r\n" + 
					"                                         \r\n" + 
					"                                         ");

			new MusicBox();
		} catch (Exception e) {
			LOGGER.error("Error while starting MusicBox.", e);
			System.exit(-1);
		}
	}

}
