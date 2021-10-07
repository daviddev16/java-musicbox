package org.musicbox.config;

import org.musicbox.core.config.JsonConfiguration;

public class Configs extends JsonConfiguration {

  private static Configs configs;
  
  public static final int VOLUME = 150/2;
  public static String YOUTUBE_API_KEY;
  public static int DELETE_MESSAGE_AFTER;
  public static boolean DEBUG_MODE;
  public static String TOKEN;
  public static String VERSION;
  public static String NAME;
  public static String OWNER;
  public static String PREFIX;
  public static String LANGUAGE;

  public Configs(String name) {
	super(name);
	NAME = getString("name");
	VERSION = getString("version");
	DEBUG_MODE = getBoolean("debugMode");
	TOKEN = getString(DEBUG_MODE ? "debugToken" : "releaseToken");
	OWNER = getString("owner");
	PREFIX = getString(DEBUG_MODE ? "debugCommandPrefix" : "commandPrefix");
	YOUTUBE_API_KEY = getString("youtubeApiKey");
	DELETE_MESSAGE_AFTER = getInt("deleteMessageAfter");
	LANGUAGE = getString("language"); /* default debug language */
	configs = this;
  }
  
  public static void setup(String name) {
	new Configs(name);
  }

  public static Configs getConfigs() {
	return configs;
  }

}
