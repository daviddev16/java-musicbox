package org.musicbox.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class JsonConfiguration {

  private JsonObject map;

  public JsonConfiguration(String name) {
	InputStream configStream = JsonConfiguration.class.getResourceAsStream("/" + name + ".json");
	JsonReader reader = new JsonReader(new InputStreamReader(configStream));
	map = new Gson().fromJson(reader, JsonObject.class);
	try {
	  configStream.close();
	  reader.close();
	} catch (IOException e) {
	  e.printStackTrace();
	}
  }

  public JsonElement get(String memberName) {
	return map.get(memberName);
  }

  public JsonObject getJsonObject(String memberName) {
	return get(memberName).getAsJsonObject();
  }
  
  public JsonArray getJsonArray(String memberName) {
 	return get(memberName).getAsJsonArray();
   }

  public String getString(String memberName) {
	return get(memberName).getAsString();
  }

  public boolean getBoolean(String memberName) {
	return get(memberName).getAsBoolean();
  }
  
  public int getInt(String memberName) {
	return get(memberName).getAsInt();
  }

}
