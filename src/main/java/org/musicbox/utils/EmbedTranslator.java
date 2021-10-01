package org.musicbox.utils;

import java.awt.Color;
import java.util.List;

import org.musicbox.models.Placeholder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public final class EmbedTranslator {

  public static MessageEmbed translate(JsonObject jsonObject, List<Placeholder> placeholders) {
	EmbedBuilder builder = new EmbedBuilder();
	if (jsonObject.has("author")) {
	  builder.setAuthor(
		  translateCodes(jsonObject.get("author").getAsJsonObject().get("name").getAsString(), placeholders),
		  translateCodes(jsonObject.get("author").getAsJsonObject().get("url").getAsString(), placeholders),
		  translateCodes(jsonObject.get("author").getAsJsonObject().get("iconUrl").getAsString(), placeholders));
	}
	if (jsonObject.has("footer")) {
	  builder.setFooter(
		  translateCodes(jsonObject.get("footer").getAsJsonObject().get("text").getAsString(), placeholders),
		  translateCodes(jsonObject.get("footer").getAsJsonObject().get("iconUrl").getAsString(), placeholders));
	}
	if (jsonObject.has("description")) {
	  builder.setDescription(translateCodes(jsonObject.get("description").getAsString(), placeholders));
	}
	if (jsonObject.has("color")) {
	  builder.setColor(Color.decode("#" + translateCodes(jsonObject.get("color").getAsString(), placeholders)));
	}
	if (jsonObject.has("image")) {
	  builder.setImage(translateCodes(jsonObject.get("image").getAsString(), placeholders));
	}
	if (jsonObject.has("thumbnail") && I18n.DISABLE_EMBED_THUMBNAILS) {
	  builder.setThumbnail(translateCodes(jsonObject.get("thumbnail").getAsString(), placeholders));
	}
	if (jsonObject.has("title")) {
	  builder.setTitle(translateCodes(jsonObject.get("title").getAsString(), placeholders));
	}
	if (jsonObject.has("fields")) {
	  JsonArray fieldArray = jsonObject.get("fields").getAsJsonArray();
	  fieldArray.forEach(fieldElement -> {
		JsonObject fieldObject = fieldElement.getAsJsonObject();
		if (fieldObject.has("#blank")) {
		  builder.addBlankField(fieldObject.get("#black").getAsBoolean());
		} else {
		  builder.addField(translateCodes(fieldObject.get("name").getAsString(), placeholders),
			  translateCodes(fieldObject.get("value").getAsString(), placeholders),
			  fieldObject.get("inline").getAsBoolean());
		}
	  });
	}

	return builder.build();
  }

  public static String translateCodes(String text, List<Placeholder> placeholders) {
	String translatedCode = text;
	for (Placeholder placeholder : placeholders) {
	  translatedCode = translatedCode.replace(placeholder.getCode(), placeholder.getReplacement());
	}
	return translatedCode;
  }

}
