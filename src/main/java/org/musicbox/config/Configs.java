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
	LANGUAGE = getString("language");
	configs = this;
  }
  
  public static void setup(String name) {
	new Configs(name);
  }

  public static Configs getConfigs() {
	return configs;
  }

  /*public String getName() {
	return get("name").getAsString();
  }

  public String getVersion() {
	return get("version").getAsString();
  }

  public boolean isDebugMode() {
	return get("debugMode").getAsBoolean();
  }

  public JsonObject getAuthentication() {
	return get("authentication").getAsJsonObject();
  }

  public String getDebugToken() {
	return getAuthentication().get("debugApplicationToken").getAsString();
  }

  public String getMainToken() {
	return getAuthentication().get("mainApplicationToken").getAsString();
  }

  public String getYoutubeAPIKey() {
	return getAuthentication().get("youtubeApiKey").getAsString();
  }

  public JsonObject getMessages(String language) {
	return getJsonObject("musicBoxMessages").get(language).getAsJsonObject();
  }

  public JsonObject getMessages(String language, String key) {
	return getJsonObject("musicBoxMessages").get(language).getAsJsonObject().get(key).getAsJsonObject();
  }

  // it applies the placeholders into the failed message
  public String getFailedReason(String language, int select, List<Placeholder> placeholders) {
	return EmbedTranslator.translateCodes(getJsonObject("musicBoxMessages").get("failReasons").getAsJsonObject()
		.get(language).getAsJsonArray().get(select).getAsString(), placeholders);
  }

  public String getDefaultCommandPrefix() {
	return get("commandPrefix").getAsString();
  }

  public String getDebugCommandPrefix() {
	return getJsonObject("debugOptions").get("commandPrefix").getAsString();
  }

  public String getOwnerTag() {
	return get("owner").getAsString();
  }*/

}
