package org.musicbox.config;

import java.util.List;

import org.musicbox.models.Placeholder;
import org.musicbox.utils.EmbedTranslator;

import com.google.gson.JsonObject;

public class MusicBoxConfiguration extends JsonConfiguration {

  public MusicBoxConfiguration(String name) {
	super(name);
  }

  public String getName() {
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

  /* it applies the placeholders into the failed message */
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
  }

}
