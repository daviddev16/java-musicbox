package org.musicbox.core.utils;

import java.awt.Color;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public final class EmbedUtils {

   @SuppressWarnings("unused")
   public static MessageEmbed translate(JsonObject jsonObject, List<Placeholder> placeholders) {
      EmbedBuilder builder = new EmbedBuilder();
      if (jsonObject.has("author")) {
         builder.setAuthor(
               translatePlaceholders(jsonObject.get("author").getAsJsonObject().get("name").getAsString(), placeholders),
               translatePlaceholders(jsonObject.get("author").getAsJsonObject().get("url").getAsString(), placeholders),
               translatePlaceholders(jsonObject.get("author").getAsJsonObject().get("iconUrl").getAsString(), placeholders));
      }
      if (jsonObject.has("footer")) {
         builder.setFooter(
               translatePlaceholders(jsonObject.get("footer").getAsJsonObject().get("text").getAsString(), placeholders),
               translatePlaceholders(jsonObject.get("footer").getAsJsonObject().get("iconUrl").getAsString(), placeholders));
      }
      if (jsonObject.has("description")) {
         builder.setDescription(translatePlaceholders(jsonObject.get("description").getAsString(), placeholders));
      }
      if (jsonObject.has("color")) {
         builder.setColor(Color.decode("#" + translatePlaceholders(jsonObject.get("color").getAsString(), placeholders)));
      }
      if (jsonObject.has("image")) {
         builder.setImage(translatePlaceholders(jsonObject.get("image").getAsString(), placeholders));
      }
      if (jsonObject.has("thumbnail") && Constants.ENABLE_EMBED_THUMBNAILS) {
         builder.setThumbnail(translatePlaceholders(jsonObject.get("thumbnail").getAsString(), placeholders));
      }
      if (jsonObject.has("title")) {
         builder.setTitle(translatePlaceholders(jsonObject.get("title").getAsString(), placeholders));
      }
      if (jsonObject.has("fields")) {
         JsonArray fieldArray = jsonObject.get("fields").getAsJsonArray();
         fieldArray.forEach(fieldElement -> {
            JsonObject fieldObject = fieldElement.getAsJsonObject();
            if (fieldObject.has("#blank")) {
               builder.addBlankField(fieldObject.get("#black").getAsBoolean());
            } else {
               builder.addField(translatePlaceholders(fieldObject.get("name").getAsString(), placeholders),
                     translatePlaceholders(fieldObject.get("value").getAsString(), placeholders),
                     fieldObject.get("inline").getAsBoolean());
            }
         });
      }

      return builder.build();
   }

   public static String translatePlaceholders(String text, List<Placeholder> placeholders) {
      String translatedCode = text;
      for (Placeholder placeholder : placeholders) {
         translatedCode = translatedCode.replace(placeholder.getCode(), placeholder.getReplacement());
      }
      return translatedCode;
   }

}
