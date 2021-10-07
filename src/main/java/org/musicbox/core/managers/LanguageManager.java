package org.musicbox.core.managers;

import java.util.HashMap;
import java.util.Map;

import org.musicbox.core.config.LanguageConfiguration;
import org.musicbox.core.utils.Constants;

import com.google.gson.JsonObject;

public final class LanguageManager {

  private static LanguageManager languageManager;
  private Map<String, LanguageConfiguration> languagesMap;

  private LanguageManager() {
	languageManager = this;
	languagesMap = new HashMap<>();

	createLanguage(Constants.PT_BR, 
		LanguageConfiguration.createLanguage(Constants.PT_BR));
  }

  public void createLanguage(String languageId, LanguageConfiguration languageConfiguration) {
	languagesMap.put(languageId, languageConfiguration);
  }

  public static LanguageManager getLanguageManager() {
	return languageManager;
  }

  public static void setup() {
	new LanguageManager();
  }

  public JsonObject getMessage(String languageId, int messageId) {
	return languagesMap.get(languageId).getMessage(messageId).getAsJsonObject();
  }

}
