package org.musicbox.config;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class JsonConfiguration  {

	private JsonObject rootJsonMap;
	
	public JsonConfiguration(String name) {
		InputStream configStream = JsonConfiguration.class.getResourceAsStream("/" + name + ".json");
		JsonReader reader = new JsonReader(new InputStreamReader(configStream));
		rootJsonMap = new Gson().fromJson(reader, JsonObject.class);
	}

	public JsonElement get(String memberName) {
		return rootJsonMap.get(memberName);
	}
	
	public JsonObject getJsonObject(String memberName) {
		return get(memberName).getAsJsonObject();
	}
	
}
